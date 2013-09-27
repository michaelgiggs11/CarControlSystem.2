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
 * ���ƿ���֮һ����������.
 * <p>ʵ�ֵ�Ч����<br>
 * <p>�����û�����Ļ����ͬ����Ļ���������С��ǰ�������ˡ���ת����ת��ָ��<br>
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

	// �û��ᴥ����������1��MotionEvent ACTION_DOWN����
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "onDown", Toast.LENGTH_SHORT).show();
		Log.v(TAG, "onDown");
		return false;
	}

	// �û��ᴥ����������δ�ɿ����϶�����һ��1��MotionEvent ACTION_DOWN����
	// ע���onDown()������ǿ������û���ɿ������϶���״̬
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	// �û����ᴥ���������ɿ�����һ��1��MotionEvent ACTION_UP����
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * // �û����´������������ƶ����ɿ�����1��MotionEvent ACTION_DOWN, ���ACTION_MOVE,
	 * 1��ACTION_UP����
	 * 
	 * @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float
	 * velocityX, float velocityY) { // TODO Auto-generated method stub return
	 * false; }
	 */
	// �û��������������ɶ��MotionEvent ACTION_DOWN����
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	// �û����´����������϶�����1��MotionEvent ACTION_DOWN, ���ACTION_MOVE����
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// �������ͣ�
		// e1����1��ACTION_DOWN MotionEvent
		// e2�����һ��ACTION_MOVE MotionEvent
		// velocityX��X���ϵ��ƶ��ٶȣ�����/��
		// velocityY��Y���ϵ��ƶ��ٶȣ�����/��

		// �������� ��
		// X�������λ�ƴ���FLING_MIN_DISTANCE�����ƶ��ٶȴ���FLING_MIN_VELOCITY������/��
			if (Math.abs(e1.getX() - e2.getX()) >= Math.abs(e1.getY()
					- e2.getY())) {
				if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
						&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					// Fling left
					Log.v(TAG, "Fling Left");
					mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
							mCommandTarget, value_left);
					gtv.setText("��ת");
				} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
						&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					// Fling right
					mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
							mCommandTarget, value_right);
					gtv.setText("��ת");
					Log.v(TAG, "Fling Right");
				}
			}else{
				if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
						&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
					// Fling up
					Log.v(TAG, "Fling Up");
					mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
							mCommandTarget, value_forward);
					gtv.setText("ǰ��");
				} else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
						&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
					// Fling down
					mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
							mCommandTarget, value_backward);
					gtv.setText("����");
					Log.v(TAG, "Fling Down");
				}
			}
		return false;
	}
}
}
