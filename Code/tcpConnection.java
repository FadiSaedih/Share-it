package com.Shareitapplication.shareit;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Environment;



public class tcpConnection {
	Socket socket=null;
	DataInputStream in=null;
	DataOutputStream out;
	
	public  tcpConnection(String name,int pos) throws ClassNotFoundException, IOException, alertException, hostDoesNotExistEx, PrivateException{
		String ip="";
		try{
			ip=Tracker.Get_from_map(name,pos)[0];
			socket = new Socket(InetAddress.getByName(ip), 4325);
			//socket.connect(new InetSocketAddress("10.0.2.2", 4325));
			//socket.
			out = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));

			in = new DataInputStream(socket.getInputStream());

		} catch (UnknownHostException e) {
			//hostDoesNotExist(ip,name);
			//System.exit(1);
			throw new hostDoesNotExistEx();
		} catch  (IOException e) {
			throw new hostDoesNotExistEx();

		}
		
		
		out.writeUTF(name);
		out.flush();
		
		rec(name);
		if(socket.isClosed()==false)
			socket.close();
		
	}


	private void rec(String name) throws IOException, ClassNotFoundException, alertException, PrivateException {
		if(in.readUTF().compareTo("Folder")==0){
			socket.setSoTimeout(400000);
			name=name+".zip";
		}	
		else
			socket.setSoTimeout(120000);
		if(in.readUTF().compareTo("private")==0){
			
			out.writeUTF(Tracker.phoneNumber);
			out.flush();
			if(in.readUTF().compareTo("You are not int this file's private list**//**")==0){
				socket.close();
				throw new PrivateException();
			}

		
		}
		int size=0;
		String s=in.readUTF();
		try{
			size=Integer.parseInt(s);
		}
		catch(Exception e){
			/*if(s.compareTo("This File is not sharable**//**")==0){
				AlertDialog.Builder builder = new AlertDialog.Builder(connectMe.con);
				builder.setMessage("The file "+name+" is no longer shareable")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

			}
			return;*/
			socket.close();
			throw new alertException();
		}
		
		byte[] buffer = new byte[2048];
		int len,count=0;
		int check=-1,tmpsize=0;
		byte [] arr=null;
		try{
			arr=new byte[size];
		}catch(OutOfMemoryError e){
			System.gc();
			tmpsize=size/2;
			check=0;
			while(check==0){
				try{
					arr=new byte[tmpsize];
					check=1;
				}catch(OutOfMemoryError e2){
					tmpsize=tmpsize/2;
				}
			}
		}
		if(check==-1){
			while((len=in.read(buffer))>0){
				arr=copytoarr(arr,buffer,len,count);
				count=count+len;
				if(count==size)
					break;
			}
			WritetoFile(arr,name);
		}
		else{
			try{
			
				int count2=0;
				File dir = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath());
				File d=WritetoFileParts1(dir,name);
				FileOutputStream out2 = 
						new FileOutputStream(d);
				while((len=in.read(buffer))>0){
					if(count2>=tmpsize || count2+len>=tmpsize){
						out2.write(arr, 0, count2);
						out2.flush();
						arr=null;
						System.gc();
						Thread.sleep(20);
						//part++;
						count2=0;
						arr=new byte[tmpsize];
						

					}
					arr=copytoarr(arr,buffer,len,count2);
					count=count+len;
					count2=count2+len;
					if(count==size){
						out2.write(arr, 0, count2);
						out2.flush();
						break;
					}


				}
				out2.close();
				Tracker.downloaded.add(d);
				finname=d.getName();
			}
			catch(Exception e){

				e.printStackTrace();
			}
		}
		out.write(5);
		out.flush();
		
		arr=null;
		buffer=null;
		System.gc();
		
		
	}

	private byte[] copytoarr(byte[] arr, byte[] buffer, int len,int k) {

		for(int i=0;i<len;i++){
			arr[k]=buffer[i];
			k++;
		}
		return arr;
	}
	String finname="";
	private File WritetoFileParts1(File dir,String name){
		File d=null;
		
			d=new File(dir+File.separator+"downloads_Share_it"+File.separator+name);

			int i=2;
			int index=name.indexOf(".");
			int in2=index;
			while((name.substring(in2+1,name.length())).indexOf(".")>-1){

				index=(name.substring(in2+1,name.length())).indexOf(".");
				if(index==0)index++;
				in2=in2+index;
			}
			String extention=name.substring(in2,name.length());
			String n=name.substring(0,in2);
			while(d.exists()){
				//n=n+"("+String.valueOf(i)+")."+extention;
				d=new File(dir+File.separator+"downloads_Share_it"+File.separator+n+"("+String.valueOf(i)+")"+extention);
				i++;
			}
		
		return d;
	}
	

	private void WritetoFile(byte[] data,String name) throws IOException {
		
		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		File d=new File(dir+File.separator+"downloads_Share_it"+File.separator+name);
		int i=2;
		int index=name.indexOf(".");
		int in2=index;
		while((name.substring(in2+1,name.length())).indexOf(".")>-1){

			index=(name.substring(in2+1,name.length())).indexOf(".");
			if(index==0)index++;
			in2=in2+index;
		}
		String extention=name.substring(in2,name.length());
		String n=name.substring(0,in2);
		while(d.exists()){
			//n=n+"("+String.valueOf(i)+")."+extention;
			d=new File(dir+File.separator+"downloads_Share_it"+File.separator+n+"("+String.valueOf(i)+")"+extention);
			i++;
		}
		FileOutputStream out2 = 
				new FileOutputStream(d);

		out2.write(data, 0, data.length);
		out2.close();
		
		Tracker.downloaded.add(d);
		finname=d.getName();
	}



}
