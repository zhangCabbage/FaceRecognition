package com.zhang.view;


import com.zhang.util.SystemMethod;

import orange.zhang.app.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleBarView extends RelativeLayout {

	private static final String TAG = "TitleBarView";
	private Context mContext;
	
	private Button button_Left;
	private Button button_Right;
	private Button actionBar_Left;
	private Button actionBar_Right;
	private TextView tv_center;
	private LinearLayout common_actionBar_layout;
	public TitleBarView(Context context){
		super(context);
		mContext=context;
		initView();
	}
	
	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}
	
	private void initView(){
		LayoutInflater.from(mContext).inflate(R.layout.common_myaction_bar, this);
		
		button_Left = (Button) findViewById(R.id.title_button_left);
		button_Right=(Button) findViewById(R.id.title_button_right);
		tv_center=(TextView) findViewById(R.id.title_txt);
		actionBar_Left=(Button) findViewById(R.id.actionBar_left);
		actionBar_Right=(Button) findViewById(R.id.actionBar_right);
		
		common_actionBar_layout=(LinearLayout) findViewById(R.id.common_actionBar_layout);
		
	}
	
	public void setCommonTitle(int LeftVisibility,int centerVisibility,int centerBarVisibilter,int rightVisibility){
		button_Left.setVisibility(LeftVisibility);
		button_Right.setVisibility(rightVisibility);
		tv_center.setVisibility(centerVisibility);
		common_actionBar_layout.setVisibility(centerBarVisibilter);
		
	}
	
	public void setBtnLeft(int icon){
		Drawable img=mContext.getResources().getDrawable(icon);
		int height=SystemMethod.dip2px(mContext, 20);
		int width=img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		button_Left.setCompoundDrawables(img, null, null, null);
	}
	
	public void setBtnLeft(int iconID, String str){
		Drawable img=mContext.getResources().getDrawable(iconID);
		int height=SystemMethod.dip2px(mContext, 20);
		int width=img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		button_Left.setCompoundDrawables(img, null, null, null);
		button_Left.setText(str);
	}
	
	public void setBtnRight(int icon){
		Drawable img=mContext.getResources().getDrawable(icon);
		int height=SystemMethod.dip2px(mContext, 30);
		int width=img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		button_Right.setCompoundDrawables(img, null, null, null);
	}
	
	public void setTitleLeft(int resId){
		actionBar_Left.setText(resId);
	}
	
	public void setTitleRight(int resId){
		actionBar_Right.setText(resId);
	}
	
	public void setCenter_TitleText(int txtRes){
		tv_center.setText(txtRes);
	}
	
	public void setBtnLeftOnclickListener(OnClickListener listener){
		button_Left.setOnClickListener(listener);
	}
	
	public void setBtnRightOnclickListener(OnClickListener listener){
		button_Right.setOnClickListener(listener);
	}
	
	public Button getBtnLeft(){
		return button_Left;
	}
	
	public Button getBtnRight(){
		return button_Right;
	}
	
	public Button getTitleLeft(){
		return actionBar_Left;
	}
	
	public Button getTitleRight(){
		return actionBar_Right;
	}
	
	public void destoryView(){
		button_Left.setText(null);
		button_Right.setText(null);
		tv_center.setText(null);
	}

}
