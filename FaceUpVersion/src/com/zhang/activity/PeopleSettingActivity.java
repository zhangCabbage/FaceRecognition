package com.zhang.activity;


import orange.zhang.app.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.zhang.util.BaseActivity;
import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyStudent;
import com.zhang.view.TitleBarView;

public class PeopleSettingActivity extends BaseActivity implements OnClickListener{

	private TitleBarView actionBar;
	
	private Button addHeadImage;
	private Button searchAttend;
	
	Intent intent = null;
	MyClass selectedClass = null;
	MyStudent selectedStudent = null;
	
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_people_setting);
		MyDeBug.L("------PeopleSettingActivity------");
		intent = getIntent();
		selectedClass = (MyClass) intent.getSerializableExtra("SelectedClass");
		selectedStudent = (MyStudent) intent.getSerializableExtra("SelectedStudent");
		MyDeBug.L("这是传进来的选中团队" + selectedClass);
		MyDeBug.L("这是传进来的选中人员" + selectedStudent); 
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
		actionBar = (TitleBarView) findViewById(R.id.addStudent_actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		actionBar.setBtnLeft(R.drawable.boss_unipay_icon_back, "返回");
		
		
		addHeadImage = (Button) findViewById(R.id.addHeadImage);
		searchAttend = (Button) findViewById(R.id.searchAttend);
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		actionBar.setBtnLeftOnclickListener(this);
		
		addHeadImage.setOnClickListener(this);
		searchAttend.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == actionBar.getBtnLeft()){
			finish();
		}else if(v == addHeadImage){
			//进入到人员添加图片的界面！
			Intent intent =new Intent();
			intent.putExtra("SelectedClass", selectedClass);
			intent.putExtra("SelectedStudent", selectedStudent);
			//
			intent.setClass(PeopleSettingActivity.this, PictureAddActivity.class);
			startActivity(intent);
		}else if(v == searchAttend){
			//显示考勤情况
			//进入到人员添加图片的界面！
			Intent intent =new Intent();
			intent.putExtra("SelectedClass", selectedClass);
			intent.putExtra("SelectedStudent", selectedStudent);
			//
			intent.setClass(PeopleSettingActivity.this, ShowAttendActivity.class);
			startActivity(intent);
		}
	}
}
