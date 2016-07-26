package com.zhang.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import orange.zhang.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.faceplusplus.api.FaceDetecter.Face;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.TextView;
/**
 * @author zhang
 * 用作对图片进行处理的类
 */
public class ImageHandle {
	private Activity activity = null ; 
	final private int TAKE_PICTURE = 200 ; 
	
	private String picturePath = "/orange/camera/" ; 
	private String pictureName = null ; 
	
	private boolean isFromMap = false ; 
	
	private Bitmap image = null ; 
	
	public ImageHandle(){
		
	}
	
	public ImageHandle(Context activity){
		this.activity = (Activity) activity ; 
	}
	
	/**
	 * 获得类中获取系统图片的方式标识
	 */
	public int get_TAG(){
		return TAKE_PICTURE ; 
	}
	
	/**
	 * 判断是否是第一种从图库中获取照片的方法
	 * @return
	 */
	public boolean isFirst(){
		return isFromMap ; 
	}
	
	/**
	 * 从页面间传递数据的intent中获取到传递的图片路径
	 */
	public String getPhotoPath(Intent intent){
		String camera_photo_name ; 
		Cursor cursor = this.activity.getContentResolver().query(intent.getData(), null, null, null, null);
		//这里intent.getData()返回的是URI型数据，因为这里是选择图库中的图片，
		//本地图片都是由ContentProvider通过对应的uri管理的。
		cursor.moveToFirst() ; 
		int index = cursor.getColumnIndex(ImageColumns.DATA) ; 
		//得到ImageColumns.DATA这个名字的列是第几列
		camera_photo_name = cursor.getString(index) ; 
		return camera_photo_name ; 
	}
	
	/**
	 * 获得图片路径名称，只能得到使用拍照得到的图片路径
	 * @return
	 */
	public String getPictureName(){
		return pictureName ; 
	}
	
	/**
	 *通过图片的全部路径名称，生成并返回Bitmap图片
	 * @param path
	 * @return
	 */
	@SuppressLint("NewApi")
	public Bitmap getImage(String path){
		Options options = new Options() ;
		options.inJustDecodeBounds = true ; 
//		//这里表示只读图片的边。现在生成的image还不是个真正的image，我们只需要从中提取宽高两项属性的数值！
		image = BitmapFactory.decodeFile(path, options);
		
//		int imageW = options.outWidth;
//		int imageH = options.outHeight;
//		MyDeBug.L("图片宽为：" + imageW);
//		MyDeBug.L("图片高为：" + imageH);
//		float WW = 480f;
//		float HH = 800f;
//		int size = 1;
//		
//		if(imageW > imageH && imageW > WW){
//			size = (int)( imageW/WW ) + 1 ; 
//			MyDeBug.L("1---宽大于高" + size);
//		}else if(imageW < imageH && imageH > HH){
//			size = (int)( imageH/HH ) + 1 ;
//			MyDeBug.L("2---高大于宽" + size);
//		}
//		options.inSampleSize = 2; 
		
		options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)options.outWidth / 1024f, (double)options.outHeight / 1024f)));
		MyDeBug.L("图片压缩比为：" + options.inSampleSize);
		//这里是为了设置缩放比例
		options.inJustDecodeBounds = false ; 
		image = BitmapFactory.decodeFile(path, options) ; 
		
		//480*640
		//设置好options的inJustDecodeBounds为false，要来真的了！
		//以上进行了尺寸的压缩，然后就可以对图片进行质量压缩
		
		image = rotateBitmap(readPicDegree(path), image) ; 
		//ͨ通过获取系统把图片旋转的角度，并反转回来！
//		image = this.compessImage(image);
		
		
//		Bitmap b = Bitmap.createBitmap(480, 640, image.getConfig());
//		Canvas canvas = new Canvas(b);
//		//以创建的bitmap为画板
//		Matrix matrix = new Matrix();
//		matrix.setScale(1, 1);
//		canvas.drawBitmap(image, matrix, null);
		MyDeBug.L("======================================");
		MyDeBug.L("压缩后图片宽为：" + image.getWidth());
		MyDeBug.L("压缩后图片高为：" + image.getHeight());
		MyDeBug.L("压缩后图片大小为：" + image.getByteCount() );
		//发现图片旋转与否不影响获取此图片的宽高，可能是因为这图片是手机自己拍的，存放时虽然会旋转，但是获取宽高时不改变
		
		
		return image ; 
	}
	
	/**
	 * 对图片进行质量压缩
	 * @param image 
	 * @return
	 */
	public Bitmap compessImage(Bitmap image){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();    
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos); // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中     
		int  options =  100 ;
		while  (baos.toByteArray().length /  1024  >  300 ) {  // 循环判断如果压缩后图片是否大于100kb,大于继续压缩     
			baos.reset(); // 重置baos即清空baos     
			image.compress(Bitmap.CompressFormat.JPEG, options, baos); // 这里压缩options%，把压缩后的数据存放到baos中     
			options -=  10 ; // 每次都减少10     
		}   
		ByteArrayInputStream isBm =  new  ByteArrayInputStream(baos.toByteArray()); // 把压缩后的数据baos存放到ByteArrayInputStream中     
		Bitmap bitmap = BitmapFactory.decodeStream(isBm,  null ,  null ); // 把ByteArrayInputStream数据生成图片  
		return bitmap;
	}
	
	/**
	 * 把Bitmap型的图片转换成byte数组
	 * @param image 
	 * @param compressSize 表示图片转换成二进制的压缩比例。在0~100之间。100表示不压缩
	 * @return
	 */
	public byte[] pushPictureToArray(Bitmap image, int compressSize){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		//捕获内存缓冲区的数据，转换成字节数组
		float scale = Math.min(1, Math.min(600f / image.getWidth(), 600f / image.getHeight()));
		//
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		//
		Bitmap imgSmall = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
		//继上文一种三参数createBitmap()创建Bitmap类型方法后的又一创建方法
		System.out.println("imgSmall size : " + imgSmall.getWidth() + " " + imgSmall.getHeight()) ;
		
		imgSmall.compress(Bitmap.CompressFormat.JPEG, compressSize, stream);
		//把图片压缩到指定的输出流中。这里100指压缩率，不压缩图片质量最好，0表示压缩后体积最小
		
		System.out.println("imgSmall size : " + imgSmall.getWidth() + " " + imgSmall.getHeight()) ;
		byte[] array = stream.toByteArray();
		return array ; 
	}
	
	/**
	 * 获取图片的第一种方式
	 * 从图库中选择图片
	 */
	public void takeFormPicMap(String string){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		isFromMap = true ; 
		this.activity.startActivityForResult(photoPickerIntent, TAKE_PICTURE) ; 
	}
	
	/**
	 * 设置拍照时照片存储在内存卡中路径，并创建此路径文件
	 */
	public void setPicturePath(String path){
		this.picturePath = Environment.getExternalStorageDirectory().toString() + path ; 
		//当前存储设备的目录,返回的结果是：/mnt/sdcard
		//所以注意path的格式
		File file = new File(picturePath) ; 
		if(!file.exists()){
			file.mkdirs() ; 
		}
	}
	
	/**
	 * 获取图片的第二种方式
	 * 拍照获取图片
	 */
	public void takePic(){
		setPicturePath(picturePath) ; 
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE") ; 
		//获取相机服务的第二种方法！new Intent(MediaStore.ACTION_IMAGE_CAPTURE) ; 
		
		pictureName = picturePath + System.currentTimeMillis()+".png" ; 
		File file = new File(pictureName) ; 
		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)) ; //ֱ直接拍照保存下来的图片被缩小了，这样做可以得到原图
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, Configuration.ORIENTATION_LANDSCAPE) ; //拍照时横屏竖屏的问题
		isFromMap = false ; 
		this.activity.startActivityForResult(intent, TAKE_PICTURE) ; 
		
	}
	
	/**
	 * 把bitmap类型的图片保存在手机中,并返回此图片的url路径
	 */
	public String saveBitmap(Bitmap image){
		String url = null;
		setPicturePath(picturePath) ; 
		
		String saveBitmapName = picturePath + System.currentTimeMillis()+".png" ; 
		File file = new File(saveBitmapName) ; 
		if(file.exists()){
			file.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.PNG, 90, out);
		    out.flush();
		    out.close();
		    MyDeBug.L("已经保存");
		}catch(Exception e) {
		    e.printStackTrace();
		}
		
		return saveBitmapName;
	}
	
	/**
	 * 读出系统拍出的图片被旋转的角度，获取并反转过来！
	 */
	public int readPicDegree(String path){
		int degree = 0 ; 
		try {
			ExifInterface exif = new ExifInterface(path) ;
			//读取图片文件信息的类ExifInterface 
			if(exif != null){
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL) ; 
				//
				switch(orientation){
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90 ; 
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180 ; 
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270 ; 
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println(degree) ; 
		return degree ; 
	}
	
	/**
	 * 利用离线检测出的Face[]数据，来重绘image图片，圈其中人脸部分！
	 * @param image  
	 * @return
	 */
	public Bitmap resultPaint(Bitmap image , Face[] faceinfos){
		//use the red paint
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
//		paint.setStrokeWidth(Math.max(image.getWidth(), image.getHeight()) / 100f);
				
		//create a new canvas
		Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
		//Config_配置
		Canvas canvas = new Canvas(bitmap);
		//以创建的bitmap为画板
		canvas.drawBitmap(image, new Matrix(), null);
		for (Face faceInfo : faceinfos) {
			//foreach遍历模式
			RectF rect = new RectF(image.getWidth() * faceInfo.left, image.getHeight()
                    * faceInfo.top, image.getWidth() * faceInfo.right,
                    image.getHeight()
                            * faceInfo.bottom);
			canvas.drawRect(rect, paint) ; 
		}
		//save new image
		return bitmap;
	}
	
	/**
	 * 利用传入的Face++的JSONObject检测结果数据，来重绘image图片，圈其中人脸部分！
	 * @param image  
	 * @param result  必须为Face++的JSONObject检测结果数据
	 * @return
	 */
	public Bitmap resultPaint(Bitmap image , JSONObject result , int faceNumber){
		//use the red paint
		FaceHandle faceHandle = new FaceHandle(result, faceNumber);
		com.zhang.util.Face[] faces = faceHandle.getFaceList();
		
		Paint paint = new Paint();
		paint.setColor( Color.WHITE );
//		paint.setStrokeWidth(Math.max(image.getWidth(), image.getHeight()) / 100f);
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		//create a new canvas
		Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(image, new Matrix(), null);
		
		try{
			//find out all faces
			for (int i = 0; i < faceNumber; ++i) {
				float x, y, w, h;
				//get the center point
				x = (float)result.getJSONArray("face").getJSONObject(i)
						.getJSONObject("position").getJSONObject("center").getDouble("x");
				y = (float)result.getJSONArray("face").getJSONObject(i)
						.getJSONObject("position").getJSONObject("center").getDouble("y");

				//get face size
				w = (float)result.getJSONArray("face").getJSONObject(i)
						.getJSONObject("position").getDouble("width");
				h = (float)result.getJSONArray("face").getJSONObject(i)
						.getJSONObject("position").getDouble("height");
				
				//change percent value to the real size
				x = x / 100 * image.getWidth();
				y = y / 100 * image.getHeight();
				w = w / 100 * image.getWidth() * 0.6f;
				h = h / 100 * image.getHeight() * 0.6f;

				//draw the box to mark it out
				//四条边分别来画
//				canvas.drawLine(x - w, y - h, x - w, y + h, paint);
//				canvas.drawLine(x - w, y - h, x + w, y - h, paint);
//				canvas.drawLine(x + w, y + h, x - w, y + h, paint);
//				canvas.drawLine(x + w, y + h, x + w, y - h, paint);
				
				RectF rect = new RectF(( x-w ), ( y-h ), ( x+w ), ( y+h ));
				canvas.drawRect(rect, paint) ; 
				
				int age = faces[i].getAgeValue();
				String gender = faces[i].getGenderValue();
				Bitmap ageBitmap = buildAgeBitmap(age, "男性".equals(gender) );
				
				int ageWidth = ageBitmap.getWidth();
				int ageHeigth = ageBitmap.getHeight();
				MyDeBug.L("宽---" + ageWidth+"高---" + ageHeigth);
				if(bitmap.getWidth() < image.getWidth() && bitmap.getHeight() < image.getHeight()){
					float ratio = Math.max(bitmap.getWidth()*1.0f/image.getWidth(), 
							bitmap.getHeight()*1.0f/image.getHeight());
					MyDeBug.L("气泡框缩放比：" + ratio);
					ageBitmap = Bitmap.createScaledBitmap(ageBitmap, (int)(ageWidth*ratio), (int)(ageHeigth*ratio), false);
				}
				
				canvas.drawBitmap(ageBitmap, x-ageBitmap.getWidth()/2, y-h-ageBitmap.getHeight(), null);
			}
		}catch(JSONException e){
			e.printStackTrace();
			System.out.println("error!");
		}
		//save new image
		return bitmap;
	}
	
	private Bitmap buildAgeBitmap(int age, boolean isMale){
		TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.agebitmap, null).findViewById(R.id.id_age_and_gender);
		MyDeBug.L("取得的TextView的内容----" + tv.getText());
		
		if(isMale){
			MyDeBug.L("男");
			tv.setText(age + InfoConstant.BOY[age]);
			tv.setCompoundDrawablesWithIntrinsicBounds(this.activity.getResources().getDrawable( R.drawable.male1 ), null, null, null);
		}else{
			MyDeBug.L("女");
			tv.setText(age + InfoConstant.Girl[age]);
			tv.setCompoundDrawablesWithIntrinsicBounds(this.activity.getResources().getDrawable( R.drawable.female1 ), null, null, null);
		}
//		tv.buildDrawingCache();
//		Bitmap bitmap = tv.getDrawingCache();

		tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
        tv.buildDrawingCache();
        Bitmap bitmap = tv.getDrawingCache();

//		tv.setDrawingCacheEnabled(true);
//		Bitmap bitmap = tv.getDrawingCache();
//		MyDeBug.L("试验----" + tv.getDrawingCache());
//		tv.destroyDrawingCache();
		return bitmap;
	}
	
	/**
	 * 讲图片纠正到正确的方向
	 */
	public Bitmap rotateBitmap(int degree, Bitmap bitmap){
		Matrix matrix = new Matrix() ; 
		matrix.postRotate(degree) ; 
		Bitmap image = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true) ; 
		return image ; 
	}
}

