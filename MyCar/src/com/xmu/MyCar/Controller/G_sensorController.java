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
 * 手机重力加速传感器控制.
 * <p>实现的效果：<br>
 * <p>按住手机屏幕 ，根据重力传感器的坐标值变化，发送小车行进指令<br>
 * <p>松开手机屏幕 ，发送小车停止指令<br>
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
	
	
	// 感应器管理器 
    private SensorManager sensorMgr; 
      
    // 得到加速感应器 
    Sensor mSensor; 
      
    // 定义各坐标轴上的重力加速度 
    private float G_x, G_y, G_z;

	 
	
	public G_sensorController(MyCarActivity activity, int digitalOutIndexNumber,
			Resources res) {
		mActivity = activity;
		mCommandTarget = (byte) (digitalOutIndexNumber - 1);
		
		 // 得到当前手机传感器管理对象 
        sensorMgr = (SensorManager) mActivity.getSystemService(SENSOR_SERVICE); 
          
        // 加速重力感应对象 
        mSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
          
	
        // 实例化一个监听器 
        SensorEventListener lsn = new SensorEventListener() {
        	// 实现接口的方法 
    	public void onSensorChanged(SensorEvent e) { 
    		// 得到各轴上的重力加速度 
    		G_x = e.values[SensorManager.DATA_X]; 
    		G_y = e.values[SensorManager.DATA_Y]; 
    		G_z = e.values[SensorManager.DATA_Z];
        	}
    	public void onAccuracyChanged(Sensor s, int accuracy) {
    		}
    
        };
        // 注册listener，第三个参数是检测的精确度
        sensorMgr.registerListener(lsn, mSensor, SensorManager.SENSOR_DELAY_GAME);
		

	}
	


	public void attachToView(ViewGroup targetView) {
	
		G_sensor_btn = (Button) targetView.getChildAt(0);
		G_sensor_btn.setOnTouchListener(G_sensorTouchListener);
		//G_sensor_btn.setText("按住屏幕让小车动起来...");
	}
	
	

	
	private OnTouchListener G_sensorTouchListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			

			switch (event.getAction()) {
			
				case MotionEvent.ACTION_DOWN:
				{	
					//按住事件发生后执行代码的区域
					threadDisable = false;
					//G_sensor_btn.setText("Run...");
					G_sensor_btn.setBackgroundResource(R.drawable.g_sensor_background_2);
					new Thread( new Runnable() {

			            @Override
			            public void run() {
			            	while ( ! threadDisable){////////////////////
			                	try {
			                        Thread.sleep( 100 );//暂停0.1秒
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
					//松开事件发生后执行代码的区域
				threadDisable = true ;
				//G_sensor_btn.setText("按住屏幕让小车动起来...");
				G_sensor_btn.setBackgroundResource(R.drawable.g_sensor_background_1);
				mActivity.sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,
						mCommandTarget, value_stop);
					break;
				}
				
				case MotionEvent.ACTION_MOVE:
				{
					//移动事件发生后执行代码的区域
					break;
				}
				
				
				default:
					
					break;
				}
				return false;
		}

	};


}












