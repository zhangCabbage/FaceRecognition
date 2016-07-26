package com.zhang.activity;

import java.util.ArrayList;
import java.util.List;

import orange.zhang.app.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhang.service.PreferenceUtils;
import com.zhang.service.TeacherService;
import com.zhang.util.BaseActivity;
import com.zhang.util.InfoConstant;
import com.zhang.util.MyShowToast;
import com.zhang.util.NetCheck;
import com.zhang.util.Parameter;
import com.zhang.util.Teacher;


public class LoginActivity extends BaseActivity {

	private RelativeLayout login_user_layout;
	private Button login_Button;
	private Button register_Button;
	
	
	private static final int LOGIN = 1;
	//用户名和密码
	private EditText userTeacher_Edit;
	private EditText userPassword_Edit;
	private String userTeacher;
	private String userPassword;
	private TeacherService teacherService = TeacherService.getInstance();
	
	private LinearLayout layout = null;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		//覆写Handler中的handleMessage方法
		//注意handler处理message是在另外一个线程中
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case LOGIN:
				Teacher teacher = (Teacher) msg.obj;
				layout.setVisibility(View.GONE);
				if(teacher!=null){
					//登陆成功！
					savePreference(teacher);
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//					intent.putExtra("teacher", teacher);
//					intent.setFlags(InfoConstant.Login);
					startActivity(intent);
					finish();
				}else{
					Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
		
	};
	
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		login_user_layout=(RelativeLayout) findViewById(R.id.login_user_layout);
		login_Button=(Button) findViewById(R.id.login_in);//输入账号密码后验证登陆
		register_Button=(Button) findViewById(R.id.register);//注册新用户
		
		userTeacher_Edit = (EditText) findViewById(R.id.login_user);
		userPassword_Edit = (EditText) findViewById(R.id.login_password);
		
		layout = (LinearLayout) findViewById(R.id.login_waiting_layout);
		
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		login_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//登陆
				login();
			}
		});
		register_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//新用户注册
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		Animation anim=AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_anim);
		anim.setFillAfter(true);
		login_user_layout.startAnimation(anim);
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void login(){
		userTeacher = userTeacher_Edit.getText().toString().trim();
		userPassword = userPassword_Edit.getText().toString().trim();
		System.out.println("================================");
		System.out.println("teacher="+userTeacher + "，password="+userPassword);
		
		if( userTeacher.isEmpty() || userPassword.isEmpty() ){
			//登陆不成功
			MyShowToast.showToastShortTime(getApplicationContext(), "用户名或密码不能为空");
		}else if( !NetCheck.isOnLine(getApplicationContext()) ){
			MyShowToast.showToastShortTime(getApplicationContext(), "世界上最远的距离就是没网！");
		}else{
			layout.setVisibility(View.VISIBLE);
			//我们这里把检测用户、密码信息单独放在一个线程中
			new LoginThread().start();
		}
	}
	
	//登录线程
	public class LoginThread extends Thread{
		public void run(){
			List<Parameter> params = new ArrayList<Parameter>();
			params.add(new Parameter("teacher", userTeacher));
			params.add(new Parameter("password", userPassword));
			Teacher teacher = teacherService.checkUser(params);
			if(teacher!=null){
				System.out.println("LoginThread-result-not-null:");
			}else{
				System.out.println("LoginThread-result-Null:");
			}
//			mHandler.sendEmptyMessage(LOGIN);
			Message message = handler.obtainMessage();//提高效率！效果上和new Message()是一样的
			message.what = LOGIN;
			message.obj = teacher;
			handler.sendMessage(message);
		}
	}
	
	public void savePreference(Teacher teacher){
		PreferenceUtils.setPrefString(this, InfoConstant.TeacherID, teacher.getTeacherID());
		PreferenceUtils.setPrefString(this, InfoConstant.TeacherName, teacher.getTeacherName());
		PreferenceUtils.setPrefString(this, InfoConstant.TeacherPassword, teacher.getPassword());
		
	}
}
