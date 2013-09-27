package com.xmu.MyCar.Controller;


import java.util.List;

import com.xmu.MyCar.MyCarActivity;
import com.xmu.MyCar.R;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * 语音控制.
 * <p>实现的效果：<br>
 * <p>首先检查语音设备是否可用。<br>
 * <p>点击屏幕，调用谷歌语音服务，在回调函数onActivityResult中返回语音比对结果 ，并发送指令。<br>
 * <p> 谷歌语音的回调函数onActivityResult()在MyCarActivity类中具体实现<br>
 * @author liyihuang
 */ 
public class VoiceController{// extends Activity
	private static final String TAG = "VoiceController";
	
	private final byte mCommandTarget;
	private MyCarActivity mActivity;
	//private final String ACTION_NAME = "sentMyCommand";
	private Button Voice_btn;

	public static final byte value_forward = 1;
	public static final byte value_backward = 2;
	public static final byte value_left = 3;
	public static final byte value_right = 4;
	public static final byte value_stop = 5;
	private static final int REQUEST_CODE = 1;
	

	public VoiceController (MyCarActivity activity, int digitalOutIndexNumber,
			Resources res) {
		mActivity = activity;
		mCommandTarget = (byte) (digitalOutIndexNumber - 1);
		//registerBoradcastReceiver();
	}

	
/**/
	public void attachToView(ViewGroup targetView) {

		Voice_btn = (Button) targetView.getChildAt(0);
		//Voice_btn.setOnClickListener(new Voice_btnListener());
		//listView = (ListView) targetView.getChildAt(1);
		
		PackageManager pm = mActivity.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if(list.size()!=0)
        {
        	Voice_btn.setOnClickListener(new Voice_btnListener());
        
        }else{
        	Voice_btn.setEnabled(false);
        	Voice_btn.setText("当前语音识别设备不可用..."); 
        	Voice_btn.setBackgroundResource(R.drawable.voice_control_2);
        }
		
	}
	
	class Voice_btnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v(TAG, "btnListener");
			// 启动手机内置的语言识别功能
			mActivity.sendCommand(MyCarActivity.PWM_OUT_COMMAND,
					mCommandTarget, (byte)220);//v=0-255 
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  //设置为当前手机的语言类型
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "语音识别");//出现语言识别界面上面需要显示的提示
			//intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);//语音搜索查询到的最多笔数为1
			mActivity.startActivityForResult(intent,REQUEST_CODE);
			Voice_btn.setBackgroundResource(R.drawable.voice_control_2);
			Log.v(TAG, "start voice Recognizer");
			
	
		}
		
		
		
	} 
}
	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//回调获取从谷歌得到的数据
		 
		if(requestCode==REQUEST_CODE&&resultCode==RESULT_OK)
		{
			
			List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			//listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list)); //把数据显示在listview中
			Log.v("Yuyin", "*******************");
			for(int i = 0; i < list.size(); i++)
			{
				String array = list.get(i);
				Log.v("Yuyin", "*******************"+array);
				String action = "前进";
				
				if(array.equals(action)){
					Log.v("Yuyin", "*******qianjin************");
					break;
				}
				
			}

			
		
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	*/
	
	/*
	public void registerBoradcastReceiver(){//注册广播    
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ACTION_NAME);    
		mActivity.registerReceiver(mBroadcastReceiver, myIntentFilter);
		System.out.println("广播注册");
		} 
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String str = intent.getStringExtra("mycommand");
			if(action.equals(ACTION_NAME)){
				Log.v("*******VoiceControl*********", str);
			
				//Toast.makeText(BroadcastDemoActivity.this, "处理action名字相对应的广播", 200).show();
				}
			}
	};*/
	
	
	/*
	@Override
	protected Dialog onCreateDialog(int id) {
		RecognizerDialog recognizerDialog = new RecognizerDialog(
				this, "appid=4e79f83e");
		// 这里的appid应该写从科大讯飞申请到的appid，为保密，用123467代替了，不能保证用123467能高运行
		recognizerDialog.setEngine("sms", null, null);
		recognizerDialog.setListener(new RecognizerDialogListener() {
			@Override
			public void onResults(ArrayList<RecognizerResult> results,
					boolean arg1) {
				StringBuffer result = new StringBuffer();
				for (RecognizerResult r : results) {
					result.append(r.text);
				}
				//editText.setText(result.toString());
				Log.v("******VoiceController", "result="+result.toString());
			}

			@Override
			public void onEnd(SpeechError arg0) {

			}
		});
		return recognizerDialog;
	}	
	*/















