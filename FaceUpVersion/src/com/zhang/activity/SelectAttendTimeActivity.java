package com.zhang.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import orange.zhang.app.R;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.zhang.activity.SelectClassActivity.findclassThread;
import com.zhang.service.ClassService;
import com.zhang.service.PreferenceUtils;
import com.zhang.util.BaseActivity;
import com.zhang.util.InfoConstant;
import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.Parameter;
import com.zhang.view.TitleBarView;

/**
 * use to show the attend of the teacher's class
 * @author DELL
 * 
 */
public class SelectAttendTimeActivity extends BaseActivity implements OnClickListener{

	private DatePicker startDatePicker;
	private DatePicker endDatePicker;
	
	private Date startDate;
	private Date endDate;
	private Button next;
	private int year = 2016;
	private int month = 7 - 1;
	private int day = 2;
	
	private TitleBarView actionBar;
	private ArrayList<MyClass> myClassList = null ; //用来把所有的class显示出来
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_select_time_attend);
		MyDeBug.L("------SelectAttendTimeActivity------");
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		actionBar = (TitleBarView) findViewById(R.id.select_attend_actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		actionBar.setBtnLeft(R.drawable.boss_unipay_icon_back, "返回");
		
		startDatePicker = (DatePicker) findViewById(R.id.startDatePicker);
		endDatePicker = (DatePicker) findViewById(R.id.endDatePicker);
		next = (Button) findViewById(R.id.next);
		
		new findclassThread().start();//自己获取数据库中的list数据
	}
	
	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		actionBar.setBtnLeftOnclickListener(this);
		actionBar.setBtnRightOnclickListener(this);
		
		startDatePicker.init(year, month, day, new OnDateChangedListener() {
		    @Override
		    public void onDateChanged(DatePicker view, int year,
		            int monthOfYear, int dayOfMonth) {
		        // 获取一个日历对象，并初始化为当前选中的时间
		        Calendar calendar = Calendar.getInstance();
		        calendar.set(year, monthOfYear, dayOfMonth);
		        startDate = calendar.getTime();
		        
		    }
		});
		
		endDatePicker.init(year, month, day, new OnDateChangedListener() {
		    @Override
		    public void onDateChanged(DatePicker view, int year,
		            int monthOfYear, int dayOfMonth) {
		        // 获取一个日历对象，并初始化为当前选中的时间
		        Calendar calendar = Calendar.getInstance();
		        calendar.set(year, monthOfYear, dayOfMonth);
		        endDate = calendar.getTime();
		        
		    }
		});
		
		next.setOnClickListener(this);
	}
	
	public class findclassThread extends Thread{
		public void run(){
			String teacherID = PreferenceUtils.getPrefString(SelectAttendTimeActivity.this, InfoConstant.TeacherID, "");
			List<Parameter> params = new ArrayList<Parameter>();
			params.add(  new Parameter("teacherID", teacherID ) );
			
			myClassList = (ArrayList<MyClass>) ClassService.getInstance().findAllClass(params);
			
			if(myClassList!=null){
				MyDeBug.L("LoginThread-result-not-null:");
			}else{
				MyDeBug.L("LoginThread-result-Null:");
			}
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        startDate = calendar.getTime();
        endDate = calendar.getTime();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == actionBar.getBtnLeft()){
			//返回
			this.finish();
		}else if(v == next){
			//下一步设置
			Intent intent =new Intent();
			intent.putExtra("settingStartDate", startDate);
			intent.putExtra("settingEndDate", endDate);
			intent.putExtra("myClassList", myClassList);
			//
			intent.setClass(SelectAttendTimeActivity.this, SelectAttendClassActivity.class);
			startActivity(intent);
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}