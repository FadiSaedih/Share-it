package com.Shareitapplication.shareit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class dataServer {
	ServerSocket server; 
	public dataServer() throws IOException, ClassNotFoundException{
		try{
		//server=new ServerSocket(4323);
		}
		catch(Exception e){
			
		}
		//run();
	}
	public void close() throws IOException{
		if(server.isClosed()==false)
		server.close();
	}
	public void run() throws IOException, ClassNotFoundException, InterruptedException{
		server=new ServerSocket(4323);
		
		while(true){
			Socket cli=server.accept();
			
			
			new dataServerThread(cli).run();

		}
		

	}


}
