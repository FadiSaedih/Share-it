package com.example.first_app;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class peer_Chat_Server {

	ServerSocket server; 
	String sentence = "";
	int index=0;
	int index2=0;
	DatagramPacket receivePacket = null;


	public peer_Chat_Server() throws IOException{
		DatagramSocket serverSocket = new DatagramSocket(4324);
		byte[] receiveData = new byte[1024];            

		while(true)               
		{                
			receivePacket = new DatagramPacket(receiveData, receiveData.length);              
			serverSocket.receive(receivePacket);              
			sentence = new String(receivePacket.getData()).trim(); 
			index=sentence.indexOf("<<>>");
			if(index<0){
				if(sentence.indexOf("<><>")>0){
					AlertDialog.Builder builder = new AlertDialog.Builder(Concurency.currentContext);
					builder.setMessage("Connection Declined From "+sentence.split("<><>")[0])
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
					
				}
			
			
			}
			String name=sentence.subSequence(0,index-1).toString();
			String message=sentence.subSequence(index+4,sentence.length()).toString();
			if(ChatVector.chvec.containsKey(name)){
				ChatVector.getFromVec(name).add(message);
				Concurency.chatMap.get(name).notifyDataSetChanged();
			}
			else{//new connection
				
				sentence=sentence.subSequence(index+4,sentence.length()).toString();
				final String name2=sentence.subSequence(0,index-1).toString();
				if(name.compareTo("new Connection")==0){
					AlertDialog.Builder builder = new AlertDialog.Builder(Concurency.currentContext);
					builder.setMessage("Connection request from "+name)
					.setCancelable(false)
					.setNegativeButton("Decline",  new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							try {
								peer_chat.decline(Concurency.User.name+"<><>Decline",receivePacket.getAddress());
							} catch (IOException e) {
								
								e.printStackTrace();
							}
						}
					})
					.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							String message2=sentence.subSequence(index+4,sentence.length()).toString();
							ChatVector.addtovec(name2,new ArrayList<String>());
							if(message2.length()>1)
							{
								ChatVector.getFromVec(name2).add(message2);
							}
							
						}
					});
					AlertDialog alert = builder.create();
					alert.show();

					
				}
				
			}

			receiveData = new byte[1024];

		} 
	}


}
