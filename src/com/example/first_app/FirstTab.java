package com.example.first_app;


import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class FirstTab extends Activity/*ListActivity*/  implements OnItemClickListener {


	public  final String[] titles = new String[] { "Strawberry",
			"Banana", "Orange", "Mixed" };

	public  final String[] descriptions = new String[] {
			"It is an aggregate accessory fruit",
			"It is the largest herbaceous flowering plant", "Citrus Fruit",
	"Mixed Fruits" };



	ListView listView;
	List<account> rowItems;
	public static CustomListViewAdapter<account> adapter=null;
	RelativeLayout rl=null;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Concurency.currentContext=this;
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		//Bundle b = this.getIntent().getExtras();
		//if(b!=null)

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
		//listView.setBackgroundColor(Color.BLACK);
		listView.setDivider(new ColorDrawable(Color.BLACK));
		listView.setDividerHeight(1);

		//CustomListViewAdapter adapter = new CustomListViewAdapter(this,
		//android.R.layout.simple_list_item_1, rowItems);

		//if(Concurency.cli_vec!=null){
			adapter = new CustomListViewAdapter<account>(this,
					android.R.layout.simple_list_item_1, Concurency.cli_vec);
			listView.setAdapter(adapter);
			//}
		
		RelativeLayout.LayoutParams viewLp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		viewLp.leftMargin =0;
		viewLp.topMargin = 85;
		//this.addContentView(listView, viewLp);
		rl.addView(listView, viewLp);
		this.setContentView(rl);
		listView.setOnItemClickListener(this);


	}
	public void adapterch() {
		Concurency.cli_vec.add(new account("a","b"));
		adapter = new CustomListViewAdapter<account>(this,
				android.R.layout.simple_list_item_1, Concurency.cli_vec);
		listView.setAdapter(adapter);




	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view,final int position, long id) {
		/*if(position==0){
			RelativeLayout.LayoutParams viewLp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 85);
			viewLp.leftMargin = 0;
			viewLp.topMargin =600;
			Button titleView = new Button(this);
			titleView.setBackgroundColor(Color.BLACK);
			titleView.setText("fgh");
			titleView.setTextColor(Color.WHITE);
			titleView.setTextSize(10);
			this.addContentView(titleView, viewLp);
		}*/
		//rowItems.get(position);
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
				Concurency.connName=Concurency.cli_vec.get(position).mac;
				startActivity(new Intent(view.getContext(), InChat.class));

			}
		});
		AlertDialog alert = builder.create();
		alert.show();


	}


	@Override
	protected void onResume() {
		super.onResume();
		
		// The activity has become visible (it is now "resumed").
		
		adapter = new CustomListViewAdapter<account>(this,
				android.R.layout.simple_list_item_1, Concurency.cli_vec);
		listView.setAdapter(adapter);
		




	}

}
