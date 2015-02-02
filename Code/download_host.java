package com.Shareitapplication.shareit;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import android.os.Environment;
import android.widget.Toast;


public class download_host{
	Socket c=null;
	DataOutputStream out;
	DataInputStream in=null;
	ObjectInputStream inObj = null;
	boolean folder=false;
	boolean privacy=false;
	public download_host(Socket cli) throws IOException{
		in =  new DataInputStream(cli.getInputStream());
		out = new DataOutputStream(
				new BufferedOutputStream(cli.getOutputStream()));
		c=cli;
		c.setSoTimeout(5000);
		run();


	}



	public void run() throws IOException {

		
		try{

			String s=in.readUTF();

			
			byte[] r=search(s);
			if(r==null)
				return;
			if(folder==true){
				out.writeUTF("Folder");
				out.flush();
			}
			else{
				out.writeUTF("file");
				out.flush();
			}
			if(privacy==true){
				out.writeUTF("private");
				out.flush();
				
				//inPrivate(r,s);
				String pass=in.readUTF();
			
				synchronized( Tracker.privateShare ) { 
					if(Tracker.privateShare.get(s).contains(pass)==false){
						out.writeUTF("You are not int this file's private list**//**");
						out.flush();
						c.close();
						return;
					}
				}

				out.writeUTF("continue inprivate");
				out.flush();
				
			}
			else{
				out.writeUTF("public");
				out.flush();
			}
			synchronized( Tracker.non_sharing ) { 
				if(Tracker.non_sharing.contains(s) /*|| Tracker.RemovedFiles.contains(s)*/){
					out.writeUTF("This File is not sharable**//**");
					out.flush();
					c.close();
					return;
				}
			}

			if(r==null || r.length==0){
				out.writeUTF("This File is not sharable**//**");
				out.flush();
				c.close();
				return;
			}
			

			out.writeUTF(String.valueOf(r.length));
			out.flush();
			out.write(r);
			out.flush();
			in.read();
			

			c.close();
			
			System.gc();
		}
		catch(Exception e){

			e.printStackTrace();
			c.close();
		}
	}







	private byte[] search(String sentence) throws IOException {
		synchronized( Tracker.privateShare ) { 
			if(Tracker.privateShare.containsKey(sentence)){
				privacy=true;
			}
		}
		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		File d=new File(dir+File.separator+Tracker.SourceFile);
		File[] listOfFiles = d.listFiles();
		if(Tracker.SourceFile.compareTo("")!=0 &&listOfFiles!=null)
			for(int i=0;i<listOfFiles.length;i++){
				if(listOfFiles[i].getName().compareTo(sentence)==0){
					if(listOfFiles[i].isFile()){
						RandomAccessFile f = new RandomAccessFile(listOfFiles[i].getAbsolutePath(), "r");
						byte[] b = new byte[(int)listOfFiles[i].length()];
						f.read(b);
						f.close();
						return b;
					}
					else{
						folder=true;
						try {
							Tracker.zipFolder(listOfFiles[i].getAbsolutePath(),Tracker.newfiles.elementAt(i).getAbsolutePath()+".zip");
							RandomAccessFile f = new RandomAccessFile(listOfFiles[i].getAbsolutePath()+".zip", "r");
							File file=new File(listOfFiles[i].getAbsolutePath()+".zip");
							byte[] b = new byte[(int)file.length()];
							f.read(b);
							f.close();

							file.delete();
							return b;
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					/*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				try{
					GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
					gzipOutputStream.write(b);
					gzipOutputStream.close();
				} catch(IOException e){
					throw new RuntimeException(e);
				}

				return byteArrayOutputStream.toByteArray(); */



				}
			}
		for(int i=0;i<Tracker.newfiles.size();i++){
			if(Tracker.newfiles.elementAt(i).getName().compareTo(sentence)==0){
				if(Tracker.newfiles.elementAt(i).isFile()){
					synchronized( Tracker.newfiles.elementAt(i) ) { 
						RandomAccessFile f = new RandomAccessFile(Tracker.newfiles.elementAt(i).getAbsolutePath(), "r");
						try{
						byte[] b = new byte[(int)Tracker.newfiles.elementAt(i).length()];
						f.read(b);
						f.close();
						return b;
						}
						catch(Exception e){
							Toast.makeText(app.appconn.getApplicationContext(),"File is too big!", Toast.LENGTH_LONG).show();
							break;
						}
					}
				}
				else{
					folder=true;
					try {
						synchronized( Tracker.newfiles.elementAt(i) ) { 
							Tracker.zipFolder(Tracker.newfiles.elementAt(i).getAbsolutePath(),Tracker.newfiles.elementAt(i).getAbsolutePath()+".zip");
							RandomAccessFile f = new RandomAccessFile(Tracker.newfiles.elementAt(i).getAbsolutePath()+".zip", "r");
							File file=new File(Tracker.newfiles.elementAt(i).getAbsolutePath()+".zip");
							byte[] b = new byte[(int)file.length()];
							f.read(b);
							f.close();

							file.delete();
							return b;
						}
					} catch (Exception e) {
						//e.printStackTrace();
					}

				}
			}
		}
		return null;
	}


}
