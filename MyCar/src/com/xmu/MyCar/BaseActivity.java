package com.xmu.MyCar;




import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivity extends MyCarActivity {////////////////////////////////
	private static final String TAG = "BaseActivity";
	private static final int REQUEST_CODE = 1;
	private InputController mInputController;


	public BaseActivity() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	         
		if (mAccessory != null) {
			showControls();
		} else {
			hideControls();
		}
		Log.v(TAG, "onCreate");		
	
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Simulate");
		menu.add("About");
		menu.add("Quit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle() == "Simulate") {
			showControls();
		} else if (item.getTitle() == "Quit") {
			//finish();
			//System.exit(0);
			isFinish();
		}else if (item.getTitle() == "About") {
			showAbout();
		} 
		return true;
	}

	protected void enableControls(boolean enable) {
		if (enable) {
			showControls();
		} else {
			hideControls();
		}
		Log.v(TAG, "enableControls");
	}

	protected void hideControls() {
		setContentView(R.layout.no_device);
		mInputController = null;
	}

	protected void showControls() {
	
		setContentView(R.layout.main);

		mInputController = new InputController(this);
		mInputController.accessoryAttached();
	}
/*
	protected void handleLightMessage(LightMsg l) {
		if (mInputController != null) {
			mInputController.setLightValue(l.getLight());
		}
	}
*/
	protected void handleSwitchMessage(SwitchMsg o) {
		if (mInputController != null) {
			byte sw = o.getSw();
			if (sw >= 0 && sw < 4) {
				mInputController.switchStateChanged(sw, o.getState() != 0);
			}
		}
	}
	
	protected void showAbout() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.app_about)
		.setMessage(R.string.app_about_msg)
		.setPositiveButton(R.string.str_ok,
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub	
					}
				}
		).show();
	}
	protected void isFinish(){
		new AlertDialog.Builder(this)
		.setTitle(R.string.app_exit)
		.setMessage(R.string.app_exit_msg)
		.setNegativeButton(R.string.str_no,
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				})
		.setPositiveButton(R.string.str_yes,
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
						System.exit(0);
					}
				})
		.show();
	}
	
	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//回调获取从谷歌得到的数据
		 
		if(requestCode==REQUEST_CODE&&resultCode==RESULT_OK)
		{
			
			List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			//listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list)); //把数据显示在listview中
			Log.v(TAG, "onActivityResult");
			for(int i = 0; i < list.size(); i++)
			{
				String array = list.get(i);
				Log.v("Yuyin", "*******************"+array);
				String action = "前进";
				
				if(array.equals(action)){
					Log.v("Yuyin", "*******qianjin************");
					sendCommand(MyCarActivity.DIGITAL_OUT_COMMAND,(byte)0, (byte)1);
							
					break;
				}
				
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}*/
	//protected void changeControls() {
		//setContentView(R.layout.main);
		//mInputController = new InputController(this);
		//mInputController.accessoryAttached();
		/*
		Intent intent = new Intent();
		intent.setClass(BaseActivity.this,G_sensorActivity.class );
		//BaseActivity.this.startActivity(intent);
		startActivity(intent);
		//BaseActivity.this.finish();
		*/
		//mG_sensorController = new G_sensorController(this);
		//mG_sensorController.accessoryAttached();
		
		
	//}
	

	
}