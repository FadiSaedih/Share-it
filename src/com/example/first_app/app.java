package com.example.first_app;



import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
public class app extends  TabActivity  {
	//LinearLayout layout;

	/** Called when the activity is first created. */
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.app);
	         
	        TabHost tabHost = getTabHost();
	         
	        // Tab for Photos
	        TabSpec Indivisuals = tabHost.newTabSpec("Indivisuals");
	        // setting Title and Icon for the Tab
	        Indivisuals.setIndicator("Indivisuals");
	        Intent photosIntent = new Intent(this, FirstTab.class);
	        Indivisuals.setContent(photosIntent);
	         
	        // Tab for Songs
	        TabSpec Settings = tabHost.newTabSpec("Settings");
	        Settings.setIndicator("Settings");
	        Intent SettingIntent = new Intent(this, FirstTab.class);
	        Settings.setContent(SettingIntent);
	         
	        // Tab for Videos
	        TabSpec Groups = tabHost.newTabSpec("Groups");
	        Groups.setIndicator("Groups");
	        Intent Groupsintent = new Intent(this, SecondTab.class);
	        Groups.setContent(Groupsintent);
	      
	        TabSpec Connections = tabHost.newTabSpec("Connections");
	        Connections.setIndicator("Chat");
	        Intent Conn = new Intent(this, ConnectionTab.class);
	        Connections.setContent(Conn);
	         
	        tabHost.addTab(Indivisuals); // Adding videos tab
	        // Adding all TabSpec to TabHost
	        tabHost.addTab(Groups); // Adding photos tab
	       
	       
	        tabHost.addTab(Settings);
		
	        tabHost.addTab(Connections);
		
		
		
		
	}


	



}
