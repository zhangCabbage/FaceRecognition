package com.zhang.activity;

import orange.zhang.app.R;

import com.zhang.util.BaseActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/** ����ҳ
  * @ClassName: SplashActivity
  * @Description: TODO
  * @author cabbage
  * @date 2015-4-20 下午2:21:28
  */
@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity {

	private static final int SPLASH_LENGTH = 2000;
	private static final int GO_HOME = 100;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				gotoMain();
				break;
			}
		}
	};
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_splash);

	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_LENGTH);
	}

	public void gotoMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	

}
