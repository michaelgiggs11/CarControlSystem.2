package com.xmu.MyCar;

import com.xmu.MyCar.R;
import com.xmu.MyCar.Controller.ButtonController;
import com.xmu.MyCar.Controller.G_sensorController;
import com.xmu.MyCar.Controller.GestureController;
import com.xmu.MyCar.Controller.GestureController1;
import com.xmu.MyCar.Controller.VoiceController;
import com.xmu.MyCar.R.id;

import android.view.ViewGroup;

public class OutputController extends AccessoryController {

	private boolean mVertical;
	
	OutputController(MyCarActivity hostActivity, boolean vertical) {
		super(hostActivity);
		mVertical = vertical;
	}

	protected void onAccesssoryAttached() {
		setupPwmOutController(0, R.id.pwmout);
		setupButtonController(1, R.id.directionbutton);
		setupG_sensorController(1,R.id.G_sensorContainer);
		setupVoiceController(1,R.id.VoiceContainer);
		setupGestureController1(1,R.id.GestureContainer1);
		setupGestureController(1,R.id.GestureContainer);
	}

	private void setupPwmOutController(int pwmoutIndex, int viewId) {
		PwmOutController sc = new PwmOutController(mHostActivity, pwmoutIndex,
				getResources());
		sc.attachToView((ViewGroup) findViewById(viewId));
	}

	private void setupButtonController(int index, int viewId) {
		ButtonController r = new ButtonController(mHostActivity, index,
				getResources());
		r.attachToView((ViewGroup) findViewById(viewId));
	}
	private void setupG_sensorController(int index, int viewId) {
		G_sensorController r = new G_sensorController(mHostActivity, index,
				getResources());
		r.attachToView((ViewGroup) findViewById(viewId));
	}
	private void setupVoiceController(int index, int viewId) {
		VoiceController r = new VoiceController(mHostActivity, index,
				getResources());
		r.attachToView((ViewGroup) findViewById(viewId));
	}
	private void setupGestureController1(int index, int viewId) {
		GestureController1 r = new GestureController1(mHostActivity, index,
				getResources());
		r.attachToView((ViewGroup) findViewById(viewId));
	}
	private void setupGestureController(int index, int viewId) {
		GestureController r = new GestureController(mHostActivity, index,
				getResources());
		r.attachToView((ViewGroup) findViewById(viewId));
	}
	

}
