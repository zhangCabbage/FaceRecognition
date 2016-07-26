package com.zhang.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import orange.zhang.app.R;

import com.faceplusplus.api.FaceDetecter;
import com.facepp.http.HttpRequests;
import com.tencent.tauth.Tencent;
import com.zhang.adapter.MySettingListAdapter;
import com.zhang.service.PreferenceUtils;
import com.zhang.util.BaseActivity;
import com.zhang.util.FaceDetect;
import com.zhang.util.ImageHandle;
import com.zhang.util.InfoConstant;
import com.zhang.util.MyDeBug;
import com.zhang.util.MySetting;
import com.zhang.util.MyShowToast;
import com.zhang.util.Teacher;
import com.zhang.view.Fun_Fragment;
import com.zhang.view.TitleBarView;
import com.zhang.view.Work_Fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @ClassName: MainActivity
 * @Description: TODO
 * @author cabbage
 * @date 2015-4-20 上午11:12:36
 */
public class MainActivity extends BaseActivity{

	protected static final String TAG = "MainActivity";
	private TitleBarView actionBar;
	Button actionBar_left;
	Button actionBar_right;
	
	
	Fun_Fragment fun_Fragment;
	Work_Fragment work_Fragment;
	
	private DrawerLayout mDrawerLayout = null;//抽屉菜单栏
	private ListView drawerListView = null ; //菜单栏
	private float downX=0, downY=0, upX=0, upY=0;//用来判断当前触摸手机屏时，触摸点的坐标
	
	ImageHandle imageHandle = null ; 
	FaceDetect faceDetect = null ; 
	HandlerThread detectThread = null ; 
    Handler detectHandler = null ; 
    FaceDetecter detecter = null ; 
    HttpRequests httpRequests = null ; 
	
    public static Tencent mTencent = null ; 
    
    boolean isFirst = true ; 
    boolean isReadyExit = false ; //用来判断是否退出
    long firstTime ; 
    
    Teacher teacher = null ; 
    public static boolean isLogin = false ; //用户是否登陆的标记
    
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
		mTencent = Tencent.createInstance(InfoConstant.tencentAppId, getApplicationContext() );
//		intent = getIntent();
		if( !PreferenceUtils.getPrefString(this, InfoConstant.TeacherID, "").equals("") ){
			teacher = new Teacher();
			
			teacher.setTeacherID( PreferenceUtils.getPrefString(this, InfoConstant.TeacherID, "") );
			teacher.setTeacherName( PreferenceUtils.getPrefString(this, InfoConstant.TeacherName, "") );
			teacher.setPassword( PreferenceUtils.getPrefString(this, InfoConstant.TeacherPassword, "") );
			MyDeBug.L("teacher----" + teacher.getTeacherName());
			isLogin = true;
			MyDeBug.L("isLogin----" + isLogin);
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		actionBar = (TitleBarView) findViewById(R.id.actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
		
		actionBar.setTitleLeft(R.string.actionBar_left);
		actionBar.setTitleRight(R.string.actionBar_right);
		actionBar.setBtnRight(R.drawable.actionbar_person_add);
		actionBar.setBtnLeft(R.drawable.actionbar_left_menu);
		
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		drawerListView = (ListView) findViewById(R.id.left_drawer);
		ArrayList<MySetting> mySettingList = new ArrayList<MySetting>();
		MySetting mySetting;
		if(isLogin){
			mySetting = new MySetting(getResources().getDrawable(R.drawable.setting_c), teacher.getTeacherName());
			mySettingList.add(mySetting);
		}else{
			mySetting = new MySetting(getResources().getDrawable(R.drawable.setting_a), "登陆");
			mySettingList.add(mySetting);
		}
		
		mySetting = new MySetting(getResources().getDrawable(R.drawable.setting_e), "考勤详情");
		mySettingList.add(mySetting);
		
		mySetting = new MySetting(getResources().getDrawable(R.drawable.setting_b), "注销");
		mySettingList.add(mySetting);
		
		mySetting = new MySetting(getResources().getDrawable(R.drawable.setting_d), "检查更新");
		mySettingList.add(mySetting);
		
		MySettingListAdapter adapter = new MySettingListAdapter(MainActivity.this, mySettingList);
//		String[] str = new String[] { "item1", "item2", "item3"};
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, str);
		drawerListView.setAdapter(adapter);
		
		initBase();
	}

	/**
	 * 初始化Face++离线检测器
	 */
	public void initBase(){
		//
		imageHandle = new ImageHandle(this) ; 
		faceDetect = new FaceDetect(MainActivity.this) ; 
		
//		detectThread = new HandlerThread("detect");
//        detectThread.start();
//        detectHandler = new Handler(detectThread.getLooper());
//        detecter = new FaceDetecter();
//        detecter.init(this, faceDetect.getApiKey());
//        httpRequests = faceDetect.getHttpRequests();
	}
	
	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		
		actionBar.getTitleLeft().setOnClickListener(new OnClickListener() {
			//左侧按钮——点名
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (actionBar.getTitleLeft().isEnabled()) {
					actionBar.getTitleLeft().setEnabled(false);
					actionBar.getTitleRight().setEnabled(true);
					isFirst = true ; 
					
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					work_Fragment = new Work_Fragment(imageHandle, faceDetect);
					ft.replace(R.id.main_content, work_Fragment);
					ft.commit();
					
				}
			}
		});
		actionBar.getTitleRight().setOnClickListener(new OnClickListener() {
			//右侧按钮——娱乐
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (actionBar.getTitleRight().isEnabled()) {
					actionBar.getTitleLeft().setEnabled(true);
					actionBar.getTitleRight().setEnabled(false);
					isFirst = false ; 
					
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					fun_Fragment = new Fun_Fragment(imageHandle, faceDetect);
					ft.replace(R.id.main_content, fun_Fragment);
					ft.commit();
				}
			}
		});
		actionBar.setBtnRightOnclickListener(new OnClickListener() {
			//右侧查找添加班级
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isLogin == true){
					Intent intent = new Intent();
					intent.setFlags(InfoConstant.AddClass);
					intent.setClass(MainActivity.this, SelectClassActivity.class);
					MainActivity.this.startActivity(intent);
				}else{
					MyShowToast.showToastLongTime(MainActivity.this, "客官，左侧栏登陆后才能添加考勤团队！");
				}
				
			}
		});
		
		actionBar.setBtnLeftOnclickListener(new OnClickListener() {
			//点击按钮弹出抽屉
			@Override
			public void onClick(View v) {
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		});
		
		drawerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position == 0){
					if(isLogin == false){
						//未登录，点击第一项
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}else{
						//已登录，点击第一项
					}
				}else if(position == 1){
					//考勤详情
					if(isLogin == false){
						MyShowToast.showToastShortTime(getApplicationContext(), "客官，请先登录！");
					}else{
						//
						//
						//
						//
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, SelectAttendTimeActivity.class);
						startActivity(intent);
					}
				}else if(position == 2){
					if(isLogin == true){
						//已经登录才能注销
						PreferenceUtils.setPrefString(getApplicationContext(), InfoConstant.TeacherID, "");
						PreferenceUtils.setPrefString(getApplicationContext(), InfoConstant.TeacherName, "");
						PreferenceUtils.setPrefString(getApplicationContext(), InfoConstant.TeacherPassword, "");
						//刷新一下菜单栏
						
						isLogin = false;
						
						ArrayList<MySetting> mySettingList = new ArrayList<MySetting>();
						MySetting mySetting = new MySetting(getResources().getDrawable(R.drawable.setting_a), "登陆");
						mySettingList.add(mySetting);
						mySetting = new MySetting(getResources().getDrawable(R.drawable.setting_b), "注销");
						mySettingList.add(mySetting);
						mySetting = new MySetting(getResources().getDrawable(R.drawable.setting_d), "检查更新");
						mySettingList.add(mySetting);
						
						MySettingListAdapter adapter = new MySettingListAdapter(MainActivity.this, mySettingList);
						drawerListView.setAdapter(adapter);
						
					}else{
						MyShowToast.showToastShortTime(getApplicationContext(), "客官，请先登录再注销！");
					}
					
				}else if(position == 3){
					MyShowToast.showToastShortTime(getApplicationContext(), "暂无更新!");
				}
			}
		});
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if(isLogin == true){
			actionBar.getTitleLeft().performClick();//登陆后，优先进入左侧按钮！
		}else{
			actionBar.getTitleRight().performClick();//一打开程序，默认点击了右侧按钮！
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent) ; 
		mTencent.onActivityResult(requestCode, resultCode, intent);
		System.out.println(requestCode);
		if( requestCode == imageHandle.get_TAG() ){
			if(isFirst == true){
				work_Fragment.setPicture(intent);
			}else{
				fun_Fragment.setImage(intent);
			}
		}else if( requestCode == InfoConstant.SelectClass ){
			work_Fragment.refresh(intent);
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//当点击返回键时，引发的事件
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
//			exitByTwoClick();
			TwoClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * 双击退出函数方法1
	 */
	public void TwoClick(){
		if(isReadyExit == false){
			isReadyExit = true ;
			firstTime = System.currentTimeMillis();
//			morePopUpWindow.setBackgroundDrawable( new ColorDrawable(Color.parseColor("#F0000000")) );
//			morePopUpWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
//			morePopUpWindow.setAnimationStyle(R.style.app_pop);
//			morePopUpWindow.setOutsideTouchable(true);//点击PopupWindow区域外部,PopupWindow消失  
//			morePopUpWindow.setFocusable(true);//默认是false,当设置为true时，系统会捕获到焦点给popupWindow; 设置此参数获得焦点，否则无法点击;  
//			morePopUpWindow.update();
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
		}else{
			long secondTIme = System.currentTimeMillis();
			if(secondTIme - firstTime < 2000){
				finish();
				System.exit(0);
			}else{
				isReadyExit = false ; 
			}
		}
		
	}
	
	/**
	 * 双击退出函数方法2
	 */
	public void exitByTwoClick(){
		Timer timerExit = null ; 
		if(isReadyExit == false){
			isReadyExit = true; //这里只是准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
			timerExit = new Timer() ; 
			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
					//这是一个给Timer来执行的任务task
					isReadyExit = false ; //取消退出
				}
			};
			timerExit.schedule(task, 2000);//每隔2s如果没有按下返回键那么就取消退出
		}else{
			finish();
			System.exit(0);
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		//处理屏幕触摸事件，在它们分发到窗口之前截获。
		System.out.println("=============================");
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//当手指下按的时候
			downX = event.getX();
			downY = event.getY();
			System.out.println("down" + downX + "、" + downY);
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			//当手指抬起的时候
			upX = event.getX();
			upY = event.getY();
			System.out.println("up" + upX + "、" + upY);
			System.out.println(upY - downY);
			System.out.println(upX - downX);
			
			if(upX - downX > InfoConstant.DrawerLayoutSlideLength){
				//左滑
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}else if(upX - downX < InfoConstant.DrawerLayoutSlideLength){
				//右滑
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}else if(upY - downY > InfoConstant.DrawerLayoutSlideLength){
				//下滑
			}else if(upY - downY < InfoConstant.DrawerLayoutSlideLength){
				//上滑
			}
		}
		return super.dispatchTouchEvent(event);
	}

//	//但是为什么没有效果呢
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		//监听手指点击事件，从而实现对左右滑动监听，设置事件
//		System.out.println("====");
//		float downX=0, downY=0, upX=0, upY=0;
//		if(event.getAction() == MotionEvent.ACTION_DOWN){
//			//当手指下按的时候
//			Log.i("===========", "down");
//			System.out.println("down");
//			downX = event.getX();
//			downY = event.getY();
//		}
//		if(event.getAction() == MotionEvent.ACTION_UP){
//			//当手指抬起的时候
//			Log.i("===========", "up");
//			System.out.println("up");
//			upX = event.getX();
//			upY = event.getY();
//			if(downY - upY > 50){
//				//上滑
//			}else if(upY - downY > 50){
//				//下滑
//			}else if(downX - upX > 50){
//				//左滑
//				Log.i("===========", "sb");
//				Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();  
//				mDrawerLayout.openDrawer(Gravity.LEFT);
//			}else if(upX - downX > 50){
//				//右滑
//				mDrawerLayout.closeDrawer(Gravity.LEFT);
//			}
//		}
//		
//		return super.onTouchEvent(event);
//	}
	
}
