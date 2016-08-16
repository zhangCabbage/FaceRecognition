package com.zhang.activity;

import java.util.ArrayList;
import java.util.List;

import orange.zhang.app.R;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.zhang.adapter.StuAttendListAdapter;
import com.zhang.service.AttendService;
import com.zhang.util.BaseActivity;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyStudent;
import com.zhang.util.Parameter;
import com.zhang.view.TitleBarView;

public class ShowClassSomeTimeAttendActivity extends BaseActivity implements OnClickListener{

	private TitleBarView actionBar;
	private ListView attendListView ; 
	private StuAttendListAdapter attendListAdapter;
	
	private ArrayList<MyStudent> myAttendList = new ArrayList<MyStudent>();

	Intent intent;//上一级Activity传过来的团队信息
	int classID = 0;
	long time = 0;
	
	private final static int FindStudent = 123;
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//
			if(msg.what == FindStudent){
				//给list列表设置适配器，
				attendListAdapter = new StuAttendListAdapter(ShowClassSomeTimeAttendActivity.this, myAttendList);
				attendListView.setAdapter(attendListAdapter);
			}
		}
		
	};
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_show_attend);
		MyDeBug.L("------ShowClassSomeTimeAttendActivity------");
		intent = getIntent();
		
		classID = intent.getExtras().getInt("classID");
		time = intent.getExtras().getLong("time");
		MyDeBug.L("------123------");
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		actionBar = (TitleBarView) findViewById(R.id.select_attend_actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		actionBar.setBtnLeft(R.drawable.boss_unipay_icon_back, "返回");
		
		attendListView = (ListView) findViewById(R.id.show_attend_list);
		new findAllStuAttendThread().start();
		
	}

	public class findAllStuAttendThread extends Thread{
		
		@Override
		public void run() {
			//用来联网查找此团队下对应的所有人员。
			MyDeBug.L("------findAllStuAttendThread------");
			List<Parameter> params = new ArrayList<Parameter>();
			params.add(  new Parameter("classID", Integer.valueOf( classID ).toString() ) );
			params.add(  new Parameter("time", Long.valueOf( time ).toString() ) );
			
			myAttendList = (ArrayList<MyStudent>) AttendService.instance().findAllStuOfClassAttend(params);
			
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
	
	@Override
	protected void onResume() {
		new findAllStuAttendThread().start();
		super.onResume();
	}
}