package com.zhang.adapter;

import java.util.ArrayList;

import orange.zhang.app.R;

import com.zhang.util.MySetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySettingListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList< MySetting > mySettingList;
	
	public MySettingListAdapter(Context context, ArrayList< MySetting > mySettingList){
		this.mContext = context;
		this.mySettingList = mySettingList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mySettingList.size();
	}

	@Override
	public MySetting getItem(int position) {
		// TODO Auto-generated method stub
		return this.mySettingList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder{
		ImageView imageView;
		TextView textView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_setting_listview, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.setting_imageview);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.setting_textview);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.imageView.setImageDrawable( mySettingList.get(position).getSrc() );
		viewHolder.textView.setText( mySettingList.get(position).getText() );
		return convertView;
	}

}
