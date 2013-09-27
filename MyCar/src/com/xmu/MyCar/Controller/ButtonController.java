 package com.xmu.MyCar.Controller;

import com.xmu.MyCar.MyCarActivity;
import com.xmu.MyCar.R;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 按键控制.
 * <p>分别对UI界面的按键进行监听，发送相应的控制指令<br>

 * @author liyihuang
 */ 
public class ButtonController {
	private final byte mCommandTarget;
	private MyCarActivity mActivity;

	
	private Button btn_F,btn_B,btn_L,btn_R,btn_S;

	public static final byte value_forward = 1;
	public static final byte value_backward = 2;
	public static final byte value_left = 3;
	public static final byte value_right = 4;
	public static final byte value_stop = 5;
	
	Drawable mForwardImage;
	
	

	public ButtonController(MyCarActivity activity, int digitalOutIndexNumber,
			Resources res) {
		mActivity = activity;
		mCommandTarget = (byte) (digitalOutIndexNumber - 1);
		
	}


	public void attachToView(ViewGroup targetView) {

		btn_F = (Button)targetView.getChildAt(1);//1
		btn_F.setOnClickListener(new btn_FListener());
		btn_L = (Button) targetView.getChildAt(0);//0
		btn_L.setOnClickListener(new btn_LListener());
		btn_R = (Button) targetView.getChildAt(2);//3
		btn_R.setOnClickListener(new btn_RListener());
		btn_B = (Button) targetView.getChildAt(4);//2
		btn_B.setOnClickListener(new btn_BListener());
		btn_S = (Button) targetView.getChildAt(3);//4
		btn_S.setOnClickListener(new btn_SListener());
		
		

	}
	
	
	
//btn_Forward 	
	class btn_FListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
					mCommandTarget, value_forward);
			btn_F.setBackgroundResource(R.drawable.forward_2);
			btn_B.setBackgroundResource(R.drawable.backward_1);
			btn_L.setBackgroundResource(R.drawable.turn_left_1);
			btn_R.setBackgroundResource(R.drawable.turn_right_1);
			btn_S.setBackgroundResource(R.drawable.stop_1);
			
		}
		
	}
	
//btn_Backward
	class btn_BListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
					mCommandTarget, value_backward);
			btn_F.setBackgroundResource(R.drawable.forward_1);
			btn_B.setBackgroundResource(R.drawable.backward_2);
			btn_L.setBackgroundResource(R.drawable.turn_left_1);
			btn_R.setBackgroundResource(R.drawable.turn_right_1);
			btn_S.setBackgroundResource(R.drawable.stop_1);
		}
		
	}
	
// btn_Left
class btn_LListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
					mCommandTarget, value_left);
			btn_F.setBackgroundResource(R.drawable.forward_1);
			btn_B.setBackgroundResource(R.drawable.backward_1);
			btn_L.setBackgroundResource(R.drawable.turn_left_2);
			btn_R.setBackgroundResource(R.drawable.turn_right_1);
			btn_S.setBackgroundResource(R.drawable.stop_1);
	}

}

	// btn_Right
	class btn_RListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
					mCommandTarget, value_right);
			btn_F.setBackgroundResource(R.drawable.forward_1);
			btn_B.setBackgroundResource(R.drawable.backward_1);
			btn_L.setBackgroundResource(R.drawable.turn_left_1);
			btn_R.setBackgroundResource(R.drawable.turn_right_2);
			btn_S.setBackgroundResource(R.drawable.stop_1);
		}

	}
		
	// btn_Stop
		class btn_SListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
						mCommandTarget, value_stop);
				btn_F.setBackgroundResource(R.drawable.forward_1);
				btn_B.setBackgroundResource(R.drawable.backward_1);
				btn_L.setBackgroundResource(R.drawable.turn_left_1);
				btn_R.setBackgroundResource(R.drawable.turn_right_1);
				btn_S.setBackgroundResource(R.drawable.stop_2);
			}

		}

}












