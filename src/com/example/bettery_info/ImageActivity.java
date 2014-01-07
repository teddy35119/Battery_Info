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
	View ImageViewImage;
	
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
		ImageViewImage = (View)findViewById(R.id.view_image);
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
