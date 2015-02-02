package com.Shareitapplication.shareit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class downloading {
	ServerSocket server; 
	public downloading() throws IOException{
		try{
		//server=new ServerSocket();
		}
		catch(Exception e){
			
		}
	
	}
	public void close() throws IOException{
		if(server.isClosed()==false)
			server.close();
	}
	void run() throws IOException, InterruptedException{
		server=new ServerSocket(4325);
		while(true){
			final Socket cli = server.accept();
			
			new Thread(new Runnable()
			{
				public void run()
				{
					try{
						/*download_host dh=*/new download_host(cli);
						//dh.run();
					}catch(Exception e){}
				}
			}).start();


		}

	}


}
