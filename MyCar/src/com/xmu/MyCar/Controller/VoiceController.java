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
 * ��������.
 * <p>ʵ�ֵ�Ч����<br>
 * <p>���ȼ�������豸�Ƿ���á�<br>
 * <p>�����Ļ�����ùȸ����������ڻص�����onActivityResult�з��������ȶԽ�� ��������ָ�<br>
 * <p> �ȸ������Ļص�����onActivityResult()��MyCarActivity���о���ʵ��<br>
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
        	Voice_btn.setText("��ǰ����ʶ���豸������..."); 
        	Voice_btn.setBackgroundResource(R.drawable.voice_control_2);
        }
		
	}
	
	class Voice_btnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v(TAG, "btnListener");
			// �����ֻ����õ�����ʶ����
			mActivity.sendCommand(MyCarActivity.PWM_OUT_COMMAND,
					mCommandTarget, (byte)220);//v=0-255 
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  //����Ϊ��ǰ�ֻ�����������
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "����ʶ��");//��������ʶ�����������Ҫ��ʾ����ʾ
			//intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);//����������ѯ����������Ϊ1
			mActivity.startActivityForResult(intent,REQUEST_CODE);
			Voice_btn.setBackgroundResource(R.drawable.voice_control_2);
			Log.v(TAG, "start voice Recognizer");
			
	
		}
		
		
		
	} 
}
	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//�ص���ȡ�ӹȸ�õ�������
		 
		if(requestCode==REQUEST_CODE&&resultCode==RESULT_OK)
		{
			
			List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			//listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list)); //��������ʾ��listview��
			Log.v("Yuyin", "*******************");
			for(int i = 0; i < list.size(); i++)
			{
				String array = list.get(i);
				Log.v("Yuyin", "*******************"+array);
				String action = "ǰ��";
				
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
	public void registerBoradcastReceiver(){//ע��㲥    
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ACTION_NAME);    
		mActivity.registerReceiver(mBroadcastReceiver, myIntentFilter);
		System.out.println("�㲥ע��");
		} 
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String str = intent.getStringExtra("mycommand");
			if(action.equals(ACTION_NAME)){
				Log.v("*******VoiceControl*********", str);
			
				//Toast.makeText(BroadcastDemoActivity.this, "����action�������Ӧ�Ĺ㲥", 200).show();
				}
			}
	};*/
	
	
	/*
	@Override
	protected Dialog onCreateDialog(int id) {
		RecognizerDialog recognizerDialog = new RecognizerDialog(
				this, "appid=4e79f83e");
		// �����appidӦ��д�ӿƴ�Ѷ�����뵽��appid��Ϊ���ܣ���123467�����ˣ����ܱ�֤��123467�ܸ�����
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















