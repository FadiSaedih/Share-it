package com.example.first_app;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomListViewAdapter<T> extends ArrayAdapter<T>  {

	Context context;

	public CustomListViewAdapter(Context context, int resourceId,
			List<T> items) {
		super(context, resourceId, items);
		this.context = context;
	}
	
	

	public View getView(int position, View convertView, ViewGroup parent) {
		//ViewHolder holder = null;
		identify rowItem= (identify)getItem(position);
		if(rowItem==null)
			return null;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			//convertView = mInflater.inflate(android.R.layout.simple_list_item_activated_1, null);
			//holder = new ViewHolder();
			// holder.txtDesc =new TextView(context);
			//holder.txtTitle = new TextView(context);
			// holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
			//  convertView.setTag(holder);
			LinearLayout layout = new LinearLayout(context);
			TextView title = new TextView(context);
			TextView subtitle = new TextView(context);
			if(rowItem.image!=null)
				layout.addView(rowItem.image);
			layout.addView(title);
			layout.addView(subtitle);
			title.setText(rowItem.name);
			subtitle.setText(rowItem.mac);

			convertView = layout; 
			//holder = new ViewHolder();
			//convertView.setTag(holder);

		} else{}
			//holder = (ViewHolder) convertView.getTag();


		//holder.txtDesc.setText(rowItem.name);
		//holder.txtTitle.setText(rowItem.mac);
		// holder.imageView.seti



		return convertView;
	}
}