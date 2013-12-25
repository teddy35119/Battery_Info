package com.example.bettery_info;

import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ImageActivity extends Activity {
	
	Button BackButton,PercentButton,VoltButton,CurrentButton,TempButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		viewinit();
		BackButton.setOnClickListener(BackButtonListener);
	}
	protected void viewinit(){
		BackButton = (Button)findViewById(R.id.button_image_back);
		PercentButton = (Button)findViewById(R.id.button_image_percent);
		VoltButton = (Button)findViewById(R.id.button_image_volt);
		CurrentButton = (Button)findViewById(R.id.button_image_current);
		TempButton = (Button)findViewById(R.id.button_image_temp);
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
            String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor) {
        renderer.setChartTitle(title); // 折線圖名稱
        renderer.setChartTitleTextSize(24); // 折線圖名稱字形大小
        renderer.setXTitle(xTitle); // X軸名稱
        renderer.setYTitle(yTitle); // Y軸名稱
        renderer.setXAxisMin(xMin); // X軸顯示最小值
        renderer.setXAxisMax(xMax); // X軸顯示最大值
        renderer.setXLabelsColor(Color.BLACK); // X軸線顏色
        renderer.setYAxisMin(yMin); // Y軸顯示最小值
        renderer.setYAxisMax(yMax); // Y軸顯示最大值
        renderer.setAxesColor(axesColor); // 設定坐標軸顏色
        renderer.setYLabelsColor(0, Color.BLACK); // Y軸線顏色
        renderer.setLabelsColor(Color.BLACK); // 設定標籤顏色
        renderer.setMarginsColor(Color.WHITE); // 設定背景顏色
        renderer.setShowGrid(true); // 設定格線
    }
	
	View.OnClickListener BackButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

}
