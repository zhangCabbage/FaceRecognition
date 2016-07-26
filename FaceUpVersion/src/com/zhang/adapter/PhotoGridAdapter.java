package com.zhang.adapter;

import java.util.List;

import orange.zhang.app.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class PhotoGridAdapter extends ArrayAdapter<Bitmap>{

	private Context mContext;
	private int res;
	private List<Bitmap> bitmapList;
	private LayoutInflater inflater;
	
	public PhotoGridAdapter(Context context, int resource, List<Bitmap> bitmapList) {
		super(context, resource, bitmapList);
		this.mContext = context;
		this.res = resource;
		this.bitmapList = bitmapList;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return bitmapList.size()+1;
	}

	@Override
	public Bitmap getItem(int position) {
		return bitmapList.get(position);
	}

	private class ViewHolder{
		ImageView imageView ; 
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//这里主要是对GridView中的每项进行初始化，返回View类
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(res, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_gridview_imageView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//
		if(position == getCount()-1){
			//设置成添加按钮
			viewHolder.imageView.setImageResource( R.drawable.t_class_select_add );
		}else{
			viewHolder.imageView.setImageBitmap( bitmapList.get(position) );
		}
		return convertView;
	}


}
