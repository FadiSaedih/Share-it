package com.example.first_app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

@SuppressWarnings("deprecation")
public class peer_Client_Group{
	String data="first_app broadcast message";
	Context mContext=null;
	
	public  peer_Client_Group(Context mContext){
		this.mContext=mContext;
	}
	
	public void broadCasting() throws IOException{
		DatagramSocket socket = new DatagramSocket(4322);
		socket.setBroadcast(true);
		DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),
		    getBroadcastAddress(), 4322);
		socket.send(packet);
		
	}
	
	public void broadCastingToGroup() throws IOException{
		DatagramSocket socket = new DatagramSocket(4322);
		socket.setBroadcast(true);
		DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),
		    getBroadcastAddress(), 4322);
		socket.send(packet);
		
	}
	
	InetAddress getBroadcastAddress() throws IOException {
	    WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	   
		DhcpInfo dhcp = wifi.getDhcpInfo();
	    // handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
	    byte[] quads = new byte[4];
	    for (int k = 0; k < 4; k++)
	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	    return InetAddress.getByAddress(quads);
	}  

	

}
