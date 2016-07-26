package com.zhang.activity;

import java.util.ArrayList;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.zhang.view.MyImageView;

public class DetailPictureActivity extends Activity {

	private ArrayList<Bitmap> bitmapList;
	private int mPosition;
	private int mLocationX;
	private int mLocationY;
	private int mWidth;
	private int mHeight;
	
	private MyImageView myImageView ; 

	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bitmapList = (ArrayList<Bitmap>) getIntent().getSerializableExtra("images");
		mPosition = getIntent().getIntExtra("position", 0);
		mLocationX = getIntent().getIntExtra("locationX", 0);
		mLocationY = getIntent().getIntExtra("locationY", 0);
		mWidth = getIntent().getIntExtra("width", 0);
		mHeight = getIntent().getIntExtra("height", 0);
		
		
		myImageView = new MyImageView(this);
		myImageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
		myImageView.transformIn();
		myImageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
		myImageView.setScaleType(ScaleType.FIT_CENTER);
		setContentView(myImageView);
		myImageView.setImageBitmap(bitmapList.get(mPosition));
//		ImageLoader.getInstance().displayImage(bitmapList.get(mPosition), myImageView);
	}
	
	
	@Override
	public void onBackPressed() {
		myImageView.setOnTransformListener(new MyImageView.TransformListener() {
			@Override
			public void onTransformComplete(int mode) {
				if (mode == 2) {
					finish();
				}
			}
		});
		myImageView.transformOut();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(0, 0);
		}
	}
}
