package com.example.first_app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class peer_chat {

	public static void chat(String message,InetAddress ip) throws IOException{
		DatagramSocket socket = new DatagramSocket(4324);
		socket.setBroadcast(true);
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length,
				ip, 4324);
		socket.send(packet);
	}
	
	public static void decline(String message,InetAddress ip) throws IOException{
		DatagramSocket socket = new DatagramSocket(4324);
		socket.setBroadcast(true);
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length,
				ip, 4324);
		socket.send(packet);
	}


}
