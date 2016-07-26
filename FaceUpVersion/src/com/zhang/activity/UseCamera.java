package com.zhang.activity;


import org.json.JSONObject;

import com.faceplusplus.api.FaceDetecter;
import com.faceplusplus.api.FaceDetecter.Face;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.zhang.util.FaceDetect;
import com.zhang.util.ImageHandle;
import com.zhang.util.NetCheck;

import orange.zhang.app.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UseCamera extends Activity{
	
	//Ϊ�˽���ϴ�����ͼƬ���µ�ʶ�������������ʹ��Face++���߼������
	//FaceDetecter����
	
	private Button button_takePic = null ; 
	private Button button_onClick = null ; 
	private Button button_fromPicMap = null ;
	private ImageView imageView = null ; 
	private TextView textView_result = null ; 
	
	ImageHandle imageHandle = null ; 
	FaceDetect faceDetect = null ; 
	
	HandlerThread detectThread = null ; 
    Handler detectHandler = null ; 
    FaceDetecter detecter = null ; 
    HttpRequests httpRequests = null ; 
    
	private String camera_photo_name = null;  //��������
	private Bitmap image = null ; 
	private JSONObject result = null ; //����JSON���͵�������Ϣ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		widget_Init() ; 
		offline_Face_Init() ; 
		listenEvent_Init() ; 
	}
	
	/**
	 * �ؼ��ĳ�ʼ��
	 */
	public void widget_Init(){
		imageView = (ImageView) findViewById(R.id.imageView);
		textView_result = (TextView) findViewById(R.id.textView_result);
		button_takePic = (Button) findViewById(R.id.button_takePic);
		button_onClick = (Button) findViewById(R.id.button_onClick);
		button_fromPicMap = (Button) findViewById(R.id.button_fromPicMap);  //�ؼ�����
		button_onClick.setVisibility(View.INVISIBLE);  //����ť������
	}
	
	/**
	 * ���߼�����ĳ�ʼ��
	 */
	public void offline_Face_Init(){
		imageHandle = new ImageHandle(UseCamera.this) ; 
		
//		faceDetect = new FaceDetect() ; 
		
		detectThread = new HandlerThread("detect");
        detectThread.start();
        detectHandler = new Handler(detectThread.getLooper());
        detecter = new FaceDetecter();
        detecter.init(this, faceDetect.getApiKey());
        
        httpRequests = faceDetect.getHttpRequests();
	}
	
	/**
	 * ��ť�����¼��ĳ�ʼ��
	 */
	public void listenEvent_Init(){
		button_takePic.setOnClickListener(new OnClickListener() {
			//��д���հ�ť�¼�������������������
			@Override
			public void onClick(View v) {
//				imageHandle.takePic() ; 
			}
		});
		button_fromPicMap.setOnClickListener(new OnClickListener() {
			//��д��ͼ����ѡ��ͼƬ�¼����������ͼ��ѡ��ͼƬ��
			@Override
			public void onClick(View v) {
//				imageHandle.takeFormPicMap() ; 
			}
		});
		button_onClick.setOnClickListener(new OnClickListener() {
			//��д����ť�¼������ʹ��Face++��������ʶ��
			@Override
			public void onClick(View v) {
				textView_result.setText("Waiting ...");
				//����һ�����߳�������Face++ʶ����ͼƬ
				//ʶ���ҪȦ������ͷ������
				offLine_Detect(image) ; 
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent) ; 
		
//		if( requestCode == imageHandle.getTAG() ){
//			
//			try{
//				//���ܻᷢ���м�����ͻȻ�˳��������
//				if(imageHandle.isFirst()){
//					camera_photo_name = imageHandle.getPhotoPath(intent) ; 
//				}
//				else{
//					camera_photo_name = imageHandle.getPictureName() ; 
//				}
//				image = imageHandle.getImage(camera_photo_name) ; 
//				imageView.setImageBitmap( image ) ; 
//				button_onClick.setVisibility(View.VISIBLE) ; 
//				//��Ƭ���غ󣬵���ť��ʾ
//				
//			}catch(Exception e){
//				
//			}
//			
//		}
		
	}
	
	/**
	 * �������߼���������ؼ��ͼƬ���ͼƬ���������֣�
	 * �Ӷ���߼���ٶ�
	 */
	public void offLine_Detect(final Bitmap image){
		detectHandler.post(new Runnable() {

            @Override
            public void run() {
                final Face[] faceinfos = detecter.findFaces(image);
                if (faceinfos == null){
                	//����ⲻ������
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(UseCamera.this, "��ⲻ������", Toast.LENGTH_LONG).show() ; 
                            textView_result.setText("ͼƬ��û�м�⵽�κ��ˣ������µ���ͼƬ��") ; 
                        }
                    });
                    return ; 
                }else{
                	//��⵽������Ҫ���еĴ��?�Ѽ�⵽��������Ϣ���ϴ���������API����
                	runOnUiThread(new Runnable() {
                		
                        @Override
                        public void run() {
                        	imageView.setImageBitmap( imageHandle.resultPaint(image, faceinfos) );
                        }
                    });
                	try {
                    	//�������һ������Ƿ�����ļ�飡
                		if( NetCheck.isOnLine(UseCamera.this) ){
                			//������
                			result = httpRequests.offlineDetect(detecter.getImageByteArray(), detecter.getResultJsonString(), new PostParameters()) ; 
                    		//detecter.getResultJsonString()�˺���ص���һ���������ĸ����
                    		//
                			recognition_Person(result, faceinfos.length) ; 
                		}else{
                			//����
                			runOnUiThread(new Runnable() {
        	                	//��UI�߳�����¿ؼ���ʾ����
        	                    @Override
        	                    public void run() {
        	                    	//����Ҫ����faceinfo����ͼƬ���������ֿ����
        	                    	textView_result.setText("��⵽"+ faceinfos.length + "����") ; 
        	                    	Toast.makeText(UseCamera.this, "����Ͽ����������磡", Toast.LENGTH_LONG).show() ; 
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
	 * ����Face++API�ӿ�������ʶ�����imageͼƬ
	 * ���ǱȽ���
	 */
	public void recognition_Person(final JSONObject result, final int number){
		//imageǰ׺Ϊfinal������Ϊ�����������ڲ��࣡
		//����Ҫ����һ���̣߳��ڴ������߳���ʵ��ʶ��ͼƬ
		new Thread(new Runnable()
		{
			
			@Override
			public synchronized void run() {
				try {
					//������JSON���͵����
					//�����result��ݣ���ʵ�������߼��������/detection/detect�������صĽ��
					System.out.println("result = " + result);
					//�Լ����result���н���
					faceDetect.findAllFaceName(result, number, "") ; 
					
					runOnUiThread(new Runnable() {
	                	//��UI�߳�����¿ؼ���ʾ����
	                    @Override
	                    public void run() {
	                    	//����Ҫ����faceinfo����ͼƬ���������ֿ����
	                    	textView_result.setText("��⵽"+ number + "���ˣ�" + faceDetect + "��");
	                        System.gc() ; 
	                    }
	                });
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        detecter.release(this);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.add:
			
			break;
		case R.id.set:
			break;
		case R.id.login:
			break;
		case R.id.exit:
			finish();
			break;
		case R.id.about:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu) ; 
		return true ; 
	}
	
}
