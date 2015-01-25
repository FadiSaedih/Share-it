package com.example.first_app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class InChat extends Activity {

	List<String> rowItems=null;
	TextListViewActivity adapter;
	public String connName=null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    rowItems = new ArrayList<String>();
	    Concurency.currentContext=this;
	    connName=Concurency.connName;
	    Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		RelativeLayout rl=new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size.x-50,180);
		params.leftMargin = 16;
		params.topMargin = 0;
		final EditText ed=new EditText(this);
		ed.setText("Enter Password");
		rl.addView(ed, params);
		
		params = new RelativeLayout.LayoutParams(160, 80);
		params.leftMargin = (size.x/2)-70;
		params.topMargin = 185;
		Button button=new Button(this);
	    button.setText("Send");
	    rl.addView(button, params);
	    
	    params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.leftMargin = 0;
		params.topMargin = 275;
		final ListView listView = new ListView(this);
		listView.setDivider(new ColorDrawable(Color.BLACK));
		listView.setDividerHeight(1);
		listView.setClickable(false);
		rl.addView(listView, params);
		
		Bundle extras = getIntent().getExtras(); 
		if(extras !=null) {
			connName = extras.getString("connName");
		}
		
		  if(connName!=null){
			 
			  if(ChatVector.getFromVec(connName)!=null)
			  {
			    rowItems=ChatVector.getFromVec(connName);
				adapter = new TextListViewActivity(this,
							android.R.layout.simple_list_item_1, rowItems);
				listView.setAdapter(adapter);
				Concurency.addChat(connName, adapter);
			  }
		    }
		
	    button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				rowItems.add(ed.getText().toString());
				//adapter.clear();
				/*try {
				 * String message=connName+"<<>>"+ed.getText().toString();
					peer_chat.chat(ed.getText().toString(), null);
				} catch (IOException e) {
					
					e.printStackTrace();
				}*/
				adapter = new TextListViewActivity(v.getContext(),
							android.R.layout.simple_list_item_1, rowItems);
				listView.setAdapter(adapter);
				Concurency.addChat(connName, adapter);
				
			}
		});
	   
	    
	   
		
	    
		
		this.setContentView(rl);
	}
	
	public void SetAdapter(List<String> a){
		rowItems=a;
		
	}
	
	@Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        ChatVector.addtovec(connName, rowItems);
    }

}
