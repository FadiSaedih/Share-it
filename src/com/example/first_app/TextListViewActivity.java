package com.example.first_app;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TextListViewActivity extends ArrayAdapter<String>{

	Context context;
	public TextListViewActivity(Context context2, int resource,List<String> items) {
		super(context2, resource, items);
		
		this.context=context2;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		String rowItem= getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			LinearLayout layout = new LinearLayout(context);
			TextView title = new TextView(context);
			layout.addView(title);
			title.setText(rowItem);
			convertView = layout; 
		

		} 
		else{}
			//holder = (ViewHolder) convertView.getTag();


		


		return convertView;
	}
	
}