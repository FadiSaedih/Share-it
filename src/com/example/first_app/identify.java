package com.example.first_app;

import java.io.Serializable;
import java.util.List;

import android.widget.ImageView;

public class identify implements Serializable{
	String name,ip,mac;
	List<account> grouplist=null;
	ImageView image=null;
	public identify(String name,String mac){
		this.name=name;
		this.mac=mac;
		
	}
	public identify(String name,String mac,ImageView image){
		this.name=name;
		this.mac=mac;
		this.image=image;
	}
	public identify(String name,String mac,List<account> grouplist){
		this.name=name;
		this.mac=mac;
		this.grouplist=grouplist;
		
	}
}
