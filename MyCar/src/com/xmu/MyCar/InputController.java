package com.xmu.MyCar;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.xmu.MyCar.R;
import com.xmu.MyCar.R.drawable;
import com.xmu.MyCar.R.id;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class InputController extends AccessoryController {
	

	
	private TextView mLightView;
	private TextView mLightRawView;
	ArrayList<SwitchDisplayer> mSwitchDisplayers;
	private final DecimalFormat mLightValueFormatter = new DecimalFormat("##.#");

	InputController(MyCarActivity hostActivity) {
		super(hostActivity);
		//mLightView = (TextView) findViewById(R.id.lightPercentValue);
		//mLightRawView = (TextView) findViewById(R.id.lightRawValue);
	}

	protected void onAccesssoryAttached() {
		mSwitchDisplayers = new ArrayList<SwitchDisplayer>();
		for (int i = 0; i < 4; ++i) {
			SwitchDisplayer sd = new SwitchDisplayer(i);
			mSwitchDisplayers.add(sd);
		}
	}
/*
	public void setLightValue(int lightValueFromArduino) {
		mLightRawView.setText(String.valueOf(lightValueFromArduino));
		mLightView.setText(mLightValueFormatter
				.format((100.0 * (double) lightValueFromArduino / 1024.0)));
	}
*/
	public void switchStateChanged(int switchIndex, boolean switchState) {
		if (switchIndex >= 0 && switchIndex < mSwitchDisplayers.size()) {
			SwitchDisplayer sd = mSwitchDisplayers.get(switchIndex);
			sd.onSwitchStateChange(switchState);
		}
	}
/*
	public void onLightChange(int lightValue) {
		setLightValue(lightValue);
	}
*/
	public void onSwitchStateChange(int switchIndex, Boolean switchState) {
		switchStateChanged(switchIndex, switchState);
	}

	class SwitchDisplayer {
		private final ImageView mTargetView;
		private final Drawable mOnImage;
		private final Drawable mOffImage;

		SwitchDisplayer(int switchIndex) {
			int viewId, onImageId, offImageId;
			switch (switchIndex) {
			default:
			//?case 0:
				viewId = R.id.Button_left;
				onImageId = R.drawable.indicator_button_left_on_noglow;
				offImageId = R.drawable.indicator_button_left_off_noglow;
				break;
			case 1:
				viewId = R.id.Button_middle;
				onImageId = R.drawable.indicator_button_middle_on_noglow;
				offImageId = R.drawable.indicator_button_middle_off_noglow;
				break;
			case 2:
				viewId = R.id.Button_right;
				onImageId = R.drawable.indicator_button_right_on_noglow;
				offImageId = R.drawable.indicator_button_right_off_noglow;
				break;
			}
			mTargetView = (ImageView) findViewById(viewId);
			mOffImage = mHostActivity.getResources().getDrawable(offImageId);
			mOnImage = mHostActivity.getResources().getDrawable(onImageId);
			
			
		}

		public void onSwitchStateChange(Boolean switchState) {
			Drawable currentImage;
			if (!switchState) {
				currentImage = mOffImage;

			} else {
				currentImage = mOnImage;
			}
			mTargetView.setImageDrawable(currentImage);
		}

	}
}
