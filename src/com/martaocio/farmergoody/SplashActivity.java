package com.martaocio.farmergoody;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGTH = 1000;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				Intent intentToLogin=new Intent(SplashActivity.this,MainGameActivity.class);
				startActivity(intentToLogin);
				finish();
				
			}
			
		},SPLASH_DISPLAY_LENGTH);
	}

}
