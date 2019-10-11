package ru.kirill.ag;
import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.text.*;

public class DdpExpandableArrayAdapter extends BaseExpandableListAdapter
{
	//Кастомный адаптер для ListView
	private Map<String, Object> categories;
	private Map<String,Map<String,Object>> products;
	private LayoutInflater inflater;
	private Context con;

	public DdpExpandableArrayAdapter(Context con, Map<String, Map<String, Object>> collection)
	{
		if (collection != null)
		{
			this.categories = collection.get("categories");
			this.products = (Map<String, Map<String, Object>>) collection.get("products");
			this.con = con;
			inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}
	//кол-во элементов Parrent
	@Override
	public int getGroupCount()
	{
		// TODO: Implement this method

		return categories == null ? 0 : categories.size();
	}

	//кол-во элементов Chield в небходимом Parrent
	//поиск производиться по параметру 'CategoryId'
	@Override
	public int getChildrenCount(int p1)
	{
		// TODO: Implement this method

		Iterator it = categories.entrySet().iterator();
		int i = 0;
		String id="";
		while (it.hasNext())
		{
			Map.Entry<String,Object> entry = (Map.Entry<String, Object>) it.next();
			if (p1 == i++)id = entry.getKey();
		}
		it = products.entrySet().iterator();
		int count=0;
		while (it.hasNext())
		{
			Map.Entry<String,Map<String,Object>> entry = (Map.Entry<String, Map<String, Object>>) it.next();
			entry.getValue().put("id", entry.getKey());
			if (entry.getValue().get("CategoryId").toString().contains(id))count++;
		}
		return count;
	}

	//плучить элемент родителя
	@Override
	public Object getGroup(int p1)
	{
		// TODO: Implement this method
		Iterator it = categories.entrySet().iterator();
		int i = 0;
		while (it.hasNext())
		{
			Map.Entry<String,Object> entry = (Map.Entry<String, Object>) it.next();
			if (p1 == i++)return entry.getValue();
		}
		return null;
	}

	//получить элемент Child
	@Override
	public Object getChild(int p1, int p2)
	{
		// TODO: Implement this method
		Map<String,Map<String,Object>> sortChild = new HashMap<>();
		Iterator it = categories.entrySet().iterator();
		Iterator it2 = products.entrySet().iterator();
		int i = 0,j = 0;
		String id="";
		while (it.hasNext())
		{
			Map.Entry<String,Object> entry = (Map.Entry<String, Object>) it.next();
			if (p1 == i++)
			{
				id = entry.getKey();
				while (it2.hasNext())
				{

					Map.Entry<String,Map<String,Object>> entry2 = (Map.Entry<String, Map<String, Object>>) it2.next();
					if (entry2.getValue().get("CategoryId").toString().contains(id))
						if (p2 == j++)return entry2.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public long getGroupId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public long getChildId(int p1, int p2)
	{
		// TODO: Implement this method
		return p2;
	}

	@Override
	public boolean hasStableIds()
	{
		// TODO: Implement this method
		return true;
	}

	@Override
	public View getGroupView(int p1, boolean p2, View p3, ViewGroup p4)
	{
		// TODO: Implement this method
		final Map<String,Object> item =(Map<String,Object>)getGroup(p1);
		// TODO: Implement this method
		View view = p3;
		if (view == null)
		{
			view = inflater.inflate(R.layout.ddp_simple_item, p4, false);
		}
		if (!p2)
			((ImageView)view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_folder);
		else((ImageView)view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_open_folder);

		((TextView)view.findViewById(R.id.mName)).setText(item.get("Name").toString());
		try
		{
			((TextView)view.findViewById(R.id.mField)).setText(item.get("ExternalCode").toString());
		}
		catch (NullPointerException e)
		{
			((TextView)view.findViewById(R.id.mField)).setText("null");
		}
		return view;
	}

	//oтображение дочернего элемента
	@Override
	public View getChildView(int p1, int p2, boolean p3, View p4, ViewGroup p5)
	{
		// TODO: Implement this method
		final Map<String,Object> item =(Map<String,Object>)getChild(p1, p2);
		// TODO: Implement this method
		View view = p4;
		if (view == null)
		{
			view = inflater.inflate(R.layout.ddp_simple_item, p5, false);
		}

		final String name = item.get("Name").toString();
		String price;
		try
		{
			price = item.get("Price").toString();
		}
		catch (NullPointerException e)
		{
			price = "цена не установленна";
		}
		String sku = item.get("Sku").toString();
		String id = item.get("id").toString();
		String dateRegex ="[^0-9&\\.&E]";
		String dateUpd = item.get("UpdatedOnUTC").toString();
		String dateCrt = item.get("CreatedOnUTC").toString();
		
		((ImageView)view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_book);
		((TextView)view.findViewById(R.id.mName)).setText(name);
		((TextView)view.findViewById(R.id.mField)).setText("цена: " + price + "");

		final StringBuilder sb = new StringBuilder();
		sb.append("ID: " + id);
		sb.append("\nЦена: " + price);
		sb.append("\nВнешний код: " + sku);
		sb.append("\nДата создания: " + new SimpleDateFormat("dd-MM-yyyy").format(Float.valueOf(dateCrt.replaceAll(dateRegex,""))));
		sb.append("\nДата изменения: " + new SimpleDateFormat("dd-MM-yyyy").format(Float.valueOf(dateUpd.replaceAll(dateRegex,""))));

		view.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					AlertDialog.Builder b = new AlertDialog.Builder(con);
					b.setTitle(name).setMessage(sb).setNegativeButton("OK", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								// TODO: Implement this method
							}
						});
					b.create().show();
				}
			});

		return view;
	}

	@Override
	public boolean isChildSelectable(int p1, int p2)
	{
		// TODO: Implement this method
		return false;
	}

}
