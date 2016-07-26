package com.zhang.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import orange.zhang.app.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.faceplusplus.api.FaceDetecter.Face;
import com.facepp.error.FaceppParseException;
import com.facepp.http.PostParameters;
import com.zhang.adapter.PhotoGridAdapter;
import com.zhang.service.ClassService;
import com.zhang.service.StudentService;
import com.zhang.util.BaseActivity;
import com.zhang.util.FaceDetect;
import com.zhang.util.ImageHandle;
import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyShowToast;
import com.zhang.util.Parameter;
import com.zhang.view.MyGridView;
import com.zhang.view.TitleBarView;

public class StudentAddActivity extends BaseActivity implements OnItemClickListener, OnClickListener{

	private TitleBarView actionBar;
	private MyGridView addStudent_GridView;
	private PhotoGridAdapter photoGridAdapter;
	private List<Bitmap> bitmapList = new ArrayList<Bitmap>();
	private ImageHandle imageHandle = new ImageHandle( StudentAddActivity.this );
	
	private EditText addStudentName_EditText;
	private Button addStudent_sure;
	
	private String studentName;
	private Integer studentID;
	private static final int ADD=100;
	private static final int ADDFail = 101;
	private static final int ADDImage = 102;
	
	private FaceDetect faceDetect = null;
	
	Intent intent = null;
	MyClass selectedClass = null;
	private String groupClassName = null;//以classID作为Face++中group的名字
	
	private LinearLayout waiting_layout = null;
	
	private boolean addOnePerson = false;//表示处理图片的结果
	private int bitmapIndex = 0;//表示第几次对图片进行处理
	private String success_Verify;
	private String success_Identify;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case ADDImage:
				Bitmap image = (Bitmap) msg.obj;
				bitmapList.add(image);
				photoGridAdapter.notifyDataSetChanged();
				break;
			case ADD:
				Integer student_result = msg.arg1;
				Integer class_result = msg.arg2;
				waiting_layout.setVisibility(View.GONE);
				if(student_result != null && student_result !=0 && class_result != null && class_result !=0){
					MyShowToast.showToastShortTime(StudentAddActivity.this, "添加成功！1秒后将自动跳转--" + bitmapIndex);
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
					//如果这里显示添加失败的话，说明向Face++中添加的图片已经成功，但是向数据库中添加人员失败！
					MyShowToast.showToastShortTime(StudentAddActivity.this, "添加失败！");
				}
				break;
			case ADDFail:
				waiting_layout.setVisibility(View.GONE);
				bitmapList = new ArrayList<Bitmap>();
				//bitmapList = bitmapList_New;
				//如果采用这种方法的话，那么更新后，再点击添加按钮就不起作用，这是为什么？
				MyShowToast.showToastShortTime(StudentAddActivity.this, "添加图片没有人脸，请重新添加！");
//				photoGridAdapter.notifyDataSetChanged(); //为什么这句话不起作用
				photoGridAdapter = new PhotoGridAdapter(StudentAddActivity.this, R.layout.item_photo_gridview, bitmapList);
				addStudent_GridView.setAdapter(photoGridAdapter);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_add_student);
		MyDeBug.L("------StudentAddActivity------");
		intent = getIntent();
		selectedClass = (MyClass) intent.getSerializableExtra("selectedClass");
		MyDeBug.L("这是传进来的选中团队" + selectedClass);
		groupClassName = Integer.valueOf( selectedClass.getClassID() ).toString();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
		actionBar = (TitleBarView) findViewById(R.id.addStudent_actionBar);
		actionBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		actionBar.setBtnLeft(R.drawable.boss_unipay_icon_back, "返回");
		
		addStudentName_EditText = (EditText) findViewById(R.id.addStudent_Name);
		
		addStudent_GridView = (MyGridView) findViewById(R.id.addStudent_photo_GridView);
		photoGridAdapter = new PhotoGridAdapter(StudentAddActivity.this, R.layout.item_photo_gridview, bitmapList);
		addStudent_GridView.setAdapter(photoGridAdapter);
		
		addStudent_sure = (Button) findViewById(R.id.addStudent_sure);
		
		waiting_layout = (LinearLayout) findViewById(R.id.addStudent_waiting_layout);
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		actionBar.setBtnLeftOnclickListener(this);
		addStudent_GridView.setOnItemClickListener(this);
		
		addStudent_sure.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		faceDetect = new FaceDetect(StudentAddActivity.this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//当点击GridVIew任意一项时所要调用的事件
		if(position == bitmapList.size()){
			//进行选择图片，这里有两种选择图片的方式
			imageHandle.takeFormPicMap("");
			
		}else{
			
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == actionBar.getBtnLeft()){
			finish();
		}else if(v == addStudent_sure){
			studentName = addStudentName_EditText.getText().toString().trim();
			if( studentName != null && !studentName.isEmpty() && bitmapList.size() != 0 ){
				waiting_layout.setVisibility(View.VISIBLE);
				new InsertStudentThread().start();
			}else{
				MyShowToast.showToastShortTime(StudentAddActivity.this, "人员姓名或图片不能为空");
			}
		}
	}
	
	public class InsertStudentThread extends Thread{

		@Override
		public void run() {
			
			
			try {
				studentID = addOneStuToSQL();
				
				CountDownLatch count = new CountDownLatch(bitmapList.size());
				//对添加的人脸List进行处理，我们以classID作为Face++的组名
				for(int i=0; i < bitmapList.size(); i++){
					offLine_Detect( bitmapList.get(i), true, count);
				}
				
				//这里注意，当在添加时，你要设置一点显示，来提示正在添加！
				count.await();
				if(addOnePerson == true){
					//表示这是最后一个图片，并且前面的图片中至少有一个添加成功了！
					classNumAddOne();
				}else if( addOnePerson == false ){
					failAddStuToFaceAPISQL();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent) ; 
		if(requestCode == imageHandle.get_TAG()){
			try {
				Bitmap image = imageHandle.getImage( imageHandle.getPhotoPath(intent) );
//				Bitmap image = imageHandle.getImage( imageHandle.getPictureName() );
				
				//添加图片后立马对图片进行检测，看是否有人脸
				offLine_Detect(image, false, null);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * 利用离线检测器，本地检测一张图片，框出图片中人脸部分！
	 * 从而提高检测速度
	 */
	public void offLine_Detect(final Bitmap image, final boolean isADDStudent, final CountDownLatch count){
		faceDetect.getHandler().post(new Runnable() {

            @Override
            public synchronized void run() {

                final Face[] faceinfos = faceDetect.getDetecter().findFaces(image) ; 
                if(isADDStudent == true){
                	//是检测添加人脸到Face++数据库中，这里已确定只要进行添加的都是有人脸的！
                	try {
                    	//把人脸画出来，重新返回到gridView中
//                		bitmapList_New.add( imageHandle.resultPaint(image, faceinfos) );
                		
                		JSONObject result = faceDetect.getHttpRequests().offlineDetect(faceDetect.getDetecter().getImageByteArray(), faceDetect.getDetecter().getResultJsonString(), new PostParameters()) ; 
                		//detecter.getResultJsonString()此函数返回的是一个人脸的四个坐标
                		addPersonToFaceAPISQL(result, count);
                		
                    } catch (FaceppParseException e) {
                    	e.printStackTrace() ; 
                    }
                }else{
                	//这里只是检测看，向不向界面中添加
                	if (faceinfos == null || faceinfos.length > 1){
                    	
                    	runOnUiThread(new Runnable() {
    	                	//立马弹出消息
    	                    @Override
    	                    public void run() {
    	                    	MyShowToast.showToastShortTime(StudentAddActivity.this, "添加的图片中无人脸，或者人脸数超过1");
    	                    }
    	                });
                        return ; 
                    }else{
                    	
                    	Bitmap bitmap = imageHandle.resultPaint(image, faceinfos);
                    	Message message = handler.obtainMessage();
                		message.what = ADDImage;
                		message.obj = bitmap;
                		handler.sendMessage(message);
                    }
                }
                
            }
        });
	}
	
	/**
	 * 利用Face++API接口来识别传入的image图片，返回向Face++中添加一个图片是否成功
	 */
	public void addPersonToFaceAPISQL(final JSONObject result, final CountDownLatch count){
		new Thread(new Runnable()
		{
			int addFaceNumber = 0;
			@Override
			public synchronized void run() {
				MyDeBug.L("" + result);
				try{
					String face_id = result.getJSONArray("face").getJSONObject(0).getString("face_id") ; //人脸ID
					MyDeBug.L("人脸的ID = " + face_id);
					
					String studentName_final = Integer.valueOf( selectedClass.getClassID() ).toString() +"_"+ studentID +"_"+ studentName ; 
					addFaceNumber = faceDetect.personAddFace(studentName_final, face_id) ; 
					MyDeBug.L("加入的人脸数为：" + addFaceNumber);
					
					String groupClassName_final = groupClassName +"_"+ selectedClass.getClassName() ; 
					int addPersonNumber = faceDetect.groupAddPerson(groupClassName_final, studentName_final);
					MyDeBug.L("向group中添加的人数为：" + addPersonNumber);
//					System.out.println("向faceset中添加的人数为：" + faceDetect.facesetAddFace(faceSet_name, face_id)) ; 
					
//					success_Search = faceDetect.trainSearch(faceSet_name) ; 
					success_Verify = faceDetect.trainVerify(studentName_final) ; 
					success_Identify = faceDetect.trainIdentify(groupClassName_final) ; 
					
				} catch (Exception e){
					e.printStackTrace();
				}
				
				if(addFaceNumber == 1 && "true".equals( success_Verify ) && success_Identify.equals( "true" ) ){
					addOnePerson = true;
				}
				MyDeBug.L("话说，这是第----" + bitmapIndex++);
				count.countDown();
			}
			
		}).start();
		
	}
	
	public void failAddStuToFaceAPISQL(){
		//this place, we need to delete the student that added before!!
		List<Parameter> param = new ArrayList<Parameter>();
		param.add( new Parameter( "studentID", studentID.toString() ) );
		StudentService.getInstance().deleteStudent(param);
		
		Message message = handler.obtainMessage();
		message.what = ADDFail;
		handler.sendMessage(message);
	}
	
	public void classNumAddOne(){
		Message message = handler.obtainMessage();
		
		MyDeBug.L("更新class表中的对应团队的人员数");
		//更改数据库中class中人员人数
		List<Parameter> param = new ArrayList<Parameter>();
		param.add( new Parameter( "classID", groupClassName ) );
		Integer class_result = ClassService.getInstance().addUpdateClass(param);
		
		message.what = ADD;
		message.arg1 = studentID;
		message.arg2 = class_result;
		handler.sendMessage(message);
	}

	private Integer addOneStuToSQL() {
		MyDeBug.L("进入InsertStudent");
		//往数据库中添加人员
		List<Parameter> params = new ArrayList<Parameter>();
		params.add( new Parameter("studentName", studentName) );
		params.add( new Parameter("studentGender", "") );
		params.add( new Parameter("studentPhone", "") );
		params.add( new Parameter("classID", Integer.valueOf( selectedClass.getClassID() ).toString()) );
		Integer studentID_result = StudentService.getInstance().insertStudent(params);
		return studentID_result;
	}
}
