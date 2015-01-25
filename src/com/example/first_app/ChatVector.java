package com.example.first_app;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



public class ChatVector {

	static HashMap<String,List<String>> chvec=new HashMap<String,List<String>>();

	public static void addtovec(String name,List<String> ad){
		chvec.put(name, ad);

	}
	public static List<String> getFromVec(String name){
		return chvec.get(name);

	}

	public static List<String> getall(){
		List<String> rowItems=new ArrayList<String>();
		Set<String> s=chvec.keySet();
		if(s.size()>0){
			Iterator<String> si=s.iterator();

			while(si.hasNext())
				rowItems.add(si.next());
			return rowItems;

		}
		return null;
	}


}
