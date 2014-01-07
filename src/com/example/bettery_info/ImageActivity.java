package com.example.bettery_info;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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
	LinearLayout linearLayout1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		viewinit();
		BackButton.setOnClickListener(BackButtonListener);
		PercentButton.setOnClickListener(ImageListener);	
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
		linearLayout1= (LinearLayout)findViewById(R.id.LinearLayout1);

	}
	 // 資料處理
    private XYMultipleSeriesDataset buildDatset(String[] titles, List<double[]> xValues,
            List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        int length = titles.length; // 折線數量
        for (int i = 0; i < length; i++) {
            // XYseries對象,用於提供繪製的點集合的資料
            XYSeries series = new XYSeries(titles[i]); // 依據每條線的名稱新增
            double[] xV = xValues.get(i); // 獲取第i條線的資料
            double[] yV = yValues.get(i);
            int seriesLength = xV.length; // 有幾個點

            for (int k = 0; k < seriesLength; k++) // 每條線裡有幾個點
            {
                series.add(xV[k], yV[k]);
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
			double[] x_arr = new double[] { 1,2,3};
			double[] y_arr = new double[] { 10,20,30};
			//read database
			Cursor c = InfoSaveLoader.loadInfo(ImageActivity.this);
			c.moveToFirst();
			String str = new String();
			do{
				/*if(v.getId()==R.id.button_image_percent){
					
				}*/
				str+="%: ";
				str+= c.getInt(c.getColumnIndexOrThrow(DBHelper.PERCENT));
				/*str+="   volt: ";
				str+= c.getInt(c.getColumnIndexOrThrow(DBHelper.VOLT));
				str+="   temp: ";
				str+= c.getInt(c.getColumnIndexOrThrow(DBHelper.TEMP));
				str+="   current: ";
				str+= c.getInt(c.getColumnIndexOrThrow(DBHelper.CURRENT));
				str+="   time: ";*/
				//time format
				/*int intTime= c.getInt(c.getColumnIndexOrThrow(DBHelper.TIME));
				Date date = new Date(intTime*1000L); 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); 
				sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
				String formattedDate = sdf.format(date);
				
				str+=formattedDate;
				str+= "\n";*/
			}while(c.moveToNext());
			
			//read database done
			//start
			String[] titles = new String[] { "折線1"}; // 定義折線的名稱
	        List<double[]> x = new ArrayList<double[]>(); // 點的x坐標
	        List<double[]> y = new ArrayList<double[]>(); // 點的y坐標
	        // 數值X,Y坐標值輸入
	        x.add(x_arr);
	        y.add(y_arr);
	        XYMultipleSeriesDataset dataset = buildDatset(titles, x, y); // 儲存座標值

	        int[] colors = new int[] { Color.YELLOW };// 折線的顏色
	        PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND }; // 折線點的形狀
	        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);

	        setChartSettings(renderer, "折線圖展示", "X軸名稱", "Y軸名稱", 0, 25, 0, 25, Color.BLACK);// 定義折線圖
	        View chart = ChartFactory.getLineChartView(ImageActivity.this, dataset, renderer);
	        
	        linearLayout1.addView(chart); 
	        
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

}
