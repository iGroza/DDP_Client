package ru.kirill.ag.ddp;

import android.content.*;
import java.net.*;
import me.kutrumbos.*;

/**
 * Simple example of DDP client use-case that just involves 
 * 		making a connection to a locally hosted Meteor server
 * @author peterkutrumbos
 *
 */
public class MyDdpClient
{
	private MyDdpListener listener;
	private DdpClient ddp;
	public MyDdpClient()
	{	}

	public void call(String method,Object[] params){
		if(ddp!=null)ddp.call(method,params);
	}
	
	public  void start(MyDdpListener listener)
	{
		this.listener = listener;
		// Переменные для подключения к серверу Meteor
		String meteorIp = "cloud.astrapos.ru";
		Integer meteorPort = 3000;
		String login ="9049915511";
		String pass ="1234";
		try
		{
			// create DDP client 
			 ddp = new DdpClient(meteorIp, meteorPort);

			// create DDP client observer
			SimpleDdpClientObserver obs = new SimpleDdpClientObserver(listener, this);

			// add observer
			ddp.addObserver(obs);

			// make connection to Meteor server
			ddp.connect();
			try
			{ 
				Thread.sleep(500);

				System.out.println("calling remote method...");

				Object[] methodArgs = new Object[1];
				methodArgs[0] = new UsernameAuth(login, pass);

				ddp.call("login", methodArgs);

				ddp.subscribe("categories", new Object[]{});
				ddp.subscribe("products", new Object[]{});
			}
			catch (InterruptedException e)
			{
				System.err.println("---> Невозможно усыпить поток во время рестарта клиента\n" + e.getMessage());
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}			
	}
}
