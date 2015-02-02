package com.Shareitapplication.shareit;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import org.apache.http.conn.util.InetAddressUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
@SuppressLint("DefaultLocale")
@SuppressWarnings("deprecation")
public class Client {
	String data="first_app broadcast message";
	Context mContext=null;

	public  Client(){
		try{
			if(MainActivity.context!=null)
				this.mContext=MainActivity.context;
			else
				this.mContext=app.appconn;

		}
		catch(Exception e){
			this.mContext=app.appconn;
		}
	}



	public  void broadCasting() throws IOException{
		DatagramSocket socket = new DatagramSocket();
		socket.setBroadcast(true);

		String i=getIPAddress(true);
		String addr = getBroadcastAddress().toString();

		if(addr.contains("/"))
			addr=addr.substring(1, addr.length());

		if(Tracker.ipList.size()>0){
			for (int j = 0; j < Tracker.ipList.size(); j++){
				try{
					if(Tracker.ipList.get(j).compareTo(i)==0)
						continue;
					InetAddress pingAddr = InetAddress.getByName(Tracker.ipList.get(j));

					DatagramPacket packet = new DatagramPacket(i.getBytes(), i.getBytes().length,
							pingAddr/*InetAddress.getByName("10.0.2.2")*/, 4322);

					socket.send(packet);
				}
				catch(Exception e){
					//return;
				}
			}
		}


	}



	public  InetAddress getBroadcastAddress() throws IOException {
		WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	} 

	@SuppressLint("DefaultLocale")
	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
						if (useIPv4) {
							if (isIPv4) 
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 port suffix
								return delim<0 ? sAddr : sAddr.substring(0, delim);
							}
						}
					}
				}
			}
		} catch (Exception ex) { } // for now eat exceptions
		return "";
	}

	public void parseIp() throws IOException{
		String ip1=getBroadcastAddress().toString();
		String ip2=getIPAddress(true);

		if(ip1.contains("/"))
			ip1=ip1.substring(1, ip1.length());
		String str="",str2="",tmp=ip1;
		int count=0;
		for(int i=0;i<=3;i++){
			if(i<3){
				str=ip1.substring(0,ip1.indexOf("."));
				str2=ip2.substring(0,ip2.indexOf("."));
				count=count+ip1.indexOf(".");
			}
			else{
				//count++;
				str=ip1;
				str2=ip2;
			}
			if(str.compareTo(str2)!=0){
				int num=0;
				if(i==3){
					count=tmp.lastIndexOf(".");
					tmp=tmp.substring(0,count);
					num=Integer.parseInt(str);
					ipList(tmp,num,i);
					return;
				}
				tmp=tmp.substring(0,count);
				num=Integer.parseInt(str);

				for(int j=0;j<num;j++)
					ipList(tmp,j,i-1);

				return;
			}

			ip1=ip1.substring(ip1.indexOf(".")+1);
			ip2=ip2.substring(ip2.indexOf(".")+1);
		}
	}

	static void ipList(String ip,int num,int num2){
		if(num2==1 || num2==2 || num2==3)
			ip=ip+".";
		if(num2!=3)
			ip=ip+String.valueOf(num)+".";


		for(int i=0;i<255;i++){

			if(num2==1 || num2==2 || num2==3)
				Tracker.ipList.add(ip+String.valueOf(i));

			if(num2==0){
				try{
					for(int j=0;j<255;j++)
						Tracker.ipList.add(ip+String.valueOf(i)+"."+String.valueOf(j));
				}
				catch(Exception e){
					return;
				}
			}

		}
	}





}
