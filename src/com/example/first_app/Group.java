package com.example.first_app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group extends identify implements Serializable{

	private static final long serialVersionUID = 1L;
	//String name,ip,mac;
	//List<account> grouplist=null;
	
	public Group(String name,String mac,List<account> grouplist){
		super(name,mac,grouplist);
		//grouplist=new ArrayList<account>();
		
	}
}
