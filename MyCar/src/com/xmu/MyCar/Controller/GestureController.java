package com.xmu.MyCar.Controller;



import java.io.File;
import java.util.ArrayList;

import com.xmu.MyCar.MyCarActivity;



import android.content.Context;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ���ƿ���֮��������·�߿���.
 * <p>ʵ�ֵ�Ч����<br>
 * <p>�����û�����Ļ�ϻ�����ͼ�Σ���Ӧ�ط���С������ָ��<br>
 * <p>ע��ֻ֧�ַ��Ρ������Ρ�Բ�Ρ�8���κ�U����<br>
 * @author shichao
 */ 
public class GestureController {
	private final byte mCommandTarget;
	private MyCarActivity mActivity;
	private TextView gtv;
	private GestureLibrary gesLib;
	private GestureOverlayView gesOverlay;
	private String gesPath;
	//public static final byte value_forward = 1;
	//public static final byte value_backward = 2;
	//public static final byte value_left = 3;
	//public static final byte value_right = 4;
	public static final byte value_stop = 5;
	public static final byte value_fang = 8;
	public static final byte value_sanjiao = 10;
	public static final byte value_yuan = 9;
	public static final byte value_8 = 11;
	public static final byte value_U = 12;
	
	//private static final float FLING_MIN_DISTANCE = 100;
	//private static final float FLING_MIN_VELOCITY = 200;
	
	private static final String TAG = "GestureController";
	GestureDetector mGestureDetector;

	public GestureController(MyCarActivity activity,
			int digitalOutIndexNumber, Resources res) {
		mActivity = activity;
		mCommandTarget = (byte) (digitalOutIndexNumber - 1);

	}

	public void attachToView(ViewGroup targetView) {


		gtv = (TextView) targetView.getChildAt(0);

		
		/* �鿴SDCard�Ƿ���� */
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			/* SD�������ڣ���ʾ��Ϣ */
			gtv.setText("SD��������!�޷�����");

		}
		else {
			/* ȡ��GestureLibrary���ļ�·�� */
			gesPath = new File(Environment.getExternalStorageDirectory(),
					"MyGesturesLib").getAbsolutePath();////......................////////

			File file = new File(gesPath);

			if (!file.exists()) {
				/* gestures�ļ������ڣ���ʾ��Ϣ */
				gtv.setText("gestures�ļ�������!�޷�����");

			}

			else {
				/* ��ʼ��GestureLibrary */
				gesLib = GestureLibraries.fromFile(gesPath);
				if (!gesLib.load()) {
					/* ��ȡʧ�ܣ���ʾ��Ϣ */
					gtv.setText("gestures�ļ���ȡʧ��!�����޷�����");
					
				} else {
					/* GestureOverlayView��ʼ�� */
					gesOverlay = (GestureOverlayView) targetView.getChildAt(1);;
					gesOverlay.addOnGesturePerformedListener(new MyListener(this));
				}
			}
		}
		
		
		
		
	}
	/* ����OnGesturePerformedListener */
	public class MyListener implements OnGesturePerformedListener {
		//private Context context;
/*
		public MyListener(Context context) {
			this.context = context;
		}
*/
		public MyListener(GestureController gestureController) {
			// TODO Auto-generated constructor stub
		}

		public void onGesturePerformed(GestureOverlayView overlay,
				Gesture gesture) {
			/* ���Ʊȶ� */
			ArrayList<Prediction> predictions = gesLib.recognize(gesture);
			if (predictions.size() > 0) {
				/* ȡ��ӽ������� */
				Prediction prediction = predictions.get(0);
				/* ȡ��Ԥ��ֵ���ٴ���1.0 */
				if (prediction.score > 1.0) {
					/* prediction.name?Ϊ?��Ԥ���� */

					gtv.setText(prediction.name);
					Log.v(TAG, prediction.name);
					if (prediction.name.equals("����")) {
						mActivity.sendCommand(
								MyCarActivity.DIGITAL_OUT_COMMAND,
								mCommandTarget, value_fang);
					} else if (prediction.name.equals("������")) {
						mActivity.sendCommand(
								MyCarActivity.DIGITAL_OUT_COMMAND,
								mCommandTarget, value_sanjiao);
					} else if (prediction.name.equals("Բ��")) {
						mActivity.sendCommand(
								MyCarActivity.DIGITAL_OUT_COMMAND,
								mCommandTarget, value_yuan);
					} else if (prediction.name.equals("8")) {
						mActivity.sendCommand(
								MyCarActivity.DIGITAL_OUT_COMMAND,
								mCommandTarget, value_8);
					}else if (prediction.name.equals("U")) {
						mActivity.sendCommand(
								MyCarActivity.DIGITAL_OUT_COMMAND,
								mCommandTarget, value_U);
					}else if (prediction.name.equals("ֹͣ")) {
						mActivity.sendCommand(
								MyCarActivity.DIGITAL_OUT_COMMAND,
								mCommandTarget, value_stop);
					}
				} else {
					/* �ȶԲ�������ʾ*/
					gtv.setText("�Ҳ���ƥ���Gesture!");
				}
			} else {
				/* �ȶԲ�������ʾ */
				gtv.setText("�Ҳ���ƥ���Gesture!");
			}
		}
	}
}
