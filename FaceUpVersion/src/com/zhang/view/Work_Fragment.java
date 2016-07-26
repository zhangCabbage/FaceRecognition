package com.zhang.view;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.faceplusplus.api.FaceDetecter;
import com.faceplusplus.api.FaceDetecter.Face;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.zhang.activity.MainActivity;
import com.zhang.activity.SelectClassActivity;
import com.zhang.activity.DetailPictureActivity;
import com.zhang.adapter.ClassGridAdapter;
import com.zhang.adapter.PhotoGridAdapter;
import com.zhang.service.AttendService;
import com.zhang.service.StudentService;
import com.zhang.util.FaceDetect;
import com.zhang.util.ImageHandle;
import com.zhang.util.InfoConstant;
import com.zhang.util.MyClass;
import com.zhang.util.MyColor;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyShowToast;
import com.zhang.util.MyStudent;
import com.zhang.util.Parameter;

import orange.zhang.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class Work_Fragment extends Fragment{

	private Context mContext;
	private View mBaseView;
	private Intent intent; 

	//用来处理class的一个GridView
	private MyGridView class_GridView;
	private ClassGridAdapter classGridAdapter;
	/**
	 * 传递来的classlist
	 */
	private ArrayList<MyClass> myIntent_ClassList = new ArrayList<MyClass>() ; 
	/**
	 * 要显示的className和颜色的list
	 */
	private ArrayList< Map<String, Object> > classList;
	
	//选择点名的方式
	private Spinner styleSpinner;
	
	//用来处理图片的一个GridView
	private MyGridView photo_GridView;
	private PhotoGridAdapter photoGridAdapter;
	private ArrayList<Bitmap> bitmapList ; 
	
	private TextView result_TextView;
	private LinearLayout layout;
	
	//人脸识别、图像处理所需要的一些数据
	FaceDetect faceDetect = null;
	ImageHandle imageHandle = null ; 
    Handler detectHandler = null ; 
    FaceDetecter detecter = null ; 
    HttpRequests httpRequests = null ; 
    
    private int bitmapIndex = 0 ; //检测到第几张图片了
    private int faceNum = 0 ; //所有照片的人脸数
    
    private Button result_Button = null ; 
    
    private ArrayList<MyStudent> myStudentList = new ArrayList<MyStudent>();//某班级所有的学生列表
    private HashMap<Integer, String> studentsNameIDMap = new HashMap<Integer, String>();
    private StringBuffer strBuf = null ;
    private static final int Result = 23234;
    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Result:
				layout.setVisibility(View.GONE);
				String print = (String) msg.obj;
				MyDeBug.L("-----" + print);
				result_TextView.setText(print);
				break;
			}
		}
    	
    };
    
    public Work_Fragment(ImageHandle imageHandle, FaceDetect faceDetect){
    	this.faceDetect = faceDetect;
    	this.imageHandle = imageHandle;
		this.httpRequests = faceDetect.getHttpRequests();
		this.detectHandler = faceDetect.getHandler();
		this.detecter = faceDetect.getDetecter();
		
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_work, null);
		intent = getActivity().getIntent();
		findView();
		data_Init();
		listenEvent_Init();
		return mBaseView;
	}
	
	public void findView(){
		class_GridView = (MyGridView) mBaseView.findViewById(R.id.work_class_GridView);
		styleSpinner = (Spinner) mBaseView.findViewById(R.id.select_style_spinner);
		photo_GridView = (MyGridView) mBaseView.findViewById(R.id.work_photo_GridView);
		result_Button = (Button) mBaseView.findViewById(R.id.result_find);
		result_TextView = (TextView) mBaseView.findViewById(R.id.result);
		layout = (LinearLayout) mBaseView.findViewById(R.id.work_waiting_layout);
		
	}
	
	@SuppressWarnings("unchecked")
	public void data_Init(){
		classList = new ArrayList< Map<String, Object> >();
		//每次进入的时候，都为空，重新添加
		if(intent.getFlags() == InfoConstant.FromAddActivity){
			myIntent_ClassList = (ArrayList<MyClass>) intent.getSerializableExtra("myclasslist");
			MyDeBug.L( "这里是work_Fragment" + myIntent_ClassList.toString() );
			for(int i=0; i<myIntent_ClassList.size(); i++){
				Map<String, Object> myClassMap = new HashMap<String, Object>();
				myClassMap.put("className", myIntent_ClassList.get(i).getClassName());
				myClassMap.put("classColor", MyColor.getRandomColor());
				classList.add(myClassMap);
			}
		}
		classGridAdapter = new ClassGridAdapter(mContext, R.layout.item_class_gridview, classList);
		class_GridView.setAdapter(classGridAdapter);
		
		//[1静态方法初始化Spinner]
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.style, android.R.layout.simple_spinner_item) ; 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) ; 
		styleSpinner.setAdapter(adapter) ; 
		styleSpinner.setPrompt("方式") ; 
		
		bitmapList = new ArrayList<Bitmap>() ; 
		photoGridAdapter = new PhotoGridAdapter(mContext, R.layout.item_photo_gridview, bitmapList);
		photo_GridView.setAdapter(photoGridAdapter);
		
//		Teacher teacher = (Teacher) intent.getSerializableExtra("teacher");
//		result_TextView.setText( "登陆的用户为：" + teacher.getTeacherName() );
	}
	
	public void listenEvent_Init(){
		
		photo_GridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == bitmapList.size()){
					//拍照事件，添加成员
					imageHandle.takeFormPicMap("");
				}else{
					//这里想设置点击添加了的图片后进入另一个单独的Activity展示大图！
					//目前平滑的转换还没成功！
					Log.i("这是", ""+position);
					Intent intent = new Intent(mContext, DetailPictureActivity.class);
					intent.putExtra("images", (ArrayList<Bitmap>) bitmapList);
					intent.putExtra("position", position);
					int[] location = new int[2];
					view.getLocationOnScreen(location);
					intent.putExtra("locationX", location[0]);
					intent.putExtra("locationY", location[1]);

					intent.putExtra("width", view.getWidth());
					intent.putExtra("height", view.getHeight());
					startActivity(intent);
					((Activity) mContext).overridePendingTransition(0, 0);
				}
			}
		});
		
		class_GridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//
				if(position == classList.size()){
					//点击添加班级，弹到另一个界面进行选择事件
					Intent intent = new Intent();
					intent.setFlags(InfoConstant.SelectClass);
					intent.putExtra("mySelectClass_FromWork", myIntent_ClassList);
					intent.setClass(mContext, SelectClassActivity.class);
					((Activity) mContext).startActivityForResult(intent, InfoConstant.SelectClass);
				}else{
					//点击已经添加过的班级事件
					
				}
			}
		});
		
		result_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击检测图片中人脸信息
				//此时所有的图片都在bitmapList中
				if(MainActivity.isLogin){
					//首先检测看用户是否登陆，否者的话，不能使用点名功能！
					if(myIntent_ClassList.size() == 0){
						MyShowToast.showToastShortTime(mContext, "客官，请先选择一个班级！");
					}else if(myIntent_ClassList.size() == 1){
						
						
						if(bitmapList.size() == 0){
							MyShowToast.showToastShortTime(mContext, "客官，先拍张照片吧！");
						}else{
							layout.setVisibility(View.VISIBLE);
							strBuf = new StringBuffer();
							for(int i=0; i<bitmapList.size(); i++){
								offLine_Detect( bitmapList.get(i) );
//								onLine_Detect( imageHandle.pushPictureToArray(bitmapList.get(i), 100) );
							}
						}
						
						
					}else{
						MyShowToast.showToastShortTime(mContext, "客官，目前还不支持进行多个班级点名！");
					}
					
				}else{
					MyShowToast.showToastLongTime(mContext, "客官，左侧侧栏登陆后才可以使用点名功能哦！");
				}
				
			}
		});
	}
	
	/**
	 * 离线检测，这里点名时，我们用离线检测，因为离线检测，检测的人脸会更多，从而减少点名人数误差
	 * @param image
	 */
	public void offLine_Detect(final Bitmap image){
		detectHandler.post(new Runnable() {

            @Override
            public synchronized void run() {
                final Face[] faceinfos = detecter.findFaces(image);
                if (faceinfos == null){
                	//此照片完全没人
                	bitmapIndex++;
                	if(bitmapIndex == bitmapList.size()){
						//这个时候该返回最终结果了！
                		findNotComeStudent();
                		
                		Message message = handler.obtainMessage();
						message.what = Result;
						message.obj = strBuf.toString();
						handler.sendMessage(message);
					}
                    return ; 
                }else{
                	//有人
                	try {
                		JSONObject result = httpRequests.offlineDetect(detecter.getImageByteArray(), detecter.getResultJsonString(), new PostParameters()) ; 
                		MyDeBug.L("检测得到的结果：" + result);
            			recognition_Person(result, faceinfos.length) ; 
                		
                    } catch (FaceppParseException e) {
                    	e.printStackTrace() ; 
                    }
                }
                
            }
        });
	}
	
	/**
	 * 在线检测
	 */
	public void onLine_Detect(final byte[] array){
		new Thread(new Runnable()
		{
			
			@Override
			public synchronized void run() {
				try {
					//通过图片的二进制数组上传，在线识别图片中人脸
					JSONObject result = httpRequests.detectionDetect( new PostParameters().setImg(array) ) ; 
					MyDeBug.L("检测得到的结果：" + result);
					recognition_Person(result, faceDetect.getFaceNumber(result)) ; 
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
	}
	
	
	public void recognition_Person(final JSONObject result, final int number){
		//具体通过对result结果的分析，得出照片中哪个头像是谁！
		new Thread(new Runnable()
		{
			
			@Override
			public synchronized void run() {
				try {
					faceNum = faceNum + number ; 
					String groupName = myIntent_ClassList.get(0).getClassID()+"_"+myIntent_ClassList.get(0).getClassName() ; 
					faceDetect.findAllFaceName(result, number, groupName) ; 
					
					MyDeBug.L("话说，这是第----" + bitmapIndex++);
					if(bitmapIndex == bitmapList.size()){
						//这个时候该返回最终结果了！
						
						findNotComeStudent();
						
						Message message = handler.obtainMessage();
						message.what = Result;
						message.obj = strBuf.toString();
						handler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
	}
	
	public void findNotComeStudent(){
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(  new Parameter("classID", Integer.valueOf( myIntent_ClassList.get(0).getClassID() ).toString() ) );
		myStudentList = (ArrayList<MyStudent>) StudentService.getInstance().findAllStudent(params);
		//此班级所有的学生列表
		
		studentsNameIDMap = faceDetect.getStudentsNameIDMap();
		
		//针对此次点名操作，进行数据库结果记录
		long timestamp = new Date().getTime();//当前的时间戳
		int flag = 0;//flag==1表示此次考勤异常
		int classID = myIntent_ClassList.get(0).getClassID();//当前考勤的团队ID
		int studentID = 0;
		
		strBuf.append("检测到" + faceNum +"个人！");
		strBuf.append("\n");
		if(myStudentList.size() == 0){
			strBuf.append("此点名班级学生人数为空，请添加学生后再点名！");
		}else{
			strBuf.append("可能未到的人为：").append("\n");
			for(int i=0; i<myStudentList.size(); i++){
				studentID = myStudentList.get(i).getStudentID();
				String curStuName = studentsNameIDMap.get(studentID);
				if(curStuName == null){
					//考勤检查：缺勤
					flag = 1;
					strBuf.append(myStudentList.get(i).getStudentName());
				}
				AttendService.instance().insertAttend(constructAttendParam(timestamp, flag, studentID, classID));
				if(i != myStudentList.size()-1){
					strBuf.append("、");
				}
			}
		}
	}
	
	public List<Parameter> constructAttendParam(long timestamp, int flag, int studentID, int classID){
		List<Parameter> params = new ArrayList<Parameter>();
		params.add( new Parameter( "time", Long.valueOf(timestamp).toString() ) );
		params.add( new Parameter( "flag", Integer.valueOf(flag).toString() ) );
		params.add( new Parameter( "studentID", Integer.valueOf(studentID).toString() ) );
		params.add( new Parameter( "classID", Integer.valueOf(classID).toString() ) );
		return params;
	}
	
	public void setPicture(Intent intent){
		try {
			String camera_photo_name = null;
			//照相所返回来的Intent数据
			if(imageHandle.isFirst()){
				camera_photo_name = imageHandle.getPhotoPath(intent) ; 
			}
			else{
				camera_photo_name = imageHandle.getPictureName() ; 
			}
			Bitmap image = imageHandle.getImage(camera_photo_name) ; 
			System.out.println("======image=======");
			System.out.println(image);
			bitmapList.add(image);
			photoGridAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public void refresh(Intent intent){
		this.intent = intent;
		data_Init();
	}
}
