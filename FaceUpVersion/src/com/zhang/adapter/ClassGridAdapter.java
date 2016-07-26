package com.zhang.adapter;


import java.util.ArrayList;
import java.util.Map;

import orange.zhang.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassGridAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList< Map<String, Object> > classNameList;
	private int res;
	private LayoutInflater inflater;
	
	public ClassGridAdapter(Context context, int resourse, ArrayList< Map<String, Object> > classNameList) {
		super();
		this.mContext = context;
		this.classNameList = classNameList;
		this.res = resourse;
		this.inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return classNameList.size()+1;
	}

	@Override
	public Map<String, Object> getItem(int position) {
		// TODO Auto-generated method stub
		return classNameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	class ViewHolder{
		public ImageView imageView ; 
		public TextView textView;
	}
	
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null ; 
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(res, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_class_gridview_imageView);
			holder.textView = (TextView) convertView.findViewById(R.id.item_class_gridview_textView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position == getCount()-1){
			//设置成添加按钮
			holder.textView.setText("");
			holder.imageView.setImageResource( R.drawable.t_class_select_add );
		}else{
			holder.textView.setText( classNameList.get(position).get("className").toString() );
			holder.imageView.setBackgroundColor( (Integer) classNameList.get(position).get("classColor") );
		}
		
		return convertView;
	}

}
