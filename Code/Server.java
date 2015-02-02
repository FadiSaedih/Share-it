package com.Shareitapplication.shareit;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
	
	DatagramSocket serverSocket=null;
	DatagramSocket  serverSocketdata=null;
	String sentence="";
	public void close(){
		serverSocket.close();
		
	}
	public Server(){

	}
	public void run() throws IOException, InterruptedException{
		try{
			serverSocket = new DatagramSocket(4322);
		}
		catch(Exception e){

		}
		
		byte[] receiveData = new byte[256];            
		//String myip=Client.getIPAddress(true);
		while(true)               
		{                
			final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);              
			serverSocket.receive(receivePacket);              
			sentence = new String(receivePacket.getData()); 
			sentence =sentence.trim();
			if(sentence.compareTo(Tracker.my_ip)==0){
				receiveData = new byte[256];
				continue;
			}
			new Thread(new Runnable()
			{
				public void run()
				{
					try {
						new dataClient(sentence);
					} catch (ClassNotFoundException e) {

						e.printStackTrace();
					} catch (IOException e) {

						e.printStackTrace();
					} 
				

				}
			}).start();
			
			receiveData = new byte[256];
		} 


	}


}
