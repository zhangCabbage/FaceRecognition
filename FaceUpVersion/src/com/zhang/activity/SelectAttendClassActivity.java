package com.zhang.activity;

import java.util.ArrayList;
import java.util.Date;

import orange.zhang.app.R;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zhang.util.BaseActivity;
import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyShowToast;
import com.zhang.view.TitleBarView;
import com.zhang.view.wheelview.LoopView;
import com.zhang.view.wheelview.OnItemSelectedListener;

/**
 * use to show the attend of the teacher's class
 * @author DELL
 * 
 */
public class SelectAttendClassActivity extends BaseActivity implements OnClickListener{
	private Date startDate;
	private Date endDate;
	private Button next;
	int emptyLen = 0;
	int curPosition = 5;
	
	LoopView loopView;
	
	private TitleBarView actionBar;
	Intent intent;//上一级Activity传过来的团队信息
	
	private ArrayList<MyClass> myClassList = null ; //用来把所有的class显示出来
	
	@SuppressWarnings("unchecked")
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_select_class_attend);
		MyDeBug.L("------SelectAttendClassActivity------");
		intent = getIntent();
		startDate = (Date) intent.getSerializableExtra("settingStartDate");
		endDate = (Date) intent.getSerializableExtra("settingEndDate");
		myClassList = (ArrayList<MyClass>) intent.getSerializableExtra("myClassList");
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		actionBar = (TitleBarView) findViewById(R.id.select_attend_actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		actionBar.setBtnLeft(R.drawable.boss_unipay_icon_back, "返回");
		
		loopView = (LoopView) findViewById(R.id.loopView);
		next = (Button) findViewById(R.id.next);
		
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		actionBar.setBtnLeftOnclickListener(this);
		actionBar.setBtnRightOnclickListener(this);
		
		next.setOnClickListener(this);
		//滚动监听
		loopView.setListener(new OnItemSelectedListener() {//滚动停止后所在的Item

			@Override
			public void onItemSelected(int index) {
				// TODO Auto-generated method stub
				curPosition = index;
				MyDeBug.L("Item " + index);
				
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
		//给list列表设置适配器，
		ArrayList<String> list = new ArrayList<String>();
		if(myClassList.size() < 6){
			emptyLen = 6 - myClassList.size();
			for(int i = 0; i < emptyLen; i++){
				list.add("...");
			}
		}
		for (int i = 0; i < myClassList.size(); i++) {
			list.add(myClassList.get(i).getClassName());
		}
		
		loopView.setItems(list);
		
		loopView.setNotLoop();//设置是否循环播放
		loopView.setInitPosition(curPosition);//设置初始位置
		loopView.setTextSize(20);//设置字体大小
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == actionBar.getBtnLeft()){
			//返回
			this.finish();
		}else if(v == next){
			MyDeBug.L("button next");
			//下一步设置
			MyDeBug.L("======>");
			MyDeBug.L("curPosition -->" + curPosition);
			MyDeBug.L("emptyLen -->" + emptyLen);
			if(curPosition < emptyLen){
				//如果当前选择的下标比空下的小，那么不能进行下一步
				MyShowToast.showToastLongTime(SelectAttendClassActivity.this, "客官，请选择非空选项！");
			}else{
				//跳转到显示界面
				MyDeBug.L("======>");
				Intent intent =new Intent();
				intent.putExtra("settingStartDate", startDate);
				intent.putExtra("settingEndDate", endDate);
				intent.putExtra("selectedClass", myClassList.get(curPosition - emptyLen));
				
				MyDeBug.L("startDate" + startDate.getTime());
				MyDeBug.L("endDate" + endDate.getTime());
				MyDeBug.L("selectedClass"+startDate.getTime());
				
				intent.setClass(SelectAttendClassActivity.this, ShowClassTotalAttendActivity.class);
				startActivity(intent);
			}
		}
	}
}