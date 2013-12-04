package com.example.bettery_info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	
	String percent = "",volt="",current="",tempTV="",charge="",USB="";
	double tempTVnum,voltnum,currentnum;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
		//連結Activity及xml之元件------------------------------------------------------------------------------------
		percentTextView = (TextView)findViewById(R.id.textView_info_percent);
		voltTextView = (TextView)findViewById(R.id.textView_info_volt);
		currentTextView = (TextView)findViewById(R.id.textView_info_current);
		tempTextView = (TextView)findViewById(R.id.textView_info_temp);
		chargeTextView = (TextView)findViewById(R.id.textView_info_charge);
		USBTextView = (TextView)findViewById(R.id.textView_info_USB);
		//-------------------------------------------------------------------------------------
		
		infoListener();    //首次抓取數據
		
		
		//開啟後 一秒更新一次數據-----------------------------------------------------------------------
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						infoListener();
					}
					
				});
			}		
		};
		timer.schedule(task, 1000,1000);	
		//------------------------------------------------------------------------
	}

	private void infoListener() {
		String file_name = "/sys/class/power_supply/battery/batt_attr_text";
		File file =  new File(file_name);
		if(file.exists())
		{
			FileReader f;
			try
			{
				f = new FileReader(file_name);
				BufferedReader br = new BufferedReader(f);
				String temp = "", Allline="";
				try
				{
					temp = br.readLine();					
					while(temp != null)
					{
						Allline += temp + "\n";
						temp = br.readLine();
						try
						{
							if(temp.indexOf("Percentage(%)") >= 0)
							{
								percent = temp.substring(15, temp.length()-1);
							}
							if(temp.indexOf("VBAT(uV)") >= 0)
							{
								volt = temp.substring(10, temp.length()-1);
								voltnum = Double.parseDouble(volt);
								voltnum=voltnum/1000;
							}
							if(temp.indexOf("IBAT(uA)") >= 0)
							{
								current = temp.substring(10, temp.length()-1);
								currentnum = Double.parseDouble(current);
								currentnum = currentnum/1000;
							}
							if(temp.indexOf("BATT_TEMP(deci-celsius)") >= 0)
							{
								tempTV = temp.substring(25, temp.length()-1);
								tempTVnum = Double.parseDouble(tempTV);
								tempTVnum = tempTVnum/10;
							}
							if(temp.indexOf("is_charging_enabled") >= 0)
							{
								charge = temp.substring(21, temp.length()-1);
								if("1".equals(charge)){
									charge = "充電中";
								}else{
									charge = "未充電";
								}
							}
							if(temp.indexOf("USBIN_OV_IRQ") >= 0)
							{
								USB = temp.substring(14, temp.length()-1);
								if("0".equals(USB)){
									USB="USB已連接";
								}else{
									USB="未連接USB";
								}
							}
						}
						catch (Exception e) {
							
						}					
					}
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				percentTextView.setText(percent+"%");
				voltTextView.setText(voltnum+"mV");
				currentTextView.setText(currentnum+"mA");
				tempTextView.setText(tempTVnum+"度");
				chargeTextView.setText(charge+"");
				USBTextView.setText(USB+"");
				//toggleGPS();
				//wifi.setWifiEnabled(true);
				//mBluetoothAdapter.enable();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			percentTextView.setText("File doesn't exist.");
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
