package com.liicon.peter.sign;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ionicframework.iapp425169.R;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GestureOverlayView.OnGesturingListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 
 * @author peter
 *
 */
public class DrawGestureActivity extends Activity implements
		OnGesturePerformedListener, OnGesturingListener {

	private GestureOverlayView mDrawGestureView;

	Gesture mGesture;
	private Button saveBt;
	private Button clearBt;
	String path;
	String pathAndName;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_gesture);
		
		Intent last = getIntent();
		path = last.getStringExtra("path");
		
		saveBt = (Button) findViewById(R.id.saveBt);
		clearBt = (Button) findViewById(R.id.clearBt);

		mDrawGestureView = (GestureOverlayView) findViewById(R.id.gesture);

		// 设置手势可多笔画绘制，默认情况为单笔画绘制
		mDrawGestureView
				.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
		// 设置手势的颜色(蓝色)
		mDrawGestureView.setGestureColor(gestureColor(R.color.gestureColor));
		// 设置还没未能形成手势绘制是的颜色(红色)
		mDrawGestureView
				.setUncertainGestureColor(gestureColor(R.color.ungestureColor));
		// 设置手势的粗细
		mDrawGestureView.setGestureStrokeWidth(4);
		/*
		 * 手势绘制完成后淡出屏幕的时间间隔，即绘制完手指离开屏幕后相隔多长时间手势从屏幕上消失；
		 * 可以理解为手势绘制完成手指离开屏幕后到调用onGesturePerformed的时间间隔 默认值为420毫秒，这里设置为2秒
		 */
		mDrawGestureView.setFadeOffset(3600000);
		mDrawGestureView.setSaveEnabled(true);

		// 绑定监听器
		mDrawGestureView.addOnGesturePerformedListener(this);
		mDrawGestureView.addOnGesturingListener(this);

		mDrawGestureView.addOnGestureListener(new OnGestureListener() {

			@Override
			public void onGestureStarted(GestureOverlayView overlay,
					MotionEvent event) {
				// TODO Auto-generated method stub
				// mButton1.setEnabled(false);
				mGesture = null;
			}

			@Override
			public void onGesture(GestureOverlayView overlay, MotionEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGestureEnded(GestureOverlayView overlay,
					MotionEvent event) {
				// TODO Auto-generated method stub
				mGesture = overlay.getGesture();
				if (mGesture != null) {
					// mButton1.setEnabled(true);
				}
			}

			@Override
			public void onGestureCancelled(GestureOverlayView overlay,
					MotionEvent event) {
				// TODO Auto-generated method stub

			}

		});
		
		saveBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveMyBitmap();
				
				Intent intent = new Intent ();
				intent.putExtra("data", DrawGestureActivity.this.pathAndName);
				setResult(200,intent);
				finish();
				
			}
		});
		
		clearBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawGestureView.setFadeOffset(10);//清除前设置时间间隔缩小
				mDrawGestureView.clear(true);
				mDrawGestureView.setFadeOffset(3600000);//清楚后恢复时间间隔
			}
		});

	}

	// 手势绘制完成时调用
	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		// TODO Auto-generated method stub
		
		mGesture = overlay.getGesture();
		if (mGesture != null) {
			// mButton1.setEnabled(true);
		}
	}

	private int gestureColor(int resId) {
		return getResources().getColor(resId);
	}

	private void showMessage(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	// 结束正在绘制手势时调用(手势绘制完成时一般是先调用它在调用onGesturePerformed)
	@Override
	public void onGesturingEnded(GestureOverlayView overlay) {
		// TODO Auto-generated method stub
//		Bitmap bitmap = Bitmap.createBitmap(mDrawGestureView.getDrawingCache());
		// saveMyBitmap(bitmap, null);
	}

	// 正在绘制手势时调用
	@Override
	public void onGesturingStarted(GestureOverlayView overlay) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 移除绑定的监听器
		mDrawGestureView.removeOnGesturePerformedListener(this);
		mDrawGestureView.removeOnGesturingListener(this);
	}

	// 把bitmao图片保存到对应的SD卡路径中

	private void saveMyBitmap() {
		try {
		Bitmap bitmap = mGesture.toBitmap(240, 75, 12, Color.RED);
		// mImageView.setImageBitmap(bitmap);
		if(path == null || path.equals("") || path.equals("null")){
		path = Environment.getExternalStorageDirectory().toString();
		}
		Log.e("?????", path);
		
		  File destDir = new File(path);
		  if (!destDir.exists()) {
		   destDir.mkdirs();
		  }
		
		String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
				+ ".png";// 照片命名
		
		this.pathAndName = path + "/"+name;
		File f = new File(pathAndName);
		
		
		FileOutputStream fos = null;
		
			fos = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
			Toast.makeText(getApplicationContext(), "保存成功", 5000).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Exception", e.getMessage());
			e.printStackTrace();
		}
	}

}