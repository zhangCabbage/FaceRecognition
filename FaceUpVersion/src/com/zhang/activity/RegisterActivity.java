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
import android.widget.LinearLayout;

import com.zhang.service.TeacherService;
import com.zhang.util.BaseActivity;
import com.zhang.util.MyShowToast;
import com.zhang.util.Parameter;
import com.zhang.view.TitleBarView;

//用户注册界面
public class RegisterActivity extends BaseActivity{

	private TitleBarView actionBar;
	
	private EditText name;
	private EditText mail;
	private EditText password;
	private Button registerButton;
	private LinearLayout layout ;
	
	private String teacherName;
	private String teacherMail;
	private String teacherPassword;
	
	private static final int Register = 102;
	private static final int RegisterFail = 103;
	private TeacherService teacherService = TeacherService.getInstance();
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Register:
				layout.setVisibility(View.GONE);
				Integer result_teacher = (Integer) msg.obj;
				if(result_teacher != null && result_teacher != 0){
					//注册成功！
					MyShowToast.showToastShortTime(RegisterActivity.this, "恭喜你注册成功！1秒后将自动跳转--");
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
					MyShowToast.showToastShortTime(RegisterActivity.this, "注册失败！");
				}
				break;
			case RegisterFail:
				layout.setVisibility(View.GONE);
				MyShowToast.showToastShortTime(RegisterActivity.this, "账号已存在，注册失败！");
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_register_phone);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		actionBar = (TitleBarView) findViewById(R.id.register_title_actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		
		actionBar.setBtnLeft(R.drawable.boss_unipay_icon_back, "返回");
		
		name = (EditText) findViewById(R.id.register_editText_name);
		mail = (EditText) findViewById(R.id.register_editText_mail);
		password = (EditText) findViewById(R.id.register_editText_password);
		registerButton = (Button) findViewById(R.id.register_button);
		
		layout = (LinearLayout) findViewById(R.id.register_waiting_layout);
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
		
		registerButton.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				//点击按钮，开始注册用户
				teacherName = name.getText().toString().trim();
				teacherMail = mail.getText().toString().trim();
				teacherPassword = password.getText().toString().trim();
				if( !teacherName.isEmpty() && !teacherMail.isEmpty() && !teacherPassword.isEmpty() ){
					layout.setVisibility(View.VISIBLE);
					new CreateTeacherThread().start();
				}else{
					MyShowToast.showToastShortTime(RegisterActivity.this, "亲，昵称、账户和密码均不能为空！");
				}
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
	
	public class CreateTeacherThread extends Thread{

		@Override
		public void run() {
			//首先要检查看这个teacherMail数据库中是不是已经存在，如果存在那么不能注册！
			
			if( !teacherService.findTeacherMailExist(teacherMail) ){
				List<Parameter> params = new ArrayList<Parameter>();
				params.add(new Parameter("teacherName", teacherName));
				params.add(new Parameter("teacherMail", teacherMail));
				params.add(new Parameter("teacherPassword", teacherPassword));
				
				Integer result_Teacher = teacherService.insertTeacher(params);
//				mHandler.sendEmptyMessage(LOGIN);
				Message message = handler.obtainMessage();//提高效率！效果上和new Message()是一样的
				message.what = Register;
				message.obj = result_Teacher;
				handler.sendMessage(message);
			}else{
				Message message = handler.obtainMessage();//提高效率！效果上和new Message()是一样的
				message.what = RegisterFail;
				handler.sendMessage(message);
			}
			
		}
		
	}
}
