package com.zhang.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import orange.zhang.app.R;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.zhang.service.ClassService;
import com.zhang.service.PreferenceUtils;
import com.zhang.util.BaseActivity;
import com.zhang.util.InfoConstant;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyShowToast;
import com.zhang.util.Parameter;
import com.zhang.view.TitleBarView;

public class ClassAddActivity extends BaseActivity{

	private TitleBarView actionBar;
	private EditText className_EditText;
	private Button sureButton;
	
	private String className;
	private Integer collegeID;
	private Integer schoolID;
	private final static int ADD_Result = 1;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case ADD_Result:
				Integer result = (Integer) msg.obj;
				MyDeBug.L("添加团队的result为：" + result);
				if(result != null && result !=0){
					MyShowToast.showToastShortTime(ClassAddActivity.this, "添加成功！1秒后将自动跳转");
					Timer timer = new Timer();
					TimerTask timerTask = new TimerTask() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							finish();
						}
					};
					timer.schedule(timerTask, 1000);
					
				}else{
					MyShowToast.showToastShortTime(ClassAddActivity.this, "添加失败！");
				}
				break ; 
			}
		}
		
	};
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_add_class);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		actionBar = (TitleBarView) findViewById(R.id.addClass_actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		actionBar.setBtnLeft(R.drawable.boss_unipay_icon_back, "返回");
		
		className_EditText = (EditText) findViewById(R.id.addClass_className);
		sureButton = (Button) findViewById(R.id.addClass_sure);
		
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		actionBar.setBtnLeftOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		sureButton.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				//点击确认添加团队，并调用classservice进行团队的联网添加服务
				className = className_EditText.getText().toString().trim();
				collegeID = 1 ; 
				schoolID = 1 ; 
				if( className != null && !className.isEmpty() ){
					new InsertClassThread().start();
				}else{
					MyShowToast.showToastShortTime(ClassAddActivity.this, "团队名称不能为空");
				}
			}
		});
	}

	public class InsertClassThread extends Thread{

		@Override
		public void run() {
			String teacherID = PreferenceUtils.getPrefString(ClassAddActivity.this, InfoConstant.TeacherID, "");
			
			List<Parameter> params = new ArrayList<Parameter>();
			params.add( new Parameter("className", className) );
			params.add( new Parameter("teacherID", teacherID) );
			params.add( new Parameter("schoolID", schoolID.toString()) );
			Integer result = ClassService.getInstance().insertClass(params);
			Message message = handler.obtainMessage();
			message.what = ADD_Result;
			message.obj = result;
			handler.sendMessage(message);
		}
		
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

}
