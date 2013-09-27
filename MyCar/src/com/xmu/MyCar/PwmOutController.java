package com.xmu.MyCar;

import com.xmu.MyCar.R;
import com.xmu.MyCar.R.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;

public class PwmOutController implements Slider.SliderPositionListener {
	private final byte mCommandTarget;
	private Slider mSlider;
	private Drawable mBlue;
	private MyCarActivity mActivity;

	public PwmOutController(MyCarActivity activity, int pwmoutNumber,Resources res) {
		mActivity = activity;
		mCommandTarget = (byte) pwmoutNumber;
		mBlue = res.getDrawable(R.drawable.scrubber_horizontal_blue_holo_dark);
	}

	public void attachToView(ViewGroup targetView) {
		mSlider = (Slider) targetView.getChildAt(1);
		mSlider.setPositionListener(this);
		mSlider.setSliderBackground(mBlue);
		
	}

	public void onPositionChange(double value) {
		byte v = (byte) (value * 255);
		mActivity.sendCommand(MyCarActivity.PWM_OUT_COMMAND,
				mCommandTarget, v);//v=0-255
		Log.v("***PwmOutController***", "v="+v);
	}

}
