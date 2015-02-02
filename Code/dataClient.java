package com.Shareitapplication.shareit;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import android.annotation.SuppressLint;

public class dataClient {
	Socket socket=null;
	DataInputStream in=null;
	DataOutputStream out;
	public  dataClient(String name) throws ClassNotFoundException, IOException{

		try{
			socket = new Socket(name, 4323);
			
			out = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));

			in = new DataInputStream(socket.getInputStream());

		} catch (Exception e) {
			return;


		} 
		if(Tracker.ips.contains(name)==false && Tracker.my_ip.compareTo(name)!=0){
			Tracker.ips.add(name);
		}

		byte [] by=serializeData();
		out.writeUTF(String.valueOf(by.length));
		out.flush();
		out.write(by);
		out.flush();
		in.read();
		out.writeUTF(Tracker.id);
		out.flush();
		socket.close();
		by=null;
		System.gc();
	


	}

	@SuppressLint("SdCardPath")
	byte[] serializeData() throws IOException{
		try{
			//File[] listOfFiles2 =null;
			FilesAndFolders faf=new FilesAndFolders();
		
			if(Tracker.filevec!=null ){
				Vector<Vector<ChatServerdata>> vec=new Vector<Vector<ChatServerdata>>();
				Vector<Integer> FileSizes=new Vector<Integer>();
				File [] arr=new File[Tracker.filevec.size()];
				for(int i=0;i<Tracker.filevec.size();i++){
					arr[i]=Tracker.filevec.elementAt(i);
					if(Tracker.myfiles_to_comments.get(arr[i].getName())==null)
						Tracker.myfiles_to_comments.put(arr[i].getName(), new Vector<ChatServerdata>());
						vec.add(Tracker.myfiles_to_comments.get(Tracker.filevec.elementAt(i).getName()));
						FileSizes.add((int)arr[i].length());
				}
				faf=new FilesAndFolders(arr,vec,FileSizes);
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out2 = null;
			try{
				out2 = new ObjectOutputStream(bos);   
				out2.writeObject(faf);
				out2.flush();
				byte[] yourBytes = bos.toByteArray();

				out2.close();
				bos.close();
				return yourBytes;

			}
			catch(Exception e){
				e.toString();
			}
		}
		catch(Exception e){
			e.toString();
		}


		return null;

	}
}
