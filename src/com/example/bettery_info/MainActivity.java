package com.example.bettery_info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button resetButton;
	TextView percentTextView,voltTextView,currentTextView,tempTextView,chargeTextView,USBTextView;
	
	// String percent = "",volt="",current="",tempTV="",charge="",USB="";
	// double tempTVnum,voltnum,currentnum;
	
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
	}
	
	
	
	private void viewinit(){   //初始化介面
		percentTextView = (TextView)findViewById(R.id.textView_info_percent);
		voltTextView = (TextView)findViewById(R.id.textView_info_volt);
		currentTextView = (TextView)findViewById(R.id.textView_info_current);
		tempTextView = (TextView)findViewById(R.id.textView_info_temp);
		chargeTextView = (TextView)findViewById(R.id.textView_info_charge);
		USBTextView = (TextView)findViewById(R.id.textView_info_USB);
	}
	
	
	private void displayBatteryInfomation(HashMap<String,String> battery_infomation){
		percentTextView.setText(battery_infomation.get("percent")+"%");
		voltTextView.setText(battery_infomation.get("voltnum")+"mV");
		currentTextView.setText(battery_infomation.get("currentnum")+"mA");
		tempTextView.setText(battery_infomation.get("tempTVnum")+"度");
		chargeTextView.setText(battery_infomation.get("charge")+"");
		USBTextView.setText(battery_infomation.get("USB")+"");
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
				String temp = "", Allline=""; //what allline do?
				temp = br.readLine();					
				while(temp != null)
				{
					Allline += temp + "\n";
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
						map.put("mapTV", temp.substring(25, temp.length()-1));
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
			}catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// percentTextView.setText("File doesn't exist.");
			// dont find file do
		}
		return map;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
