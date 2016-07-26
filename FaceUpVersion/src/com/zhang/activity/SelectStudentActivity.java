package com.zhang.activity;

import java.util.ArrayList;
import java.util.List;

import orange.zhang.app.R;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.zhang.adapter.StudentListAdapter;
import com.zhang.service.ClassService;
import com.zhang.service.StudentService;
import com.zhang.util.BaseActivity;
import com.zhang.util.FaceDetect;
import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyShowToast;
import com.zhang.util.MyStudent;
import com.zhang.util.Parameter;
import com.zhang.view.TitleBarView;

public class SelectStudentActivity extends BaseActivity implements OnClickListener{

	private TitleBarView actionBar;
	private ListView studentListView ; 
	private StudentListAdapter studentListAdapter;
	private ArrayList<MyStudent> myStudentList = new ArrayList<MyStudent>();
	private ArrayList<MyStudent> mySelected_studentList = new ArrayList<MyStudent>(); //最后选择的人员
	Intent intent;//上一级Activity传过来的团队信息
	MyClass selectedClass = null ; //此即为选中的团队
	
	private final static int FindStudent = 123;
	private final static int DeleteStudent = 124;
	private final static int InsertStudent = 125;
	
	private Button addStudent_Button;
	private Button deleteStudent_Button;
	
	private FaceDetect faceDetect = null ;
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//
			
			switch(msg.what){
			case FindStudent:
				//给list列表设置适配器，
				studentListAdapter = new StudentListAdapter(SelectStudentActivity.this, myStudentList);
				studentListView.setAdapter(studentListAdapter);
				break;
			case DeleteStudent:
				MyDeBug.L("删除更新后进入Handler中");
				Integer studentResult = msg.arg1;
				Integer classResult = msg.arg2;
				if(studentResult != null && studentResult !=0 && classResult != null && classResult !=0){
					MyShowToast.showToastShortTime(SelectStudentActivity.this, "删除成功！");
					MyDeBug.L("删除成功，重新刷新列表");
					new findAllStudentThread().start();
				}
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_select_student);
		MyDeBug.L("------SelectStudentActivity------");
		intent = getIntent();
		selectedClass = (MyClass) intent.getSerializableExtra("SelectedClass");
		MyDeBug.L("这是传进来的选中团队" + selectedClass);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		actionBar = (TitleBarView) findViewById(R.id.select_student_actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
		actionBar.setBtnLeft(R.drawable.t_back_selector);
		actionBar.setCenter_TitleText(R.string.actionBar_select_student);
		actionBar.setBtnRight(R.drawable.t_true_selector);
		
		studentListView = (ListView) findViewById(R.id.select_student_list);
		new findAllStudentThread().start();
		
		addStudent_Button = (Button) findViewById(R.id.select_student_add_button);
		deleteStudent_Button = (Button) findViewById(R.id.select_student_delete_button);
	}

	public class findAllStudentThread extends Thread{
		
		@Override
		public void run() {
			//用来联网查找此团队下对应的所有人员。
			MyDeBug.L("------findAllStudentThread------");
			List<Parameter> params = new ArrayList<Parameter>();
			params.add(  new Parameter("classID", Integer.valueOf( selectedClass.getClassID() ).toString() ) );
			myStudentList = (ArrayList<MyStudent>) StudentService.getInstance().findAllStudent(params);
			
			if(myStudentList!=null){
				MyDeBug.L("findAllStudentThread-result-not-null:");
			}else{
				MyDeBug.L("findAllStudentThread-result-Null:");
			}
			Message message = handler.obtainMessage();
			message.obj = myStudentList;
			message.what = FindStudent;
			handler.sendMessage(message);
		}
		
	}
	
	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		actionBar.setBtnLeftOnclickListener(this);
		actionBar.setBtnRightOnclickListener(this);
		
		addStudent_Button.setOnClickListener(this);
		deleteStudent_Button.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mySelected_studentList = studentListAdapter.getSelectedStudent();
		if(v == actionBar.getBtnLeft()){
			//返回
			this.finish();
		}else if(v == actionBar.getBtnRight()){
			//已选择好人员，进行图片的添加
			
			if(mySelected_studentList.size() == 0){
				MyShowToast.showToastShortTime(SelectStudentActivity.this, "请选择人员来添加图片！");
			}else if(mySelected_studentList.size() == 1){
				//进入到人员添加图片的界面！
				Intent intent =new Intent();
				intent.putExtra("SelectedClass", selectedClass);
				intent.putExtra("SelectedStudent", mySelected_studentList.get(0));
				//
				intent.setClass(SelectStudentActivity.this, PeopleSettingActivity.class);
				startActivity(intent);
			}else{
				MyShowToast.showToastShortTime(SelectStudentActivity.this, "一次只能给一个人员添加图片！");
			}
			
		}else if(v == addStudent_Button){
			//跳转到另一个页面，进行添加人员
			Intent intent = new Intent();
			intent.putExtra("selectedClass", selectedClass);
			intent.setClass(SelectStudentActivity.this, StudentAddActivity.class);
			startActivity(intent);
		}else if(v == deleteStudent_Button){
			//删除人员
			if(mySelected_studentList.size() == 0){
				MyShowToast.showToastShortTime(SelectStudentActivity.this, "请选择要删除的人员！");
			}else if(mySelected_studentList.size() == 1){
				//弹出窗口确定是否真的要删除！
				new DeleteStudentThread().start();
			}else{
				MyShowToast.showToastShortTime(SelectStudentActivity.this, "一次只能选择一个人员删除！");
			}
			
		}
	}
	
	public class DeleteStudentThread extends Thread{

		@Override
		public void run() {
			//一次只能删一个
			MyDeBug.L("进入DeleteStudentThread");
			List<Parameter> params = new ArrayList<Parameter>();
			int studentID = mySelected_studentList.get(0).getStudentID();
			params.add( new Parameter( "studentID", Integer.valueOf(studentID).toString() ) );
			Integer studentResult = StudentService.getInstance().deleteStudent(params);
			
			MyDeBug.L("更新class表中的对应团队的人员数");
			List<Parameter> param = new ArrayList<Parameter>();
			int classID = selectedClass.getClassID();
			param.add( new Parameter( "classID", Integer.valueOf(classID).toString() ) );
			Integer classResult = ClassService.getInstance().deleteUpdateClass(param);
			
			//把Face++中人删除
			faceDetect = new FaceDetect(SelectStudentActivity.this);
			String deleteStudentName = Integer.valueOf( selectedClass.getClassID() ).toString() +"_"+ myStudentList.get(0).getStudentID() +"_"+ mySelected_studentList.get(0).getStudentName() ; 
			int n = faceDetect.deletePerson( deleteStudentName );
			//删除后还要对group重新进行训练
			String groupClassName = Integer.valueOf( selectedClass.getClassID() ).toString() +"_"+ selectedClass.getClassName();
			String success_Identify = faceDetect.trainIdentify(groupClassName) ; 
			MyDeBug.L("Face++中被删除人员数---" + n);
			MyDeBug.L("Face++中被删除人员结果---" + success_Identify);
			
			
			Message message = handler.obtainMessage();
			message.arg1 = studentResult;
			message.arg2 = classResult;
			message.what = DeleteStudent;
			handler.sendMessage(message);
		}
		
	}
	
	@Override
	protected void onResume() {
		new findAllStudentThread().start();
		super.onResume();
	}
}
