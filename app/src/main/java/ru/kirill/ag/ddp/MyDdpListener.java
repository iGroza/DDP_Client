package ru.kirill.ag.ddp;
import java.util.*;

public abstract class MyDdpListener
{
	//Слушатель для обработки событий в Observer с выводом collection в UI поток
	synchronized public void onUpdate(Map<String, Map<String,Object>> collection,String json){}
	
}
