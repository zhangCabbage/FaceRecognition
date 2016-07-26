package com.zhang.adapter;

import java.util.ArrayList;
import java.util.List;

import orange.zhang.app.R;

import com.zhang.util.MyStudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class StudentListAdapter extends BaseAdapter{
	
	Context mContext;
	ArrayList< MyStudent > studentList;
	ArrayList<MyStudent> mySelectedStudentList = new ArrayList<MyStudent>();
	
	public StudentListAdapter(Context context, ArrayList< MyStudent > studentList) {
		this.mContext = context;
		this.studentList = studentList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return studentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(mContext).inflate(R.layout.item_classadd_listview, null);
		TextView textView = (TextView) convertView.findViewById(R.id.classList_item_className);
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.classList_item_radio_select);
		
		textView.setText( studentList.get(position).getStudentName() );
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					mySelectedStudentList.add( studentList.get(position) );
				}else{
					mySelectedStudentList.remove( studentList.get(position) );
				}
			}
		});
		return convertView;
	}
	
	public ArrayList< MyStudent > getSelectedStudent(){
		return mySelectedStudentList;
	}

}
