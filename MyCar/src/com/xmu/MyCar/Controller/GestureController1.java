package com.xmu.MyCar.Controller;



import com.xmu.MyCar.MyCarActivity;

import android.content.res.Resources;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import android.widget.TextView;

/**
 * 手势控制之一：滑动控制.
 * <p>实现的效果：<br>
 * <p>根据用户在屏幕上向不同方向的滑动，发送小车前进、后退、左转、右转的指令<br>
 * 
 * @author shichao
 */ 
public class GestureController1 {
	private final byte mCommandTarget;
	private MyCarActivity mActivity;
	private TextView gtv;
	public static final byte value_forward = 1;
	public static final byte value_backward = 2;
	public static final byte value_left = 3;
	public static final byte value_right = 4;
	public static final byte value_stop = 5;
	
	private static final float FLING_MIN_DISTANCE = 100;
	private static final float FLING_MIN_VELOCITY = 200;
	
	private static final String TAG = "GestureController";
	GestureDetector mGestureDetector;

	public GestureController1(MyCarActivity activity,
			int digitalOutIndexNumber, Resources res) {
		mActivity = activity;
		mCommandTarget = (byte) (digitalOutIndexNumber - 1);

	}

	public void attachToView(ViewGroup targetView) {
		// btn_F = (Button)targetView.getChildAt(1);//1
		// btn_F.setOnClickListener(new btn_FListener());
		
		// mGestureDetector.setIsLongpressEnabled(false);
		mGestureDetector = new GestureDetector(new gtv_Listener());
		gtv = (TextView) targetView.getChildAt(0);
		// set OnTouchListener on TextView
		gtv.setOnTouchListener(new gtv_Listener());
		gtv.setLongClickable(true);
		// show some text
		// gtv.setText(R.string.Gesture_text);
	}
class gtv_Listener implements OnGestureListener, OnTouchListener {
	public boolean onTouch(View v, MotionEvent event) {
		// OnGestureListener will analyzes the given motion event
		return mGestureDetector.onTouchEvent(event);
	}

	// 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "onDown", Toast.LENGTH_SHORT).show();
		Log.v(TAG, "onDown");
		return false;
	}

	// 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
	// 注意和onDown()的区别，强调的是没有松开或者拖动的状态
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	// 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * // 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE,
	 * 1个ACTION_UP触发
	 * 
	 * @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float
	 * velocityX, float velocityY) { // TODO Auto-generated method stub return
	 * false; }
	 */
	// 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	// 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// 参数解释：
		// e1：第1个ACTION_DOWN MotionEvent
		// e2：最后一个ACTION_MOVE MotionEvent
		// velocityX：X轴上的移动速度，像素/秒
		// velocityY：Y轴上的移动速度，像素/秒

		// 触发条件 ：
		// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
			if (Math.abs(e1.getX() - e2.getX()) >= Math.abs(e1.getY()
					- e2.getY())) {
				if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
						&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					// Fling left
					Log.v(TAG, "Fling Left");
					mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
							mCommandTarget, value_left);
					gtv.setText("左转");
				} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
						&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					// Fling right
					mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
							mCommandTarget, value_right);
					gtv.setText("右转");
					Log.v(TAG, "Fling Right");
				}
			}else{
				if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
						&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
					// Fling up
					Log.v(TAG, "Fling Up");
					mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
							mCommandTarget, value_forward);
					gtv.setText("前进");
				} else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
						&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
					// Fling down
					mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
							mCommandTarget, value_backward);
					gtv.setText("后退");
					Log.v(TAG, "Fling Down");
				}
			}
		return false;
	}
}
}
