package com.example.first_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

//import com.example.first_app.MainActivity.RetreiveFeedTask;


@SuppressLint("NewApi")
public class Client_side implements Serializable {
	Socket socket=null;
	DataOutputStream out;
	DataInputStream in=null;
	ObjectInputStream inObj = null;
	private Scanner scanner;
	public void listenSocket() throws ClassNotFoundException{

		try{
			socket = new Socket("10.0.2.2", 4321);
			out = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));

			in = new DataInputStream(socket.getInputStream());
			//inObj = new ObjectInputStream(new  BufferedInputStream(in));

			//return send();
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: kq6py");
			System.exit(1);
		} catch  (IOException e) {
			System.err.println("sdfsdf     No I/O");
			System.exit(1);
		}
		//return null;
	}
	public  List<account> send() throws IOException, ClassNotFoundException {
		/*scanner = new Scanner (System.in);
		InetAddress ip = InetAddress.getLocalHost();
		//System.out.println("Current IP address : " + ip.getHostAddress());
		//NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		byte[] mac = network.getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}*/

		//	System.err.println(	MainActivity.bssid);
		
			String name = "1"+" cli "+"123";

			out.writeUTF(name);
			out.flush();


			int size=in.readInt();
			if(size>0){
				byte [] b=new byte[size];
				in.read(b);
				ByteArrayInputStream inb = new ByteArrayInputStream(b);
				ObjectInputStream is = new ObjectInputStream(inb);
				Object obj=is.readObject();	
				ArrayList<account>cli_vec=null;
				//inObj.read(b);
				try{
					cli_vec=(ArrayList<account>) obj;
				}
				catch(ClassCastException e){
					System.err.print("ERRRRRRRORRRR "+e.getStackTrace());
					System.exit(1);
				}
			


				return cli_vec;
			}
		
		return null;

	}
	
	public  List<Group> send2() throws IOException, ClassNotFoundException {

			int size=in.readInt();
			if(size>0){
				byte [] b=new byte[size];
				in.read(b);
				ByteArrayInputStream inb = new ByteArrayInputStream(b);
				ObjectInputStream is = new ObjectInputStream(inb);
				Object obj=is.readObject();	
				ArrayList<Group>cli_vec=null;
				//inObj.read(b);
				try{
					cli_vec=(ArrayList<Group>) obj;
				}
				catch(ClassCastException e){
					System.err.print("ERRRRRRRORRRR "+e.getStackTrace());
					System.exit(1);
				}
			


				return cli_vec;
			}
		
		return null;

	}

	public void waitread() throws IOException{
		try {
			int size=in.readInt();
			/*while((size=in.readInt())<=0){
				try{
					Thread.sleep(3000);
				}
				catch(Exception e){break;}
			}*/
			if(size>0){
				byte [] b=new byte[size];
				in.read(b);
				ByteArrayInputStream inb = new ByteArrayInputStream(b);
				ObjectInputStream is = new ObjectInputStream(inb);
				Object obj=is.readObject();	
				ArrayList<account>cli_vec=null;

				try{
					Concurency.cli_vec=(ArrayList<account>) obj;
				}
				catch(ClassCastException e){

					System.exit(1);
				}
			}
		} catch (ClassNotFoundException e) {
		
			System.exit(1);

		}
		Concurency.flag=1;
		return;

	}






}
