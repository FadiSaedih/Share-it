package com.example.first_app;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Lock;


public class peer_Server_indivisual {
	ServerSocket server; 



	public peer_Server_indivisual() throws IOException{
		DatagramSocket serverSocket = new DatagramSocket(4321);
		byte[] receiveData = new byte[1024];            
		byte[] sendData = new byte[1024]; 
		//byte[] receiveList = new byte[1024];            
		byte[] datasize = new byte[64]; 
		while(true)               
		{                
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);              
			serverSocket.receive(receivePacket);              
			String sentence = new String( receivePacket.getData());            
			if(sentence.compareTo("first_app broadcast message")==0) {       
				InetAddress IPAddress = receivePacket.getAddress();  
				
				DatagramPacket sendPacket = new DatagramPacket("data sent message".getBytes(),
						"data sent message".getBytes().length, IPAddress, 4321); 
				serverSocket.send(sendPacket); 
				
				sendData = serializeData();  
				
				sendPacket = new DatagramPacket(String.valueOf(sendData.length).getBytes(), 
						String.valueOf(sendData.length).getBytes().length, IPAddress, 4321);        
				serverSocket.send(sendPacket); 
				
				  
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4321);        
				serverSocket.send(sendPacket);   
			}
			if(sentence.compareTo("data sent message")==0){
				receivePacket = new DatagramPacket(datasize, datasize.length);              
				serverSocket.receive(receivePacket); 
				int size=Integer.parseInt(receivePacket.getData().toString());
				byte[] receiveList = new byte[size]; 
				receivePacket = new DatagramPacket(receiveList, receiveList.length);              
				serverSocket.receive(receivePacket); 

				try {
					ByteArrayInputStream inb = new ByteArrayInputStream(receiveList);
					ObjectInputStream is = new ObjectInputStream(inb);
					Object obj=is.readObject();	
					account a_vec=(account) obj;
					Concurency.addtoclivec(a_vec);
					FirstTab.adapter.notifyDataSetChanged();
				} 
				catch (Exception e) {

					e.printStackTrace();
				}

			}
		} 


	}


	byte[] serializeData() throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out2 = null;
		//if(Concurency.cli_vec!=null){
			out2 = new ObjectOutputStream(bos);   
			out2.writeObject(Concurency.User);//must only send the user id not his vec
			out2.flush();
			byte[] yourBytes = bos.toByteArray();
			out2.close();
			bos.close();
			return yourBytes;
		//}
		//return null;

	}








}
