package com.example.first_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.widget.RelativeLayout;

public class Concurency {
	public static List<account> cli_vec=new ArrayList<account>();
	public static List<Group> group_vec=new ArrayList<Group>();
	public static int flag=0;
	public static RelativeLayout Concurencylayout=null;
	public static List<account> Group_tab_vec=new ArrayList<account>();
	public static String connName="";
	public static account User=null;
	public static HashMap<String,TextListViewActivity> chatMap=new HashMap<String,TextListViewActivity>();
	public static Context currentContext=null;
	
	public static void addChat(String name,TextListViewActivity ad){
		if(chatMap.containsKey(name)){
			chatMap.remove(name);
			chatMap.put(name, ad);
			return;
		}
		
		chatMap.put(name, ad);
	}

	public static void addtoclivec(account vec){
		synchronized(cli_vec) {
			
				cli_vec.add(vec);
			

		}

	}
	
	public static void addtogroupvec(List<Group> vec){
		synchronized(group_vec) {
			for(int i=0;i<vec.size();i++){
				group_vec.add(vec.get(i));
			}

		}

	}

}
