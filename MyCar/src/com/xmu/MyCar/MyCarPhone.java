package com.xmu.MyCar;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyCarPhone extends BaseActivity implements OnClickListener {
	static final String TAG = "MyCarPhone";
	/** Called when the activity is first created. */
	OutputController mOutputController;
	TextView mButtonLabel;
	TextView mG_sensorLabel;
	TextView mVoiceLabel;
	TextView mGestureLabel;
	TextView mGestureLabel_1;
	
	LinearLayout mButtonContainer;
	LinearLayout mG_sensorContainer;
	LinearLayout mVoiceContainer;
	LinearLayout mGestureContainer;
	LinearLayout mGestureContainer_1;
	
	Drawable mFocusedTabImage;
	Drawable mNormalTabImage;
	
	int showButtonContainer  =1;
	int showG_sensorContainer=2;
	int showVoiceContainer   =3;
	int showGestureContainer_1   =4;
	int showGestureContainer   =5;
	
	@Override
	protected void hideControls() {
		super.hideControls();
		mOutputController = null;
	}

	public void onCreate(Bundle savedInstanceState) {
		mFocusedTabImage = getResources().getDrawable(
				R.drawable.tab_focused_holo_dark);
		mNormalTabImage = getResources().getDrawable(
				R.drawable.tab_normal_holo_dark);
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}
	
	protected void showControls() {
		
		super.showControls();

		mOutputController = new OutputController(this, false);
		mOutputController.accessoryAttached();
		
		mButtonLabel = (TextView) findViewById(R.id.buttonLabel);
		mG_sensorLabel = (TextView) findViewById(R.id.G_sensorLabel);
		mVoiceLabel = (TextView) findViewById(R.id.VoiceLabel);
		mGestureLabel_1 = (TextView) findViewById(R.id.GestureLabel1);
		mGestureLabel = (TextView) findViewById(R.id.GestureLabel);
		
		
		mButtonContainer = (LinearLayout) findViewById(R.id.ButtonContainer);
		mG_sensorContainer = (LinearLayout) findViewById(R.id.G_sensorContainer);
		mVoiceContainer = (LinearLayout) findViewById(R.id.VoiceContainer);
		mGestureContainer_1 = (LinearLayout) findViewById(R.id.GestureContainer1);
		mGestureContainer = (LinearLayout) findViewById(R.id.GestureContainer);
		
		
		mButtonLabel.setOnClickListener(this);
		mG_sensorLabel.setOnClickListener(this);
		mVoiceLabel.setOnClickListener(this);
		mGestureLabel_1.setOnClickListener(this);
		mGestureLabel.setOnClickListener(this);
		
		
		showTabContents(showButtonContainer);
		
		Log.v(TAG, "showControls");
	}
	
	void showTabContents(int showOutput) {
		if (showOutput==showButtonContainer) {
			mButtonContainer.setVisibility(View.VISIBLE);
			mButtonLabel.setBackgroundDrawable(mFocusedTabImage);
			mG_sensorContainer.setVisibility(View.GONE);
			mG_sensorLabel.setBackgroundDrawable(mNormalTabImage);
			mVoiceContainer.setVisibility(View.GONE);
			mVoiceLabel.setBackgroundDrawable(mNormalTabImage);
			mGestureContainer.setVisibility(View.GONE);
			mGestureLabel.setBackgroundDrawable(mNormalTabImage);
			mGestureContainer_1.setVisibility(View.GONE);
			mGestureLabel_1.setBackgroundDrawable(mNormalTabImage);
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//…Ë÷√ ˙∆¡œ‘ æ
		} else if(showOutput==showG_sensorContainer){
			mButtonContainer.setVisibility(View.GONE);
			mButtonLabel.setBackgroundDrawable(mNormalTabImage);
			mG_sensorContainer.setVisibility(View.VISIBLE);
			mG_sensorLabel.setBackgroundDrawable(mFocusedTabImage);
			mVoiceContainer.setVisibility(View.GONE);
			mVoiceLabel.setBackgroundDrawable(mNormalTabImage);
			mGestureContainer.setVisibility(View.GONE);
			mGestureLabel.setBackgroundDrawable(mNormalTabImage);
			mGestureContainer_1.setVisibility(View.GONE);
			mGestureLabel_1.setBackgroundDrawable(mNormalTabImage);
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//…Ë÷√∫·∆¡œ‘ æ
		} else if(showOutput==showVoiceContainer){
			mButtonContainer.setVisibility(View.GONE);
			mButtonLabel.setBackgroundDrawable(mNormalTabImage);
			mG_sensorContainer.setVisibility(View.GONE);
			mG_sensorLabel.setBackgroundDrawable(mNormalTabImage);
			mVoiceContainer.setVisibility(View.VISIBLE);
			mVoiceLabel.setBackgroundDrawable(mFocusedTabImage);
			mGestureContainer.setVisibility(View.GONE);
			mGestureLabel.setBackgroundDrawable(mNormalTabImage);
			mGestureContainer_1.setVisibility(View.GONE);
			mGestureLabel_1.setBackgroundDrawable(mNormalTabImage);
		} else if(showOutput==showGestureContainer_1){
			mButtonContainer.setVisibility(View.GONE);
			mButtonLabel.setBackgroundDrawable(mNormalTabImage);
			mG_sensorContainer.setVisibility(View.GONE);
			mG_sensorLabel.setBackgroundDrawable(mNormalTabImage);
			mVoiceContainer.setVisibility(View.GONE);
			mVoiceLabel.setBackgroundDrawable(mNormalTabImage);
			mGestureContainer_1.setVisibility(View.VISIBLE);
			mGestureLabel_1.setBackgroundDrawable(mFocusedTabImage);
			mGestureContainer.setVisibility(View.GONE);
			mGestureLabel.setBackgroundDrawable(mNormalTabImage);
		}else if(showOutput==showGestureContainer){
			mButtonContainer.setVisibility(View.GONE);
			mButtonLabel.setBackgroundDrawable(mNormalTabImage);
			mG_sensorContainer.setVisibility(View.GONE);
			mG_sensorLabel.setBackgroundDrawable(mNormalTabImage);
			mVoiceContainer.setVisibility(View.GONE);
			mVoiceLabel.setBackgroundDrawable(mNormalTabImage);
			mGestureContainer_1.setVisibility(View.GONE);
			mGestureLabel_1.setBackgroundDrawable(mNormalTabImage);
			mGestureContainer.setVisibility(View.VISIBLE);
			mGestureLabel.setBackgroundDrawable(mFocusedTabImage);
		}
	}

	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
		case R.id.buttonLabel:
		
			showTabContents(showButtonContainer);
			break;

		case R.id.G_sensorLabel:
		
			showTabContents(showG_sensorContainer);
			break;
			
		case R.id.VoiceLabel:
		
			showTabContents(showVoiceContainer);
			break;
			
		case R.id.GestureLabel:
		
			showTabContents(showGestureContainer);
			
			break;
		case R.id.GestureLabel1:
			
			showTabContents(showGestureContainer_1);
			
			break;
		}
	}

}