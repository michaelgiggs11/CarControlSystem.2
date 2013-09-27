 package com.xmu.MyCar.Controller;

import com.xmu.MyCar.MyCarActivity;
import com.xmu.MyCar.R;


import android.app.Activity;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * �ֻ��������ٴ���������.
 * <p>ʵ�ֵ�Ч����<br>
 * <p>��ס�ֻ���Ļ ����������������������ֵ�仯������С���н�ָ��<br>
 * <p>�ɿ��ֻ���Ļ ������С��ָֹͣ��<br>
 * @author liyihuang
 */ 
public class G_sensorController extends Activity{
	private static final String TAG = "G_sensorController";
	
	private final byte mCommandTarget;
	private MyCarActivity mActivity;
	private boolean threadDisable;

	private Button G_sensor_btn;
	public static final byte value_forward = 1;
	public static final byte value_backward = 2;
	public static final byte value_left = 3;
	public static final byte value_right = 4;
	public static final byte value_stop = 5;
	
	
	// ��Ӧ�������� 
    private SensorManager sensorMgr; 
      
    // �õ����ٸ�Ӧ�� 
    Sensor mSensor; 
      
    // ������������ϵ��������ٶ� 
    private float G_x, G_y, G_z;

	 
	
	public G_sensorController(MyCarActivity activity, int digitalOutIndexNumber,
			Resources res) {
		mActivity = activity;
		mCommandTarget = (byte) (digitalOutIndexNumber - 1);
		
		 // �õ���ǰ�ֻ�������������� 
        sensorMgr = (SensorManager) mActivity.getSystemService(SENSOR_SERVICE); 
          
        // ����������Ӧ���� 
        mSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
          
	
        // ʵ����һ�������� 
        SensorEventListener lsn = new SensorEventListener() {
        	// ʵ�ֽӿڵķ��� 
    	public void onSensorChanged(SensorEvent e) { 
    		// �õ������ϵ��������ٶ� 
    		G_x = e.values[SensorManager.DATA_X]; 
    		G_y = e.values[SensorManager.DATA_Y]; 
    		G_z = e.values[SensorManager.DATA_Z];
        	}
    	public void onAccuracyChanged(Sensor s, int accuracy) {
    		}
    
        };
        // ע��listener�������������Ǽ��ľ�ȷ��
        sensorMgr.registerListener(lsn, mSensor, SensorManager.SENSOR_DELAY_GAME);
		

	}
	


	public void attachToView(ViewGroup targetView) {
	
		G_sensor_btn = (Button) targetView.getChildAt(0);
		G_sensor_btn.setOnTouchListener(G_sensorTouchListener);
		//G_sensor_btn.setText("��ס��Ļ��С��������...");
	}
	
	

	
	private OnTouchListener G_sensorTouchListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			

			switch (event.getAction()) {
			
				case MotionEvent.ACTION_DOWN:
				{	
					//��ס�¼�������ִ�д��������
					threadDisable = false;
					//G_sensor_btn.setText("Run...");
					G_sensor_btn.setBackgroundResource(R.drawable.g_sensor_background_2);
					new Thread( new Runnable() {

			            @Override
			            public void run() {
			            	while ( ! threadDisable){////////////////////
			                	try {
			                        Thread.sleep( 100 );//��ͣ0.1��
			                    } catch (InterruptedException e) {
			                    }
			                	
			                	if(G_y<-2){
									mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
											mCommandTarget, value_left);
									byte v = 0;
									v = (byte) ((-G_y-2)*30);v=(byte)(v>=255?254:v);
									//if(G_y>=-2)v=(byte)75;else if(G_y<-2&&G_y>=-3)v=(byte)127;else if(G_y<-3&&G_y>=-10)v=(byte)-1;
									mActivity.sendCommand(MyCarActivity.PWM_OUT_COMMAND,
											mCommandTarget, v);
									Log.v(TAG, "*********left,v="+v);
									//break;
									continue;
								}else if(G_y>2){
									mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
											mCommandTarget, value_right);
									byte v;
									v = (byte) ((G_y-2)*30);v=(byte)(v>=255?254:v);
									mActivity.sendCommand(MyCarActivity.PWM_OUT_COMMAND,
											mCommandTarget, v);
									Log.v(TAG, "*********right,v="+v);
									continue;
								}
									
								if(G_x<-2){
									mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
											mCommandTarget, value_forward);
									byte v = 0;
									v = (byte) ((-G_x-2)*30);v=(byte)(v>=255?254:v);
									//if(G_x>=-2)v=(byte)75;else if(G_x<-2&&G_x>=-3)v=(byte)127;else if(G_x<-3&&G_x>=-10)v=(byte)-1;
									mActivity.sendCommand(MyCarActivity.PWM_OUT_COMMAND,
											mCommandTarget, v);
									Log.v(TAG,"*********forward,v="+v);
									continue;
								}else if(G_x>2){
									mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
											mCommandTarget, value_backward);
									byte v;
									v = (byte) ((G_x-2)*30);v=(byte)(v>=255?254:v);
									mActivity.sendCommand(MyCarActivity.PWM_OUT_COMMAND,
											mCommandTarget, v);
									Log.v(TAG,"*********backward,v="+v);
									continue;
								}
			                	
/*			                	
			                	Intent intent = new Intent();
								intent.setAction("com.xmu.MyCar.G_Service");
			                	mActivity.startService(intent);
			                	Log.v(TAG, "after startService "+threadDisable); 
			                	
								SharedPreferences sharedata = mActivity.getSharedPreferences("data", 0);   
								float G_x = sharedata.getFloat("G_x",0);
								float G_y = sharedata.getFloat("G_y",0);
								//float G_z = sharedata.getFloat("G_z",0);
								//Log.v("***G_xyz***", "x"+G_x+"y"+G_y+"z"+G_z); 
								
*/									
								
			                }/////////////////////////
			            	if(threadDisable==true){
								mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
										mCommandTarget, value_stop);
							}
			            }
			        }).start();

					break;
				}
				
				case MotionEvent.ACTION_UP:
				{Log.v(TAG,"OnTouchListener action_up");
					//�ɿ��¼�������ִ�д��������
				threadDisable = true ;
				//G_sensor_btn.setText("��ס��Ļ��С��������...");
				G_sensor_btn.setBackgroundResource(R.drawable.g_sensor_background_1);
				mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
						mCommandTarget, value_stop);
					break;
				}
				
				case MotionEvent.ACTION_MOVE:
				{
					//�ƶ��¼�������ִ�д��������
					break;
				}
				
				
				default:
					
					break;
				}
				return false;
		}

	};


}












