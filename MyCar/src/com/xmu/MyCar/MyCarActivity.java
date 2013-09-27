/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xmu.MyCar;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;



public class MyCarActivity extends Activity implements Runnable {
	private static final String TAG = "MyCarActivity";
	
	private static final int REQUEST_CODE = 1;

	private static final String ACTION_USB_PERMISSION = "com.xmu.MyCar.action.USB_PERMISSION";

	private UsbManager mUsbManager;
	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;
	
	private Button Voice_btn;
	private TextView Voice_textView;
	
	UsbAccessory mAccessory;
	ParcelFileDescriptor mFileDescriptor;
	FileInputStream mInputStream;
	FileOutputStream mOutputStream;

	private static final int MESSAGE_SWITCH = 1;
	private static final int MESSAGE_LIGHT = 3;

	public static final byte PWM_OUT_COMMAND = 2;
	public static final byte DIGITAL_OUT_COMMAND = 3;
	

	protected class SwitchMsg {
		private byte sw;
		private byte state;

		public SwitchMsg(byte sw, byte state) {
			this.sw = sw;
			this.state = state;
		}

		public byte getSw() {
			return sw;
		}

		public byte getState() {
			return state;
		}
	}

	protected class LightMsg {
		private int light;

		public LightMsg(int light) {
			this.light = light;
		}

		public int getLight() {
			return light;
		}
	}

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {//Terminating communication with an accessory
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
				}
			}
			Log.v(TAG, "BroadcastReceiver mUsbReceiver");
		}
		
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mUsbManager = UsbManager.getInstance(this);
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);

		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		}

		setContentView(R.layout.main);
	
		enableControls(false);
		Log.v(TAG, "onCreate");	
	}
	

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (mAccessory != null) {
			return mAccessory;
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		Intent intent = getIntent();
		if (mInputStream != null && mOutputStream != null) {
			return;
		}

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
			/////////////////////
			
		} else {
			Log.d(TAG, "mAccessory is null");
		}
		Log.v(TAG, "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		//closeAccessory();
	
		Log.v(TAG, "onPause");
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	private void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, this, "MyCar");
			thread.start();
			Log.d(TAG, "accessory opened");
			enableControls(true);
		} else {
			Log.d(TAG, "accessory open fail");
		}
	}

	private void closeAccessory() {
		enableControls(false);
		Log.v(TAG, "closeAccessory");
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}

	protected void enableControls(boolean enable) {
	}

	private int composeInt(byte hi, byte lo) {
		int val = (int) hi & 0xff;
		val *= 256;
		val += (int) lo & 0xff;
		return val;
	}

	public void run() {
		int ret = 0;
		byte[] buffer = new byte[16384];
		int i;

		while (ret >= 0) {
			try {
				ret = mInputStream.read(buffer);
			} catch (IOException e) {
				break;
			}

			i = 0;
			while (i < ret) {
				int len = ret - i;

				switch (buffer[i]) {
				case 0x1:
					if (len >= 3) {
						Message m = Message.obtain(mHandler, MESSAGE_SWITCH);
						m.obj = new SwitchMsg(buffer[i + 1], buffer[i + 2]);
						mHandler.sendMessage(m);
					}
					i += 3;
					break;

				case 0x5:
					if (len >= 3) {
						Message m = Message.obtain(mHandler, MESSAGE_LIGHT);
						m.obj = new LightMsg(composeInt(buffer[i + 1],
								buffer[i + 2]));
						mHandler.sendMessage(m);
					}
					i += 3;
					break;

				default:
					Log.d(TAG, "unknown msg: " + buffer[i]);
					i = len;
					break;
				}
			}

		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SWITCH:
				SwitchMsg o = (SwitchMsg) msg.obj;
				handleSwitchMessage(o);
				break;

			case MESSAGE_LIGHT:
				LightMsg l = (LightMsg) msg.obj;
				handleLightMessage(l);
				break;

			}
		}
	};

	public void sendCommand(byte command, byte target, int value) {
		byte[] buffer = new byte[3];
		if (value > 255)
			value = 255;

		buffer[0] = command;
		buffer[1] = target;
		buffer[2] = (byte) value;
		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
			}
		}
	}

	protected void handleLightMessage(LightMsg l) {
	}

	protected void handleSwitchMessage(SwitchMsg o) {
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//回调获取从谷歌得到的数据
		 
		if(requestCode==REQUEST_CODE&&resultCode==RESULT_OK)
		{
			
			List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			//listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list)); //把数据显示在listview中
			Log.v(TAG, "onActivityResult");
			Voice_textView = (TextView)findViewById(R.id.textView1);
			for(int i = 0; i < list.size(); i++)
			{
				String array = list.get(i);
				Log.v("Yuyin", "*******************"+array);
				String action1 = "前进",action2 = "后退",action3 = "左转",action4 = "右转",action5 = "停止";
				
				if(array.equals(action1)){
					Log.v("Yuyin", "*******qianjin************");
					sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,(byte)0, (byte)1);//	
					Voice_textView.setText(action1);	
					break;
				}else if(array.equals(action2)){
					sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,(byte)0, (byte)2);
					Voice_textView.setText(action2);
					break;
				}else if(array.equals(action3)){
					sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,(byte)0, (byte)6);
					Voice_textView.setText(action3);
					break;
				}else if(array.equals(action4)){
					sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,(byte)0, (byte)7);
					Voice_textView.setText(action4);
					break;
				}else if(array.equals(action5)){
					sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,(byte)0, (byte)5);
					Voice_textView.setText(action5);
					break;
				}
				
			}

		}
		Voice_btn = (Button) findViewById(R.id.Voice);;
		Voice_btn.setBackgroundResource(R.drawable.voice_control_1);
		super.onActivityResult(requestCode, resultCode, data);
	}
}
