package com.example.bettery_info;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.chart.PointStyle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ImageActivity extends Activity {
	
	Button BackButton,PercentButton,VoltButton,CurrentButton,TempButton;
	Button plusButton,minusButton;
	LinearLayout linearLayout1;
	int image_status, get_howmany_dot;
	View imageView;
	
	//Timer設置
	TimerTask image_updater = new TimerTask(){
		@Override
		public void run() {
			runOnUiThread(new Runnable(){
				@Override
				public void run() {
					displayinfo();
				}
			});
		}		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		viewinit();
		BackButton.setOnClickListener(BackButtonListener);
		PercentButton.setOnClickListener(ImageListener);	
		VoltButton.setOnClickListener(ImageListener);	
		CurrentButton.setOnClickListener(ImageListener);	
		TempButton.setOnClickListener(ImageListener);
		plusButton.setOnClickListener(sizeListener);
		minusButton.setOnClickListener(sizeListener);
		image_status = R.id.button_image_percent;
		get_howmany_dot = 15;
		displayinfo();
		Timer timer = new Timer();
		timer.schedule(image_updater, 1000,1000);
    }

    // 定義折線圖名稱
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

    // 定義折線圖的格式
    private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            r.setFillPoints(fill);
            renderer.addSeriesRenderer(r); //將座標變成線加入圖中顯示
        }
        return renderer;
        /*
        tv.setText(category.get(i).getNAME());
        tv[i][i].setLayoutParams(new LinearLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
        cat_linear.addView(tv);
        */
		//end
	}
	protected void viewinit(){
		BackButton = (Button)findViewById(R.id.button_image_back);
		PercentButton = (Button)findViewById(R.id.button_image_percent);
		VoltButton = (Button)findViewById(R.id.button_image_volt);
		CurrentButton = (Button)findViewById(R.id.button_image_current);
		TempButton = (Button)findViewById(R.id.button_image_temp);
		plusButton = (Button)findViewById(R.id.button_image_plus);
		minusButton = (Button)findViewById(R.id.button_image_minus);
		linearLayout1= (LinearLayout)findViewById(R.id.LinearLayout1);
		
	}
	 // 資料處理
    private XYMultipleSeriesDataset buildDatset(String[] titles, List<ArrayList<Double>> xValues,
            List<ArrayList<Double>> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        int length = titles.length; // 折線數量
        for (int i = 0; i < length; i++) {
            // XYseries對象,用於提供繪製的點集合的資料
            XYSeries series = new XYSeries(titles[i]); // 依據每條線的名稱新增
            ArrayList<Double> xV = xValues.get(i); // 獲取第i條線的資料
            ArrayList<Double> yV = yValues.get(i);
            int seriesLength = xV.size(); // 有幾個點

            for (int k = 0; k < seriesLength; k++) // 每條線裡有幾個點
            {
                series.add(xV.get(k), yV.get(k));
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

	
	
	View.OnClickListener BackButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};
	View.OnClickListener ImageListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			image_status=v.getId();
	        displayinfo();
		}
	};
	
	View.OnClickListener sizeListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.button_image_plus && get_howmany_dot < 100){
				get_howmany_dot+=10;
			}else if(v.getId()==R.id.button_image_minus && get_howmany_dot > 10){
				get_howmany_dot-=10;
			}
	        displayinfo();
		}
	};
	
	
	private void displayinfo(){
		//remove first
		if(imageView!=null){
			linearLayout1.removeView(imageView);
			imageView=null;
		}
		//start
		ArrayList<Double> x_arr = new ArrayList<Double>();
		ArrayList<Double> y_arr = new ArrayList<Double>();
		Double y_max=0.0;
		Double y_min=-1.0;
		Double x_max=0.0;
		Double x_min=-1.0;
		//read database
		Cursor c = InfoSaveLoader.loadInfo(ImageActivity.this,get_howmany_dot);
		c.moveToFirst();
		do{
			Double y;
			Double x;
			if(image_status==R.id.button_image_percent){
				y= c.getDouble(c.getColumnIndexOrThrow(DBHelper.PERCENT));
				x= c.getDouble(c.getColumnIndexOrThrow(DBHelper.TIME));
			}else if(image_status==R.id.button_image_volt){
				y= c.getDouble(c.getColumnIndexOrThrow(DBHelper.VOLT));
				y/=1000;
				x=c.getDouble(c.getColumnIndexOrThrow(DBHelper.TIME));
				
			}else if(image_status==R.id.button_image_current){
				y= c.getDouble(c.getColumnIndexOrThrow(DBHelper.CURRENT));
				y/=1000;
				x=c.getDouble(c.getColumnIndexOrThrow(DBHelper.TIME));
				
			}else if(image_status==R.id.button_image_temp){
				y= c.getDouble(c.getColumnIndexOrThrow(DBHelper.TEMP));
				y/=10;
				x=c.getDouble(c.getColumnIndexOrThrow(DBHelper.TIME));
				
			}else{
				y = Double.valueOf(0);
				x = Double.valueOf(0);
			}
			
			
			if(x>x_max)x_max=x;
			if(x<x_min || x_min == -1)x_min=x;
			if(y>y_max)y_max=y;
			if(y<y_min || y_min == -1)y_min=y;
			
			x_arr.add(x);
			y_arr.add(y);
		}while(c.moveToNext());
		
		//read database done
		//start
		String[] titles; // 定義折線的名稱
		if(image_status==R.id.button_image_percent){
			titles = new String[] { "電池電量"};
			y_max=100.0;
			y_min=0.0;
		}else if(image_status==R.id.button_image_volt){
			titles = new String[] { "電池電壓"};
		}else if(image_status==R.id.button_image_current){
			titles = new String[] { "電池電流"};
		}else if(image_status==R.id.button_image_temp){
			titles = new String[] { "電池溫度"};
		}else{
			titles = new String[] { "出了點問題....抓不到拉:P"};
		}
		
        List<ArrayList<Double>> x = new ArrayList<ArrayList<Double>>(); // 點的x坐標
        List<ArrayList<Double>> y = new ArrayList<ArrayList<Double>>(); // 點的y坐標
        // 數值X,Y坐標值輸入
        x.add(x_arr);
        y.add(y_arr);
        XYMultipleSeriesDataset dataset = buildDatset(titles, x, y); // 儲存座標值

        int[] colors = new int[] { Color.YELLOW };// 折線的顏色
        PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND }; // 折線點的形狀
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);

        setChartSettings(renderer, "折線圖展示", "X軸名稱", "Y軸名稱", x_min, x_max, y_min, y_max, Color.BLACK);// 定義折線圖
        imageView = ChartFactory.getLineChartView(ImageActivity.this, dataset, renderer);
        
        linearLayout1.addView(imageView); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

}
