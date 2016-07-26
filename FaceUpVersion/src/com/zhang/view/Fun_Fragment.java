package com.zhang.view;


import java.util.ArrayList;

import org.json.JSONObject;

import com.faceplusplus.api.FaceDetecter;
import com.faceplusplus.api.FaceDetecter.Face;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zhang.activity.MainActivity;
import com.zhang.service.ThreadManager;
import com.zhang.util.FaceDetect;
import com.zhang.util.FaceHandle;
import com.zhang.util.ImageHandle;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyShowToast;
import com.zhang.util.NetCheck;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Fun_Fragment extends Fragment{

	private Context mContext;
	private View mBaseView;
	
	private Button fun_button_fromPicMap;
	private Button fun_button_takePic;
	private ImageView fun_imageView;
	private TextView fun_result_TextView;
	private Button fun_share_Button;
	private Button fun_findResult_Button;
	private LinearLayout waiting_layout;
	
	ImageHandle imageHandle = null ; 
	FaceDetect faceDetect = null ; 
    Handler detectHandler = null ; 
    FaceDetecter detecter = null ; 
    HttpRequests httpRequests = null ; 
	
    private int faceNumber = 0;
    private String camera_photo_name = null;   //保存的名称
	private Bitmap image = null ; 
	private Bitmap resultBitmap = null ;
	private JSONObject result = null ; //返回JSON类型的人脸信息
    
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Result:
				waiting_layout.setVisibility(View.GONE);
//				waiting_progress.setVisibility(View.GONE);
//				waiting_TextView.setVisibility(View.GONE);
				faceNumber = faceDetect.getFaceNumber(result) ; 
				if( faceNumber == 0 ){
					MyShowToast.showToastShortTime(mContext, "客官，您导入图片的姿势不对，请换个姿势！");
					return ; 
				}else{
					resultBitmap = imageHandle.resultPaint(image, result, faceNumber) ; 
					fun_share_Button.setVisibility(View.VISIBLE);
					fun_imageView.setImageBitmap( resultBitmap );
//					recognition_Person(result, faceNumber ) ; 
					//result.getJSONArray("face").get(i).
				}
				
				break;
			}
			super.handleMessage(msg);
		}
		
		
	};
	private final static int Result = 1;
	
	@SuppressLint("ValidFragment")
	public Fun_Fragment(ImageHandle imageHandle, FaceDetect faceDetect){
		this.imageHandle = imageHandle;
		this.faceDetect = faceDetect;
		this.httpRequests = faceDetect.getHttpRequests();
		this.detectHandler = faceDetect.getHandler();
		this.detecter = faceDetect.getDetecter();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_fun, null);
		findView();
		data_Init();
		listenEvent_Init();
		return mBaseView;
	}
	
	private void findView(){
		fun_button_fromPicMap = (Button) mBaseView.findViewById(R.id.fun_button_fromPicMap);
		fun_button_takePic = (Button) mBaseView.findViewById(R.id.fun_button_takePic);
		fun_imageView = (ImageView) mBaseView.findViewById(R.id.fun_imageView);
		fun_result_TextView = (TextView) mBaseView.findViewById(R.id.fun_result_TextView);
		fun_findResult_Button = (Button) mBaseView.findViewById(R.id.fun_findResult_Button);
		fun_share_Button = (Button) mBaseView.findViewById(R.id.fun_share_Button);
		waiting_layout = (LinearLayout) mBaseView.findViewById(R.id.fun_waiting_layout);
	}
	
	/**
	 * 离线检测器等数据的初始化
	 */
	public void data_Init(){
		
	}
	
	/**
	 * 按钮监听事件的初始化
	 */
	public void listenEvent_Init(){
		fun_button_takePic.setOnClickListener(new OnClickListener() {
			//覆写拍照按钮事件，点击进入照相机，拍照
			@Override
			public void onClick(View v) {
				imageHandle.takePic() ; 
				
			}
		});
		fun_button_fromPicMap.setOnClickListener(new OnClickListener() {
			//覆写从图库中选择图片事件，点击进入图库选择图片。
			@Override
			public void onClick(View v) {
				imageHandle.takeFormPicMap("fun") ; 
			}
		});
		fun_findResult_Button.setOnClickListener(new OnClickListener() {
			//覆写点名按钮事件，点击使用Face++进行人脸识别
			@Override
			public void onClick(View v) {
				//创建一个新线程来调用Face++识别处理图片
				//识别后要圈出人物头像区域！
//				offLine_Detect(image) ; 
				if(image == null){
					MyShowToast.showToastShortTime(mContext, "亲，你忘了导入图片！");
				}else if( !NetCheck.isOnLine(mContext) ){
					MyShowToast.showToastShortTime(mContext, "世界上最远的距离就是没网！");
				}else{
					waiting_layout.setVisibility(View.VISIBLE);
					detectOnLine( imageHandle.pushPictureToArray(image, 100) );
				}
				
			}
		});
		
		fun_share_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击分享图片到QQ空间
				final Bundle params = new Bundle();
                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "小伙伴们快来测颜龄吧！");
                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "我是个****隐藏这么年竟然被发现了！");
                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.wandoujia.com/apps/orange.zhang.app");
                
                //支持传多个imageUrl，这里我们只需要一个图片就行
                ArrayList<String> imageUrls = new ArrayList<String>();
                imageUrls.add( imageHandle.saveBitmap(resultBitmap) );
//                imageUrls.add("/storage/emulated/0/tencent/QQ_Collection/pic/F06E9B619550223C3F89FD4F4E301B17_140.jpg");
                
                // String imageUrl = "XXX";
                // params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl);
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
                doShareToQzone(params);
			}
		});
	}
	
	/**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQzone(final Bundle params) {
        // QZone分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
            	MainActivity.mTencent.shareToQzone((Activity) mContext, params, qZoneShareListener);
            }
        });
    }
    
    IUiListener qZoneShareListener = new IUiListener() {

        @Override
        public void onCancel() {
        	MyShowToast.showToastShortTime(mContext, "onCancel: ");
        }

        @Override
        public void onError(UiError e) {
        	MyShowToast.showToastShortTime(mContext, "onError: ");
        }

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			MyShowToast.showToastShortTime(mContext, "onComplete: ");
		}

    };
	
	@SuppressLint("NewApi")
	public void setImage(Intent intent){
		try{
			//照相所返回来的Intent数据
			if(imageHandle.isFirst()){
				camera_photo_name = imageHandle.getPhotoPath(intent) ; 
			}
			else{
				camera_photo_name = imageHandle.getPictureName() ; 
			}
			image = imageHandle.getImage(camera_photo_name) ; 
			fun_imageView.setVisibility(View.VISIBLE) ; 
			fun_imageView.setImageBitmap( image ) ; 
			
			fun_imageView.buildDrawingCache();
			image = fun_imageView.getDrawingCache();
			MyDeBug.L("再次获取图片宽为：" + image.getWidth());
			MyDeBug.L("再次获取图片高为：" + image.getHeight());
			MyDeBug.L("再次获取图片大小为：" + image.getByteCount() );
			
			fun_result_TextView.setText("");
			
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 在线检测
	 */
	public void detectOnLine(final byte[] array){
		new Thread(new Runnable()
		{
			
			@Override
			public synchronized void run() {
				try {
					//通过图片的二进制数组上传，在线识别图片中人脸
					result = httpRequests.detectionDetect( new PostParameters().setImg(array) ) ; 
					MyDeBug.L("检测得到的结果：" + result);
					
					Message message = handler.obtainMessage();
					message.obj = result;
					message.what = Result;
					MyDeBug.L("？");
					handler.sendMessage(message);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
	}
	
	/**
	 * 利用离线检测器，本地检测图片框出图片中人脸部分！
	 * 从而提高检测速度
	 */
	public void offLine_Detect(final Bitmap image){
		detectHandler.post(new Runnable() {

            @SuppressWarnings("unused")
			@Override
            public void run() {
                final Face[] faceinfos = detecter.findFaces(image);
//                MyDeBug.L( "这里是通过离线检测器检测得结果" + faceinfos.toString() );
                if (faceinfos == null){
                	//如果检测不到人脸
                    ((Activity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, "检测不到人脸", Toast.LENGTH_LONG).show() ; 
                            fun_result_TextView.setText("图片中没有检测到任何人，请重新导入图片！") ; 
                        }
                    });
                    return ; 
                }else{
                	
                	((Activity) mContext).runOnUiThread(new Runnable() {
                		
                        @Override
                        public void run() {
                        	fun_imageView.setImageBitmap( imageHandle.resultPaint(image, faceinfos) );
                        }
                    });
                	
                	try {
                		//检测到人脸，要进行的处理：把检测到的人脸信息，上传进行在线API交互
                    	//这里添加一个检测是否联网的检查！
                		//联网检测得到result结果
                		//然后利用这个result结果来返回人脸信息！
                		if( NetCheck.isOnLine(mContext) ){
                			//联网中，这里添加一个检测时间，如果联网操作超时，那么结束！
                			result = httpRequests.offlineDetect(detecter.getImageByteArray(), detecter.getResultJsonString(), new PostParameters()) ; 
                			recognition_Person(result, faceinfos.length) ; 
//                    		//detecter.getResultJsonString()此函数返回的是一个人脸的四个坐标
//                    		//
                		}else{
                			//断网！
                			((Activity) mContext).runOnUiThread(new Runnable() {
        	                	//在UI线程外更新控件显示内容
        	                    @Override
        	                    public void run() {
        	                    	//这里要利用faceinfo来给图片中人脸部分框出来
        	                    	fun_result_TextView.setText("检测到"+ faceinfos.length + "个人") ; 
        	                    	Toast.makeText(mContext, "网络断开，请检查网络！", Toast.LENGTH_LONG).show() ; 
        	                    }
        	                });
                		}
                		
                    } catch (FaceppParseException e) {
                    	e.printStackTrace() ; 
                    }
                }
                
            }
        });
	}
	
	/**
	 * 利用离线的Face++API接口返回的image图片中人脸的信息，返回显示出来
	 * 
	 */
	public void recognition_Person(final JSONObject result, final int number){
		//image前缀为final，是因为其内有匿名内部类！
		//这里要创建一个线程，在创建的线程中实现识别图片
		((Activity) mContext).runOnUiThread(new Runnable() {
        	//在UI线程外更新控件显示内容
            @Override
            public void run() {
            	//这里要利用faceinfo来给图片中人脸部分框出来
            	FaceHandle faceHandle = new FaceHandle(result, number);
            	fun_result_TextView.setText( faceHandle.makeSpecialMessage() );
//        		fun_result_TextView.setText( faceHandle.makeMessage() );
            }
        });
		
		
//		new Thread(new Runnable()
//		{
//			
//			@Override
//			public synchronized void run() {
//				try {
//					//并返回JSON类型的数据
//					//这里的result数据，其实就是在线检测人脸的/detection/detect方法返回的结果
//					System.out.println("result = " + result);
//					
//					
//					
//					//对检测结果result进行解析
//					faceDetect.findAllFaceName(result, number) ; 
//					
//					((Activity) mContext).runOnUiThread(new Runnable() {
//	                	//在UI线程外更新控件显示内容
//	                    @Override
//	                    public void run() {
//	                    	//这里要利用faceinfo来给图片中人脸部分框出来
//	                    	fun_result_TextView.setText("检测到"+ number + "个人，" + faceDetect + "！");
//	                        System.gc() ; 
//	                    }
//	                });
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			
//		}).start();
//		
	}
	
}
