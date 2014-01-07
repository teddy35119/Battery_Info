package com.example.bettery_info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.R.string;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button imageIntentButton,bgButton;
	TextView percentTextView,voltTextView,currentTextView,tempTextView,chargeTextView,USBTextView;
	
	//Timer設置
	TimerTask infomation_updater = new TimerTask(){
		@Override
		public void run() {
			runOnUiThread(new Runnable(){
				@Override
				public void run() {
					displayBatteryInfomation(infoListener());    //
				}
			});
		}		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewinit();
		displayBatteryInfomation(infoListener());    //首次抓取數據 並顯示資訊
		Timer timer = new Timer();
		timer.schedule(infomation_updater, 1000,1000);
		imageIntentButton.setOnClickListener(ImageIntentButtonListener);
		bgButton.setOnClickListener(backgroundListener);
	}
		
	private void viewinit(){   //初始化介面
		percentTextView = (TextView)findViewById(R.id.textView_info_percent);
		voltTextView = (TextView)findViewById(R.id.textView_info_volt);
		currentTextView = (TextView)findViewById(R.id.textView_info_current);
		tempTextView = (TextView)findViewById(R.id.textView_info_temp);
		chargeTextView = (TextView)findViewById(R.id.textView_info_charge);
		USBTextView = (TextView)findViewById(R.id.textView_info_USB);
		imageIntentButton = (Button)findViewById(R.id.button_info_image);
		bgButton = (Button)findViewById(R.id.button_info_bg);
		if(isMyServiceRunning()){
            bgButton.setText("背景執行-關");
		}else{
            bgButton.setText("背景執行-開");
        }	
	}
	
	
	
	private void displayBatteryInfomation(HashMap<String,String> battery_infomation){
		double tempnum = Double.parseDouble(battery_infomation.get("temp"));
		tempnum/=10;
		if(tempnum<=30){
			tempTextView.setTextColor(Color.GREEN);
		}else if(tempnum>30&&tempnum<40){
			tempTextView.setTextColor(Color.YELLOW);
		}else{
			tempTextView.setTextColor(Color.RED);
		}
		double voltnum = Double.parseDouble(battery_infomation.get("volt"));
		voltnum/=1000;
		double currentnum = Double.parseDouble(battery_infomation.get("current"));
		currentnum/=1000;
		String chargenum="";
		String USBnum="";
		if("1".equals(battery_infomation.get("charge"))){
			chargenum="充電中";
		}else{
			chargenum="未充電";
		}
		if("1".equals(battery_infomation.get("USB"))){
			USBnum="未連接USB";
		}else{
			USBnum="USB連接中";
		}
		percentTextView.setText(battery_infomation.get("percent")+"%");
		voltTextView.setText(voltnum+"mV");
		currentTextView.setText(currentnum+"mA");
		tempTextView.setText(tempnum+"度");
		chargeTextView.setText(chargenum+"");
		USBTextView.setText(USBnum+"");
	}

	
	private HashMap<String,String> infoListener() {
		HashMap<String,String> map = new HashMap<String,String>();
		String file_name = "/sys/class/power_supply/battery/batt_attr_text";
		File file =  new File(file_name);
		if(file.exists())
		{
			FileReader f;
			try
			{
				f = new FileReader(file_name);
				BufferedReader br = new BufferedReader(f);
				String temp = "";
				temp = br.readLine();					
				while(temp != null)
				{
					temp = br.readLine();
					if(temp.indexOf("Percentage(%)") >= 0)
					{
						map.put("percent", temp.substring(15, temp.length()-1));
					}
					if(temp.indexOf("VBAT(uV)") >= 0)
					{
						map.put("volt", temp.substring(10, temp.length()-1));
					}
					if(temp.indexOf("IBAT(uA)") >= 0)
					{
						map.put("current", temp.substring(10, temp.length()-1));
					}
					if(temp.indexOf("BATT_TEMP(deci-celsius)") >= 0)
					{
						map.put("temp", temp.substring(25, temp.length()-1));
					}
					if(temp.indexOf("is_charging_enabled") >= 0)
					{
						map.put("charge", temp.substring(21, temp.length()-1));
					}
					if(temp.indexOf("USBIN_OV_IRQ") >= 0)
					{
						map.put("USB", temp.substring(14, temp.length()-1));
					}
				}
				
				//toggleGPS();
				//wifi.setWifiEnabled(true);
				//mBluetoothAdapter.enable();
				
				br.close();
				f.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// percentTextView.setText("File doesn't exist.");
			// dont find file do
		}
		return map;
		
	}
	
	View.OnClickListener ImageIntentButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent imageIntent = new Intent();
			imageIntent.setClass(MainActivity.this, ImageActivity.class);
			startActivity(imageIntent);
		}
	};
	View.OnClickListener backgroundListener = new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
			if(isMyServiceRunning()){
				Intent intent = new Intent(MainActivity.this, LogService.class);
	            stopService(intent);
	            Log.d("MainActivity", "Stop Service");
	            bgButton.setText("背景執行-開");
	            Toast.makeText(getApplicationContext(), "背景執行已關閉", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(MainActivity.this, LogService.class);
	            startService(intent);
	            Log.d("MainActivity", "Start Service");
	            bgButton.setText("背景執行-關");
	            Toast.makeText(getApplicationContext(), "背景執行已開啟", Toast.LENGTH_SHORT).show();
			}	
		}
	};
	
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (LogService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
