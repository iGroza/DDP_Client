package ru.kirill.ag;

import android.app.*;
import android.os.*;
import ru.kirill.ag.ddp.*;
import java.util.*;
import android.widget.*;
import android.content.res.*;
import org.w3c.dom.*;
import android.content.*;
import android.widget.AdapterView.*;
import android.view.*;

public class MainActivity extends Activity 
{
	//множество коллекций получаемые из подписок
	Map<String, Map<String, Object>> collection;
	private ExpandableListView listView;
	private DdpExpandableArrayAdapter adapter;
	private ProgressBar pb;
	private Context con;

	private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		pb = findViewById(R.id.mProgBar);
		con = MainActivity.this;
		listView = findViewById(R.id.tv);
		new DdpTask().execute();

    }

	//Наполнение ListView данными
	private void putData(Map<String, Map<String, Object>> collection)
	{
		// TODO: Implement this method
		this.collection = collection;
		if(isFirst){
		adapter = new DdpExpandableArrayAdapter(con, collection);
		listView.setAdapter(adapter);
		isFirst=false;
		}
		adapter.notifyDataSetChanged();
		listView.smoothScrollToPosition(0);
		pb.setVisibility(pb.GONE);
		listView.scrollListBy(listView.SCROLL_INDICATOR_BOTTOM);
		
	}

	//Отдельный поток для реализации DDP
	class DdpTask extends AsyncTask<Void,Map<String, Map<String, Object>>,Void>
	{
		MyDdpClient ddp = new MyDdpClient();
		@Override
		protected Void doInBackground(Void[] p1)
		{
			// TODO: Implement this method
			ddp.start(new MyDdpListener(){

					@Override
					public void onUpdate(Map<String, Map<String, Object>> collection, String json)
					{ 
						// TODO: Implement this method
						publishProgress(collection);
					}
				});
			return null;
		}

		//Отправка промежуточных данных
		@Override
		protected void onProgressUpdate(Map<String, Map<String, Object>>[] values)
		{
			// TODO: Implement this method
			putData((HashMap<String, Map<String, Object>>)(values[0]));
			super.onProgressUpdate(values);
		}
	}
}
