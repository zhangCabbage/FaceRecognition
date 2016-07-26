package com.zhang.adapter;

import java.util.ArrayList;

import orange.zhang.app.R;

import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ClassListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList< MyClass > classList = null;//表示所有的MyClass类
	private ArrayList< MyClass > mySelect_ClassList = new ArrayList<MyClass>();//表示被选中的MyClass类
	
	public ClassListAdapter(Context context, ArrayList< MyClass > classList) {
		super();
		mContext = context;
		this.classList = classList;
	}

	@Override
	public int getCount() {
		return classList.size();
	}

	@Override
	public MyClass getItem(int position) {
		return classList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder{
		public TextView textView;
		public CheckBox checkBox;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null ; 
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_classadd_listview, null);
			holder.textView = (TextView) convertView.findViewById(R.id.classList_item_className);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.classList_item_radio_select);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textView.setText(classList.get(position).getClassName() + "(" + classList.get(position).getClassStuNumber() + "人)");
		if( classList.get(position).isSelected() ){
			mySelect_ClassList.add(classList.get(position));
			holder.checkBox.setChecked(true);
		}
		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mySelect_ClassList.add(classList.get(position));
				}else{
					mySelect_ClassList.remove(classList.get(position));
				}
//				System.out.println("memberList:" + myclassList.toString());
			}
		});
		return convertView;
	}
	
	public ArrayList< MyClass > getSelectClassList(){
		for(int i=0; i<mySelect_ClassList.size(); i++){
			mySelect_ClassList.get(i).setSelected(true);
			MyDeBug.L( "这里是ClassListAdapter" + mySelect_ClassList.get(i).toString() );
		}
		return mySelect_ClassList;
	}
}
