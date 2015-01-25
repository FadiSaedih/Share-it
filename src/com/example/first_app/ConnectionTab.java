package com.example.first_app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class ConnectionTab extends Activity implements OnItemClickListener{
	
	GridView gridView =null;
	List<String> rowItems=null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Concurency.currentContext=this;
		setContentView(R.layout.connectiontab);
		gridView = (GridView) findViewById(R.id.gridView1);
		gridView.setOnItemClickListener(this);
		gridView.setClickable(true);
		gridView.setColumnWidth(170);
		rowItems=ChatVector.getall();
		if(rowItems!=null){
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, rowItems);

			gridView.setAdapter(adapter);
		}


	}
	@Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
		rowItems=ChatVector.getall();
		if(rowItems!=null){
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, rowItems);

			gridView.setAdapter(adapter);
		}
    }
	
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,final int position, long id) {
		
		Intent resultData = new Intent(view.getContext(), InChat.class);
		resultData.putExtra("connName", rowItems.get(position));
		startActivity(resultData);
		 
	}

}
