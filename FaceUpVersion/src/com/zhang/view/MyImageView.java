package com.zhang.view;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

/**
 * 2d平滑变化的显示图片的ImageView
 * 仅限于用于:从一个ScaleType==CENTER_CROP的ImageView，切换到另一个ScaleType=
 * FIT_CENTER的ImageView，或者反之 (当然，得使用同样的图片最好)
 * 
 * @author Dean Tao
 * 
 */
@SuppressLint("NewApi")
public class MyImageView extends ImageView {

	private static final int STATE_NORMAL = 0;
	private static final int STATE_TRANSFORM_IN = 1;
	private static final int STATE_TRANSFORM_OUT = 2;
	private int mOriginalWidth;
	private int mOriginalHeight;
	private int mOriginalLocationX;
	private int mOriginalLocationY;
	private int mState = STATE_NORMAL;
	private Matrix mSmoothMatrix;
	private Bitmap mBitmap;
	private boolean mTransformStart = false;
	private Transfrom mTransfrom;
	private final int mBgColor = 0xFF000000;
	private int mBgAlpha = 0;
	private Paint mPaint;
	
	public MyImageView(Context context) {
		super(context);
		init();
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mSmoothMatrix = new Matrix();
		mPaint=new Paint();
		mPaint.setColor(mBgColor);
		mPaint.setStyle(Style.FILL);
//		setBackgroundColor(mBgColor);
	}

	public void setOriginalInfo(int width, int height, int locationX, int locationY) {
		mOriginalWidth = width;
		mOriginalHeight = height;
		mOriginalLocationX = locationX;
		mOriginalLocationY = locationY;
		// 因为是屏幕坐标，所以要转换为该视图内的坐标，因为我所用的该视图是MATCH_PARENT，所以不用定位该视图的位置,如果不是的话，还需要定位视图的位置，然后计算mOriginalLocationX和mOriginalLocationY
		mOriginalLocationY = mOriginalLocationY - getStatusBarHeight(getContext());
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 用于开始进入的方法。 调用此方前，需已经调用过setOriginalInfo
	 */
	public void transformIn() {
		mState = STATE_TRANSFORM_IN;
		mTransformStart = true;
		invalidate();
	}

	/**
	 * ���ڿ�ʼ�˳��ķ����� ���ô˷�ǰ�����Ѿ����ù�setOriginalInfo
	 */
	public void transformOut() {
		mState = STATE_TRANSFORM_OUT;
		mTransformStart = true;
		invalidate();
	}

	private class Transfrom {
		float startScale;// ͼƬ��ʼ������ֵ
		float endScale;// ͼƬ���������ֵ
		float scale;// ����ValueAnimator���������ֵ
		LocationSizeF startRect;// ��ʼ������
		LocationSizeF endRect;// ���������
		LocationSizeF rect;// ����ValueAnimator���������ֵ

		void initStartIn() {
			scale = startScale;
			try {
				rect = (LocationSizeF) startRect.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		void initStartOut() {
			scale = endScale;
			try {
				rect = (LocationSizeF) endRect.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * ��ʼ������ı�����Ϣ
	 */
	private void initTransform() {
		if (getDrawable() == null) {
			return;
		}
		if (mBitmap == null || mBitmap.isRecycled()) {
			mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
		}
		//��ֹmTransfrom�ظ�����ͬ��ĳ�ʼ��
		if (mTransfrom != null) {
			return;
		}
		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		mTransfrom = new Transfrom();

		/** ����Ϊ���ŵļ��� */
		/* �����ʼ������ֵ����ʼֵ��Ϊ��CENTR_CROPЧ������Ҫ��֤ͼƬ�Ŀ�͸�����1����ƥ��ԭʼ�Ŀ�͸ߣ���1������ */
		float xSScale = mOriginalWidth / ((float) mBitmap.getWidth());
		float ySScale = mOriginalHeight / ((float) mBitmap.getHeight());
		float startScale = xSScale > ySScale ? xSScale : ySScale;
		mTransfrom.startScale = startScale;
		/* �������ʱ�������ֵ������ֵ��ΪҪ�ﵽFIT_CENTERЧ������Ҫ��֤ͼƬ�Ŀ�͸�����1����ƥ��ԭʼ�Ŀ�͸ߣ���1��С�� */
		float xEScale = getWidth() / ((float) mBitmap.getWidth());
		float yEScale = getHeight() / ((float) mBitmap.getHeight());
		float endScale = xEScale < yEScale ? xEScale : yEScale;
		mTransfrom.endScale = endScale;

		/**
		 * �������Canvas Clip�ķ�Χ��Ҳ����ͼƬ����ʾ�ķ�Χ����ΪͼƬ�������󣬲����ǵȱ���ģ��������Ч����Ҫ�ü�ͼƬ��ʾ������
		 * ������ʾ����ı仯��Χ����ԭʼCENTER_CROPЧ��ķ�Χ����
		 * �������յ�FIT_CENTER�ķ�Χ֮��ģ���������LocationSizeF��ü���
		 * ����Ͱ������϶�����꣬�Ϳ�ߣ����תΪCanvas�ü���Rect.
		 */
		/* ��ʼ���� */
		mTransfrom.startRect = new LocationSizeF();
		mTransfrom.startRect.left = mOriginalLocationX;
		mTransfrom.startRect.top = mOriginalLocationY;
		mTransfrom.startRect.width = mOriginalWidth;
		mTransfrom.startRect.height = mOriginalHeight;
		/* �������� */
		mTransfrom.endRect = new LocationSizeF();
		float bitmapEndWidth = mBitmap.getWidth() * mTransfrom.endScale;// ͼƬ���յĿ��
		float bitmapEndHeight = mBitmap.getHeight() * mTransfrom.endScale;// ͼƬ���յĿ��
		mTransfrom.endRect.left = (getWidth() - bitmapEndWidth) / 2;
		mTransfrom.endRect.top = (getHeight() - bitmapEndHeight) / 2;
		mTransfrom.endRect.width = bitmapEndWidth;
		mTransfrom.endRect.height = bitmapEndHeight;

		mTransfrom.rect = new LocationSizeF();
	}

	private class LocationSizeF implements Cloneable{
		float left;
		float top;
		float width;
		float height;
		@Override
		public String toString() {
			return "[left:"+left+" top:"+top+" width:"+width+" height:"+height+"]";
		}
		
		@Override
		public Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}
		
	}

	/* ����ʵ����CENTER_CROP�Ĺ��� ��Matrix�����Ż��Ĺ���У��Ѿ������� */
	private void getCenterCropMatrix() {
		if (getDrawable() == null) {
			return;
		}
		if (mBitmap == null || mBitmap.isRecycled()) {
			mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
		}
		/* ����ʵ����CENTER_CROP�Ĺ��� */
		float xScale = mOriginalWidth / ((float) mBitmap.getWidth());
		float yScale = mOriginalHeight / ((float) mBitmap.getHeight());
		float scale = xScale > yScale ? xScale : yScale;
		mSmoothMatrix.reset();
		mSmoothMatrix.setScale(scale, scale);
		mSmoothMatrix.postTranslate(-(scale * mBitmap.getWidth() / 2 - mOriginalWidth / 2), -(scale * mBitmap.getHeight() / 2 - mOriginalHeight / 2));
	}

	private void getBmpMatrix() {
		if (getDrawable() == null) {
			return;
		}
		if (mTransfrom == null) {
			return;
		}
		if (mBitmap == null || mBitmap.isRecycled()) {
			mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
		}
		/* ����ʵ����CENTER_CROP�Ĺ��� */
		mSmoothMatrix.setScale(mTransfrom.scale, mTransfrom.scale);
		mSmoothMatrix.postTranslate(-(mTransfrom.scale * mBitmap.getWidth() / 2 - mTransfrom.rect.width / 2),
				-(mTransfrom.scale * mBitmap.getHeight() / 2 - mTransfrom.rect.height / 2));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return; // couldn't resolve the URI
		}

		if (mState == STATE_TRANSFORM_IN || mState == STATE_TRANSFORM_OUT) {
			if (mTransformStart) {
				initTransform();
			}
			if (mTransfrom == null) {
				super.onDraw(canvas);
				return;
			}

			if (mTransformStart) {
				if (mState == STATE_TRANSFORM_IN) {
					mTransfrom.initStartIn();
				} else {
					mTransfrom.initStartOut();
				}
			}

			if(mTransformStart){
				Log.d("Dean", "mTransfrom.startScale:"+mTransfrom.startScale);
				Log.d("Dean", "mTransfrom.startScale:"+mTransfrom.endScale);
				Log.d("Dean", "mTransfrom.scale:"+mTransfrom.scale);
				Log.d("Dean", "mTransfrom.startRect:"+mTransfrom.startRect.toString());
				Log.d("Dean", "mTransfrom.endRect:"+mTransfrom.endRect.toString());
				Log.d("Dean", "mTransfrom.rect:"+mTransfrom.rect.toString());
			}
			
			mPaint.setAlpha(mBgAlpha);
			canvas.drawPaint(mPaint);
			
			int saveCount = canvas.getSaveCount();
			canvas.save();
			// �ȵõ�ͼƬ�ڴ˿̵�ͼ��Matrix����
			getBmpMatrix();
			canvas.translate(mTransfrom.rect.left, mTransfrom.rect.top);
			canvas.clipRect(0, 0, mTransfrom.rect.width, mTransfrom.rect.height);
			canvas.concat(mSmoothMatrix);
			getDrawable().draw(canvas);
			canvas.restoreToCount(saveCount);
			if (mTransformStart) {
				mTransformStart=false;
				startTransform(mState);
			} 
		} else {
			//��Transform In�仯��ɺ󣬰ѱ�����Ϊ��ɫ��ʹ��Activity��͸��
			mPaint.setAlpha(255);
			canvas.drawPaint(mPaint);
			super.onDraw(canvas);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void startTransform(final int state) {
		if (mTransfrom == null) {
			return;
		}
		ValueAnimator valueAnimator = new ValueAnimator();
		valueAnimator.setDuration(300);
		valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		if (state == STATE_TRANSFORM_IN) {
			PropertyValuesHolder scaleHolder = PropertyValuesHolder.ofFloat("scale", mTransfrom.startScale, mTransfrom.endScale);
			PropertyValuesHolder leftHolder = PropertyValuesHolder.ofFloat("left", mTransfrom.startRect.left, mTransfrom.endRect.left);
			PropertyValuesHolder topHolder = PropertyValuesHolder.ofFloat("top", mTransfrom.startRect.top, mTransfrom.endRect.top);
			PropertyValuesHolder widthHolder = PropertyValuesHolder.ofFloat("width", mTransfrom.startRect.width, mTransfrom.endRect.width);
			PropertyValuesHolder heightHolder = PropertyValuesHolder.ofFloat("height", mTransfrom.startRect.height, mTransfrom.endRect.height);
			PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofInt("alpha", 0, 255);
			valueAnimator.setValues(scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder);
		} else {
			PropertyValuesHolder scaleHolder = PropertyValuesHolder.ofFloat("scale", mTransfrom.endScale, mTransfrom.startScale);
			PropertyValuesHolder leftHolder = PropertyValuesHolder.ofFloat("left", mTransfrom.endRect.left, mTransfrom.startRect.left);
			PropertyValuesHolder topHolder = PropertyValuesHolder.ofFloat("top", mTransfrom.endRect.top, mTransfrom.startRect.top);
			PropertyValuesHolder widthHolder = PropertyValuesHolder.ofFloat("width", mTransfrom.endRect.width, mTransfrom.startRect.width);
			PropertyValuesHolder heightHolder = PropertyValuesHolder.ofFloat("height", mTransfrom.endRect.height, mTransfrom.startRect.height);
			PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofInt("alpha", 255, 0);
			valueAnimator.setValues(scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder);
		}

		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public synchronized void onAnimationUpdate(ValueAnimator animation) {
				mTransfrom.scale = (Float) animation.getAnimatedValue("scale");
				mTransfrom.rect.left = (Float) animation.getAnimatedValue("left");
				mTransfrom.rect.top = (Float) animation.getAnimatedValue("top");
				mTransfrom.rect.width = (Float) animation.getAnimatedValue("width");
				mTransfrom.rect.height = (Float) animation.getAnimatedValue("height");
				mBgAlpha = (Integer) animation.getAnimatedValue("alpha");
				invalidate();
				((Activity)getContext()).getWindow().getDecorView().invalidate();
			}
		});
		valueAnimator.addListener(new ValueAnimator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				/*
				 * ����ǽ���Ļ�����Ȼ��ϣ�����ͣ����center_crop�����򡣵��������out�Ļ����Ͳ�Ӧ����center_crop��λ����
				 * �� ��Ӧ�������仯��λ�ã���Ϊ��out��ʱ�����ʱ�����ظ���ͼ��Normal��Ҫ��Ȼ����һ��ͻȻ������ȥ��bug
				 */
				// TODO ������Ը��ʵ���������޸�
				if (state == STATE_TRANSFORM_IN) {
					mState = STATE_NORMAL;
				}
				if (mTransformListener != null) {
					mTransformListener.onTransformComplete(state);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		valueAnimator.start();
	}

	public void setOnTransformListener(TransformListener listener) {
		mTransformListener = listener;
	}

	private TransformListener mTransformListener;

	public static interface TransformListener {
		/**
		 * 
		 * @param mode
		 *            STATE_TRANSFORM_IN 1 ,STATE_TRANSFORM_OUT 2
		 */
		void onTransformComplete(int mode);// mode 1
	}

}
