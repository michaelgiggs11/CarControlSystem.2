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
 * 手势控制之二：手势路线控制.
 * <p>实现的效果：<br>
 * <p>根据用户在屏幕上画出的图形，相应地发送小车控制指令<br>
 * <p>注：只支持方形、三角形、圆形、8字形和U字形<br>
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

		
		/* 查看SDCard是否存在 */
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			/* SD卡不存在，显示信息 */
			gtv.setText("SD卡不存在!无法运行");

		}
		else {
			/* 取得GestureLibrary的文件路径 */
			gesPath = new File(Environment.getExternalStorageDirectory(),
					"MyGesturesLib").getAbsolutePath();////......................////////

			File file = new File(gesPath);

			if (!file.exists()) {
				/* gestures文件不存在，显示信息 */
				gtv.setText("gestures文件不存在!无法运行");

			}

			else {
				/* 初始化GestureLibrary */
				gesLib = GestureLibraries.fromFile(gesPath);
				if (!gesLib.load()) {
					/* 读取失败，显示信息 */
					gtv.setText("gestures文件读取失败!程序无法运行");
					
				} else {
					/* GestureOverlayView初始化 */
					gesOverlay = (GestureOverlayView) targetView.getChildAt(1);;
					gesOverlay.addOnGesturePerformedListener(new MyListener(this));
				}
			}
		}
		
		
		
		
	}
	/* 定义OnGesturePerformedListener */
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
			/* 手势比对 */
			ArrayList<Prediction> predictions = gesLib.recognize(gesture);
			if (predictions.size() > 0) {
				/* 取最接近的手势 */
				Prediction prediction = predictions.get(0);
				/* 取得预测值至少大于1.0 */
				if (prediction.score > 1.0) {
					/* prediction.name?为?串预测结果 */

					gtv.setText(prediction.name);
					Log.v(TAG, prediction.name);
					if (prediction.name.equals("方形")) {
						mActivity.sendCommand(
								MyCarActivity.DIGITAL_OUT_COMMAND,
								mCommandTarget, value_fang);
					} else if (prediction.name.equals("三角形")) {
						mActivity.sendCommand(
								MyCarActivity.DIGITAL_OUT_COMMAND,
								mCommandTarget, value_sanjiao);
					} else if (prediction.name.equals("圆形")) {
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
					}else if (prediction.name.equals("停止")) {
						mActivity.sendCommand(
								MyCarActivity.DIGITAL_OUT_COMMAND,
								mCommandTarget, value_stop);
					}
				} else {
					/* 比对不到，显示*/
					gtv.setText("找不到匹配的Gesture!");
				}
			} else {
				/* 比对不到，显示 */
				gtv.setText("找不到匹配的Gesture!");
			}
		}
	}
}
