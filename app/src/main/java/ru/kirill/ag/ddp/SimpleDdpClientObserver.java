package ru.kirill.ag.ddp;

import java.util.Observable;
import java.util.Observer; 
import java.util.*;
import com.google.gson.*;
import me.kutrumbos.*; 
import java.security.cert.*;
import ru.kirill.ag.*;

public class SimpleDdpClientObserver implements Observer
{

	private MyDdpListener listener;
	private Gson gson = new Gson();
	private Map<String,Map<String,Object>> mCollection = new HashMap<>();
    private MyDdpClient mDdp;

	private boolean isReady = false;
	public SimpleDdpClientObserver(MyDdpListener listener, MyDdpClient mDdp)
	{
		this.listener = listener;
		this.mDdp = mDdp;
	}

	@Override
	public void update(Observable client, Object msg)
	{
		try
		{
			//System.out.println(msg);
			if (msg instanceof String)
			{
				//Создание коллекции @json для дальнейшей работы с данными
				Map<String,Object> json = gson.fromJson(msg.toString(), HashMap.class);
				String msgType = msgType = json.get(Const.DdpMessage.MSG.toString()).toString();
				//проверка данных на валидность
				if (msgType == null) return;
				//Создание коллекций на основе данных полученных с сервера
				if (msgType.contains(Const.OBSMessage.ADDED.toString()))
				{
					String collName = (String) json.get(Const.DdpField.COLLECTION.toString());
					if (!mCollection.containsKey(collName))
					{
						System.out.println("--> Создана коллекция: " + collName);
						mCollection.put(collName, new HashMap<String,Object>());
						
					}
					//Заполнение данных
					if(msg.toString().contains("~><119><~"))return;
					String id = json.get(Const.DdpField.ID.toString()).toString();
					// id : fields{name , ect.. }=
					mCollection.get(collName).put(id, json.get(Const.DdpField.FIELDS.toString()));
					
				}
				//Изменение информации в коллекции в случае редоктирования данных на сервере
				if (msgType.contains(Const.OBSMessage.CHANGED.toString()))
				{
					String collName = (String) json.get(Const.DdpField.COLLECTION.toString());
					if (mCollection.containsKey(collName))
					{
						Map<String,Object> tempColl = mCollection.get(collName);
						String id = (String) json.get(Const.DdpField.ID.toString());
						Map<String,Object> oldElements = (Map<String, Object>) tempColl.get(id);
						if (oldElements != null)
						{
							Map<String,Object> newElements = (Map<String, Object>) json.get(Const.DdpField.FIELDS.toString());
							
							if (newElements != null)
							{
								System.out.println("--> Заменена значений:");
								for (Map.Entry<String,Object> field:newElements.entrySet())
								{
									System.out.println("> " + field.getKey() + "|" + oldElements.get(field.getKey()) + " -> " + field.getValue());
									oldElements.put(field.getKey(), field.getValue());
								}

							}
							List<String> clearElements=(List<String>) json.get("cleared");
							if (clearElements != null)
							{
								System.out.println("--> Очищены следущие поля:");
								for (String s:clearElements)
								{
									if (oldElements.containsKey(s))
									{
										System.out.println("> " + s);
										oldElements.remove(s);
									}
								}
							}
						}

					}
					else
					{
						System.out.println("---> Неверные значения для параметра @changed");
					}
				}
				if (msgType.contains(Const.OBSMessage.REMOVED.toString()))
				{
					String collName = (String) json.get(Const.DdpField.COLLECTION.toString());
					if (mCollection.containsKey(collName))
					{
						//удаление елемента по ID
						Map<String,Object> tempColl = mCollection.get(collName);
						String id = json.get(Const.DdpField.ID.toString()).toString();
						System.out.println("--> Удален элемент:" + tempColl.get(id));
						
						tempColl.remove(id);
					}
				}
				if(msgType.contains("ready")&&msg.toString().contains("3")){
					isReady = true;
				}
				if(isReady)listener.onUpdate(mCollection, msg.toString());
				
			}
		}
		catch (NullPointerException e)
		{
			System.err.println("---> Ошибка парсинга json, не найден указанный параметр\n" + e.getMessage());
			//{"code":1006,"close":"WebSocketClient connection closed"}
			System.err.println("---> " + msg);
			//reconect client
			if (msg.toString().contains("WebSocketClient")){
			isReady=false;
			mDdp.start(listener);
			}
		}
	}
} 
