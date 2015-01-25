package com.example.first_app;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class SecondTab extends Activity/*ListActivity*/   implements OnItemClickListener,Serializable {

	public static final String[] titles = new String[] { "Strawberry",
		"Banana", "Orange", "Mixed" };

	public static final String[] descriptions = new String[] {
		"It is an aggregate accessory fruit",
		"It is the largest herbaceous flowering plant", "Citrus Fruit",
	"Mixed Fruits" };



	ListView listView;
	List<account> rowItems;
	

	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Concurency.currentContext=this;
		Display display = getWindowManager().getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);

		final RelativeLayout rl=new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size.x-170, 80);
		params.leftMargin = 20;
		params.topMargin = 0;
		EditText ed=new EditText(this);
		ed.setText("Enter Password");
		//this.addContentView(ed, params);
		rl.addView(ed, params);

		params = new RelativeLayout.LayoutParams(160, 80);
		params.leftMargin = size.x-160;
		params.topMargin = 0;
		Button button=new Button(this);
		button.setText("Search");
		rl.addView(button, params);
		final ImageView imageView = new ImageView(this);
		File filePath = getFileStreamPath("");
		
		imageView.setImageDrawable(Drawable.createFromPath(filePath.toString()));
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Concurency.cli_vec.add(new account("a","b"));
				FirstTab.adapter.notifyDataSetChanged();
				
			}
		
			});

		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 2);
		params.leftMargin =0;
		params.topMargin = 82;
		View v = new View(this);
		v.setLayoutParams(params);
		v.setBackgroundColor(Color.BLACK);
		rl.addView(v, params);



		listView = new ListView(this);
		//listView.setBackgroundColor(Color.BLACK);
		listView.setDivider(new ColorDrawable(Color.BLACK));
		listView.setDividerHeight(1);
		CustomListViewAdapter<Group> adapter=null;
		
		adapter = new CustomListViewAdapter<Group>(this,
				android.R.layout.simple_list_item_1, Concurency.group_vec);

		listView.setAdapter(adapter);
		
		RelativeLayout.LayoutParams viewLp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		viewLp.leftMargin =0;
		viewLp.topMargin = 85;
		//this.addContentView(listView, viewLp);
		rl.addView(listView, viewLp);
		this.setContentView(rl);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener 
				(){ 
			@Override 
			public boolean onItemLongClick(AdapterView<?> av, final View v, final int 
					pos, long id) { 
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setMessage("Do you want to see users in group")
				.setCancelable(false)
				.setNegativeButton("No",  new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//do things
					}
				})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						RelativeLayout rl2=new RelativeLayout(v.getContext());
						RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(size.x-270, size.y-700);
						params2.leftMargin =size.x/5;
						params2.topMargin = size.y/8;
						Concurency.Concurencylayout=rl2;
						Concurency.Group_tab_vec=Concurency.group_vec.get(pos).grouplist;
						//rl2.setBackgroundColor(Color.WHITE);
						rl.addView(rl2,params2);
						Intent intent = new Intent(v.getContext(), GroupTab.class);
						//intent.putExtra("xsize",size.x-270);
						//intent.putExtra("ysize",size.y-700);
						startActivity(intent);
						
						
						
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				return false; 
			} 
		}); 

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {



		/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to establish connection")
		.setCancelable(false)
		.setNegativeButton("No",  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//do things
			}
		})
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//do things
			}
		});
		AlertDialog alert = builder.create();
		alert.show();*/
		


	}


}
