package com.zhang.activity;

import java.util.ArrayList;
import java.util.List;

import orange.zhang.app.R;

import com.zhang.adapter.ClassListAdapter;
import com.zhang.service.ClassService;
import com.zhang.service.PreferenceUtils;
import com.zhang.util.BaseActivity;
import com.zhang.util.FaceDetect;
import com.zhang.util.InfoConstant;
import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyShowToast;
import com.zhang.util.Parameter;
import com.zhang.view.TitleBarView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * 
 * 
 * @ClassName: AddActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-21 上午11:41:06
 */
public class SelectClassActivity extends BaseActivity implements OnClickListener {

	private TitleBarView actionBar;
	Intent intent;
	
	private ListView classListView;
	private ArrayList<MyClass> myClassList = null ; //用来把所有的class显示出来
	private ArrayList<MyClass> mySelectedClassList = null ; 
	private ArrayList<MyClass> myIntent_ClassList = new ArrayList<MyClass>() ; //要传递回的classlist
	private ClassListAdapter classListAdapter;
	private static final int FIND = 11;
	private static final int Delete = 10;
	
	private FaceDetect faceDetect = null ; 
	
	private Button addClass_button;
	private Button deleteClass_button;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case FIND:
				classListAdapter = new ClassListAdapter(SelectClassActivity.this, myClassList);
				classListView.setAdapter(classListAdapter);
				break;
			case Delete:
				Integer result = (Integer) msg.obj;
				if(result != null && result !=0){
					MyShowToast.showToastShortTime(SelectClassActivity.this, "删除成功！");
					new findclassThread().start();
				}
				break;
			}
		}
		
	};
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_select_class);
		MyDeBug.L("开始执行AddActivity初始化");
		intent = getIntent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
		actionBar = (TitleBarView) findViewById(R.id.actionBar_addPerson);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
		actionBar.setBtnLeft(R.drawable.t_back_selector);
		actionBar.setCenter_TitleText(R.string.actionBar_select_class);
		actionBar.setBtnRight(R.drawable.t_true_selector);
		
		myClassList = new ArrayList<MyClass>();
		if(intent.getFlags() == InfoConstant.SelectClass){
			//如果是选择团队点名的话
			mySelectedClassList = (ArrayList<MyClass>) intent.getSerializableExtra("mySelectClass_FromWork");
		}
		new findclassThread().start();//自己获取数据库中的list数据
		//这里为什么要开一个线程
		//因为要联网，进行Http的操作，不能在主UI线程中，要不然就运行不了！
		//同样这里也解释了为什么这里要添加一个Handler了，因为在外线程中是不能修控件的内容的，只能通过Handler来对控件进行操作
		classListView = (ListView) findViewById(R.id.add_class_list);
//		classListAdapter = new ClassListAdapter(AddActivity.this, myClassList);
//		classListView.setAdapter(classListAdapter);
		MyDeBug.L( intent.getFlags() + "" );
		if(intent.getFlags() == InfoConstant.AddClass){
			//如果是添加团队
			addClass_button = (Button) findViewById(R.id.add_class_student_button);
			addClass_button.setVisibility(View.VISIBLE);
			addClass_button.setOnClickListener(this);
			
			deleteClass_button = (Button) findViewById(R.id.delete_class_student_button);
			deleteClass_button.setVisibility(View.VISIBLE);
			deleteClass_button.setOnClickListener(this);
		}
	}
	
	public class findclassThread extends Thread{
		public void run(){
			String teacherID = PreferenceUtils.getPrefString(SelectClassActivity.this, InfoConstant.TeacherID, "");
			List<Parameter> params = new ArrayList<Parameter>();
			params.add(  new Parameter("teacherID", teacherID ) );
			
			myClassList = (ArrayList<MyClass>) ClassService.getInstance().findAllClass(params);
			
			//
			if(mySelectedClassList != null && mySelectedClassList.size() != 0){
				for(int i=0; i<mySelectedClassList.size(); i++){
					for(int j=0; j<myClassList.size(); j++){
						if( myClassList.get(j).getClassID() == mySelectedClassList.get(i).getClassID() ){
							myClassList.get(j).setSelected(true);
						}
					}
					
				}
			}
			
			if(myClassList!=null){
				MyDeBug.L("LoginThread-result-not-null:");
			}else{
				MyDeBug.L("LoginThread-result-Null:");
			}
//			handler.sendEmptyMessage( FIND );
			Message message = handler.obtainMessage();//提高效率！效果上和new Message()是一样的
			message.what = FIND;
			message.obj = myClassList;
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
		myIntent_ClassList = classListAdapter.getSelectClassList();
		
		if(v == actionBar.getBtnLeft()){
			//左按钮返回事件
			Intent intent = new Intent();
			intent.setFlags(InfoConstant.FromAddActivity);
			intent.putExtra("myclasslist", mySelectedClassList);
			
			setResult(InfoConstant.SelectClass, intent); 
			this.finish();
			
		}else if(v == actionBar.getBtnRight()){
			//右按钮事件
			
			if(intent.getFlags() == InfoConstant.SelectClass){
				//确定选择了这么多团队点名，然后点击确认返回MainActivity
				
				Intent intent = new Intent();
				intent.setFlags(InfoConstant.FromAddActivity);
				intent.putExtra("myclasslist", myIntent_ClassList);
				
				setResult(InfoConstant.SelectClass, intent); 
				finish();
			}
			if(intent.getFlags() == InfoConstant.AddClass){
				//具体来到团队的学生中添加人脸
				//如果一个都没选中或者选中的团队超过一个，则不能进行往团队中添加人脸
				if( myIntent_ClassList.size() == 1 ){
					Intent intent = new Intent();
					intent.putExtra("SelectedClass", myIntent_ClassList.get(0));
					MyDeBug.L("要进入selectStudentActivity" + myIntent_ClassList.get(0));
					intent.setClass(SelectClassActivity.this, SelectStudentActivity.class);
					SelectClassActivity.this.startActivity(intent);
				}else if( myIntent_ClassList.size() == 0 ){
					MyShowToast.showToastShortTime(SelectClassActivity.this, "请选择人员所在的团队，添加团队");
				}else{
					MyShowToast.showToastShortTime(SelectClassActivity.this, "对不起，一个人员只能添加到一个团队中");
				}
				
			}
			
		}else if(v == addClass_button){
			//增加团队的名称
			Intent intent = new Intent();
			intent.setClass(SelectClassActivity.this, ClassAddActivity.class);
			SelectClassActivity.this.startActivity(intent);
			
		}else if(v == deleteClass_button){
			//删除选中的团队
			if(myIntent_ClassList.size() == 0){
				MyShowToast.showToastShortTime(SelectClassActivity.this, "请选择要删除的团队！");
			}else if(myIntent_ClassList.size() == 1){
				//弹出窗口确定是否真的要删除！
				new DeleteClassThread().start();
			}else{
				MyShowToast.showToastShortTime(SelectClassActivity.this, "一次只能选择一个团队删除！");
			}
		}
	}
	public class DeleteClassThread extends Thread{

		@Override
		public void run() {
			List<Parameter> parmas = new ArrayList<Parameter>();
			parmas.add( new Parameter( "classID", Integer.valueOf( myIntent_ClassList.get(0).getClassID() ).toString() ) );
			Integer result = ClassService.getInstance().deleteClass(parmas);
			
			//把Face++中人删除
			faceDetect = new FaceDetect(SelectClassActivity.this);
			String deleteGroupName = Integer.valueOf( myIntent_ClassList.get(0).getClassID() ).toString() +"_"+ myIntent_ClassList.get(0).getClassName() ; 
			int n = faceDetect.deleteGroup( deleteGroupName );
			MyDeBug.L("Face++中被删出了一个团队" + n);
			
			Message message = handler.obtainMessage();
			message.obj = result;
			message.what = Delete;
			handler.sendMessage(message);
			
		}
		
	}
	
	@Override
	protected void onResume() {
		//每次重新被关注时，调用此函数
		new findclassThread().start();
		super.onResume();
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		//当点击返回键时，引发的事件
//		switch(keyCode){
//		case KeyEvent.KEYCODE_BACK:
////			exitByTwoClick();
//			if(intent.getFlags() == InfoConstant.AddClass){
//				//如果是来添加团队的
//				Intent intent = new Intent();
//				intent.setClass(SelectClassActivity.this, MainActivity.class);
//				SelectClassActivity.this.startActivity(intent);
//				this.finish();
//			}else if(intent.getFlags() == InfoConstant.SelectClass){
//				//只是来选择团队的话
//				Intent intent = new Intent();
//				intent.setFlags(InfoConstant.FromAddActivity);
//				intent.putExtra("myclasslist", mySelectedClassList);
//				intent.setClass(SelectClassActivity.this, MainActivity.class);
//				SelectClassActivity.this.startActivity(intent);
//				this.finish();
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
}
