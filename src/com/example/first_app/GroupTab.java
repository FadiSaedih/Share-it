package com.example.first_app;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
@SuppressLint("NewApi")
public class GroupTab extends Activity implements OnItemClickListener{

	ListView listView;
	List<account> rowItems;
	CustomListViewAdapter<account> adapter=null;
	RelativeLayout rl=null;
	public static String connName="";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Concurency.currentContext=this;
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		rl=new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size.x-170, 80);
		params.leftMargin = 20;
		params.topMargin = 0;
		EditText ed=new EditText(this);
		ed.setText("Enter Password");
		rl.addView(ed, params);

		params = new RelativeLayout.LayoutParams(160, 80);
		params.leftMargin = size.x-160;
		params.topMargin = 0;
		Button button=new Button(this);
		button.setText("Search");
		rl.addView(button, params);

		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 2);
		params.leftMargin =0;
		params.topMargin = 82;
		View v = new View(this);
		v.setLayoutParams(params);
		v.setBackgroundColor(Color.BLACK);
		rl.addView(v, params);

		listView = new ListView(this);
		listView.setClickable(true);
		listView.setDivider(new ColorDrawable(Color.BLACK));
		listView.setDividerHeight(1);
		listView.setOnItemClickListener(this);
		

		  adapter = new CustomListViewAdapter<account>(this,
				android.R.layout.simple_list_item_1, Concurency.Group_tab_vec);
		
			listView.setAdapter(adapter);
		
		
		RelativeLayout.LayoutParams viewLp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		viewLp.leftMargin =0;
		viewLp.topMargin = 85;
		
		rl.addView(listView, viewLp);
		this.setContentView(rl);
		
		
		
		
		/* Thread t2 = new Thread(new Runnable()
	        {
	            public void run()
	            {
	            	//while(Concurency.flag==0);
	            	try {
	    				MainActivity.cs.waitread();
	    				
	    			} catch (Exception e) {
	    				
	    				e.printStackTrace();
	    			}
	            	//onResume();
	            	Concurency.flag=0;
	            	 runOnUiThread(new Runnable() {  
	                     @Override
	                     public void run() {
	                         // TODO Auto-generated method stub
	                    	 adapterch();
	                    	 //adapter.notifyDataSetChanged();
	                     }
	                 });

	            	
	            	
	            	//adapter.notifyDataSetChanged();
	            
	       		 	//startActivity(getIntent());
	            	
	            }
	            
	        }
		 );
		// t2.start();*/
		 
		
	}
	void adapterch() {
	
		 adapter = new CustomListViewAdapter<account>(this,
				android.R.layout.simple_list_item_1, Concurency.cli_vec);
		listView.setAdapter(adapter);
		
		

		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,final int position, long id) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to establish connection")
		.setCancelable(false)
		.setNegativeButton("No",  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//do things
			}
		})
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//Intent intentMain = new Intent(app.this , InChat.class);
				// app.this.startActivity(intentMain);
				Concurency.connName=Concurency.Group_tab_vec.get(position).mac;
				startActivity(new Intent(view.getContext(), InChat.class));

			}
		});
		AlertDialog alert = builder.create();
		alert.show();


	}
	
	 

}


