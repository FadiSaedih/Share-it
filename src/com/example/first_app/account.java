package com.example.first_app;

import java.io.Serializable;

public class account extends identify implements Serializable{

	private static final long serialVersionUID = 1L;
	//String name,ip,mac;
	
	public account(String name,String mac){
		super(name,mac);
	}

}
