package com.zhang.adapter;

import java.util.ArrayList;

import orange.zhang.app.R;

import com.zhang.util.MyAttend;
import com.zhang.util.TimeTools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AttendListAdapter extends BaseAdapter{
	
	Context mContext;
	ArrayList< MyAttend > attendList;
	boolean isClassAttend;
	int len;
	
	public AttendListAdapter(Context context, ArrayList< MyAttend > attendList, boolean isClass) {
		this.mContext = context;
		this.attendList = attendList;
		this.isClassAttend = isClass;
		this.len = attendList.size();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return attendList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return attendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(mContext).inflate(R.layout.item_showattend_listview, null);
		TextView timeText = (TextView) convertView.findViewById(R.id.showAttend_item_time);
		TextView flagText = (TextView) convertView.findViewById(R.id.showAttend_item_flag);
		
		position = this.len - 1 - position;
		timeText.setText( TimeTools.timestampToString(attendList.get(position).getTime()) );
		if(this.isClassAttend){
			flagText.setText( "缺勤" + attendList.get(position).getTotalNum() + "人" );
		}else{
			flagText.setText( attendList.get(position).isFlag() ? "缺勤" : "正常");
		}
		
		return convertView;
	}

}
