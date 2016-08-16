package com.zhang.adapter;

import java.util.ArrayList;

import orange.zhang.app.R;

import com.zhang.util.MyAttend;
import com.zhang.util.MyStudent;
import com.zhang.util.TimeTools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StuAttendListAdapter extends BaseAdapter{
	
	Context mContext;
	ArrayList< MyStudent > attendList;
	boolean isClassAttend;
	int len;
	
	public StuAttendListAdapter(Context context, ArrayList< MyStudent > attendList) {
		this.mContext = context;
		this.attendList = attendList;
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
		timeText.setText( attendList.get(position).getStudentName() );
		flagText.setText( attendList.get(position).getFlag() == 1 ? "缺勤" : "正常");
		
		return convertView;
	}

}
