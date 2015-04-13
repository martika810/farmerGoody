package com.martaocio.farmergoody;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

public class GoogleAdListener extends AdListener {
	
	private Context context;
	private AdView adView;
	
	public GoogleAdListener(Context context, AdView adView) {
		this.context = context;
		
		this.adView = adView; 
		this.adView.setVisibility(View.GONE);
	}
	
	@Override
	public void onAdLoaded() {
		
	
		this.adView.setVisibility(View.VISIBLE);
	}

}
