package com.xmu.MyCar;

import android.content.res.Resources;
import android.view.View;

public abstract class AccessoryController {
	static final String TAG = "AccessoryController";
	protected MyCarActivity mHostActivity;

	public AccessoryController(MyCarActivity activity) {
		mHostActivity = activity;
	}

	protected View findViewById(int id) {
		return mHostActivity.findViewById(id);
	}

	protected Resources getResources() {
		return mHostActivity.getResources();
	}

	void accessoryAttached() {
		onAccesssoryAttached();
	}

	abstract protected void onAccesssoryAttached();

}