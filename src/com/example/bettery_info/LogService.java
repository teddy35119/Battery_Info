package com.example.bettery_info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LogService extends Service {
	private Handler handler = new Handler();
	
	@Override
	public void onStart(Intent intent, int startId) {
		//HashMap<String, String> map = getInfo();
		//Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
		handler.postDelayed(logInfo, 10000);
		super.onStart(intent, startId);
	}
	@Override
	public void onDestroy(){
		handler.removeCallbacks(logInfo);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private Runnable logInfo = new Runnable(){
		@Override
		public void run() {
			//Log.i("time:", new Date(0).toString());
			HashMap<String,String> map = getInfo();
			InfoSaveLoader.insertInfo(LogService.this, Integer.parseInt(map.get("percent")), Integer.parseInt(map.get("volt")), 
					Integer.parseInt(map.get("current")), Integer.parseInt(map.get("temp")), (int) (System.currentTimeMillis() / 1000L));
			Log.d("LogService","Loged");
			handler.postDelayed(this, 10000);
		}
		
	};

	
	private HashMap<String,String> getInfo() {
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
	
}
