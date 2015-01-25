package com.example.first_app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends Activity /*implements OnClickListener*/ {
	
	public static Client_side cs;
	//public static List<account> cli_vec=null;
	public static String bssid ;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		cs=new Client_side();
		setContentView(R.layout.activity_main);
		//LinearLayout lnr = new LinearLayout(this);
		final RelativeLayout rl=new RelativeLayout(this);
		Button button=new Button(this);
	    button.setText("Sign In");
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(160, 80);
		params.leftMargin = 270;
		params.topMargin = 450;
		rl.addView(button, params);
		
		EditText ed=new EditText(this);
		ed.setText("Enter Password");
		params = new RelativeLayout.LayoutParams(360, 80);
		params.leftMargin = 170;
		params.topMargin = 350;
		rl.addView(ed, params);
		
		EditText ed2=new EditText(this);
		ed2.setText("Enter Name");
		params = new RelativeLayout.LayoutParams(360, 80);
		params.leftMargin = 170;
		params.topMargin = 250;
		rl.addView(ed2, params);
		
		//final Button button = (Button) findViewById(R.id.button1);
		//final EditText Name=(EditText) findViewById(R.id.Name);
		//final EditText password=(EditText) findViewById(R.id.Pass);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			//Concurency.User=new account(ed.getText().toString(),ed2.getText().toString());
				  
			//new RetreiveFeedTask().execute();
			//RetreiveFeedTask t=new RetreiveFeedTask();
			//t.execute();
				/*Thread t = new Thread(new Runnable()
		        {
		            public void run()
		            {
		            	try {
		    				cs.listenSocket();
		    				Concurency.cli_vec=cs.send();
		    				Concurency.group_vec=cs.send2();
		    				System.out.println(Concurency.cli_vec.get(0).name);
		    			} catch (Exception e) {
		    				
		    				e.printStackTrace();
		    			}
		    	    	
		            }
		        });
				
				

		        // System.out.println("STARTING: " + t);
		        t.start();
		        try {
					t.join();
				} catch (InterruptedException e) {
					
				
				}
		       */
				Intent intentMain = new Intent(MainActivity.this , 
					 app.class);
				 MainActivity.this.startActivity(intentMain);
				 //Log.i("Content "," Main layout ");
				
			}
		});
		
		new Thread(new Runnable()
        {
            public void run()
            {
            	try {
            		new peer_Server_indivisual();
    			
    			} catch (Exception e) {
    				
    				e.printStackTrace();
    			}
    	    	
            }
        }).start();
		new Thread(new Runnable()
        {
            public void run()
            {
            	try {
            		new peer_server_group();
    				
    			} catch (Exception e) {
    				
    				e.printStackTrace();
    			}
    	    	
            }
        }).start();
		
		
		peer_Client_indivisuals cli=new peer_Client_indivisuals(this);
		peer_Client_Group group=new peer_Client_Group(this);
		try {
			Thread.sleep(500);
			cli.broadCasting();
			group.broadCasting();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		 
		this.setContentView(rl);
		
       

	}
	
	/*class RetreiveFeedTask extends AsyncTask<String, Void,List<account>> {

	    private Exception exception;

	    protected List<account> doInBackground(String... urls) {
	       /* try {
	        	// cli_vec=cs.listenSocket();
	        	cs.listenSocket();
	        	/* WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	        	// wifiManager.
				 WifiInfo wifiInfo = wifiManager.getConnectionInfo();*/
				// bssid = wifiInfo.getBSSID();
	        	 //cli_vec=cs.send();
	        	
	       /* } catch (Exception e) {
	            this.exception = e;
	            System.exit(1);
	        }*/
	    /*	try {
				cs.listenSocket();
				cli_vec=cs.send();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	    	
			return cli_vec;
	    }
	    @Override
        public void onPreExecute() {
	    	/*try {
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}*/
	    	
       /* }

     

        public Void onPostExecute(Void... params) throws IOException, ClassNotFoundException {
        	
            return null;


        }

	  
	}*/
	



}
