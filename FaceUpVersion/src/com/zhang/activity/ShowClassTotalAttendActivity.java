package com.zhang.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import orange.zhang.app.R;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zhang.adapter.AttendListAdapter;
import com.zhang.service.AttendService;
import com.zhang.util.BaseActivity;
import com.zhang.util.MyAttend;
import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyShowToast;
import com.zhang.util.Parameter;
import com.zhang.view.TitleBarView;

public class ShowClassTotalAttendActivity extends BaseActivity implements OnClickListener{

	private Date startDate;
	private Date endDate;
	private MyClass selectedClass = null;
	
	private TitleBarView actionBar;
	private ListView attendListView ; 
	private AttendListAdapter attendListAdapter;
	
	private ArrayList<MyAttend> myAttendList = new ArrayList<MyAttend>();

	Intent intent;//上一级Activity传过来的团队信息
	
	private final static int FindStudent = 123;
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//
			if(msg.what == FindStudent){
				//给list列表设置适配器，
				attendListAdapter = new AttendListAdapter(ShowClassTotalAttendActivity.this, myAttendList, true);
				attendListView.setAdapter(attendListAdapter);
			}
		}
		
	};
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_show_class_total_attend);
		MyDeBug.L("------ShowClassTotalAttendActivity------");
		
		intent = getIntent();
		startDate = (Date) intent.getSerializableExtra("settingStartDate");
		endDate = (Date) intent.getSerializableExtra("settingEndDate");
		selectedClass = (MyClass) intent.getSerializableExtra("selectedClass");
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		actionBar = (TitleBarView) findViewById(R.id.select_attend_actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		actionBar.setBtnLeft(R.drawable.boss_unipay_icon_back, "返回");
		
		attendListView = (ListView) findViewById(R.id.show_attend_list);
		new FindclassAttendThread().start();
		
	}

	public class FindclassAttendThread extends Thread{
		
		@Override
		public void run() {
			//用来联网查找此团队下对应的所有人员。
			MyDeBug.L("------findAllStuAttendThread------");
			List<Parameter> params = new ArrayList<Parameter>();
			params.add(  new Parameter("classID", Integer.valueOf( selectedClass.getClassID() ).toString() ) );
			params.add(  new Parameter("startDate", Long.valueOf( startDate.getTime() ).toString() ) );
			params.add(  new Parameter("endDate", Long.valueOf( endDate.getTime() ).toString() ) );
			
			myAttendList = (ArrayList<MyAttend>) AttendService.instance().findclassAttend(params);
			
			if(myAttendList!=null){
				MyDeBug.L("findAllStuAttendThread-result-not-null:");
			}else{
				MyDeBug.L("findAllStuAttendThread-result-Null:");
			}
			
			Message message = handler.obtainMessage();
			message.obj = myAttendList;
			message.what = FindStudent;
			handler.sendMessage(message);
		}
		
	}
	
	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		actionBar.setBtnLeftOnclickListener(this);
		actionBar.setBtnRightOnclickListener(this);
		attendListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				MyDeBug.L("zhang");
				MyDeBug.L("classID -- "+selectedClass.getClassID());
				MyDeBug.L("time -- "+ myAttendList.get(position).getTime());
				Intent intent = new Intent();
				intent.putExtra("classID", selectedClass.getClassID());
				intent.putExtra("time", myAttendList.get(position).getTime());
				intent.setClass(ShowClassTotalAttendActivity.this, ShowClassSomeTimeAttendActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == actionBar.getBtnLeft()){
			//返回
			this.finish();
		}
	}
	
}