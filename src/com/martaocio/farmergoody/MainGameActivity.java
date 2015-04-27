package com.martaocio.farmergoody;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.ads.AdRequest;

public class MainGameActivity extends BaseGameActivity {
	
	//private AdView adView;
	InterstitialAd mInterstitialAd;
	private final static String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
	private final static String BANNER_INTERSTITIAL_AD_UNIT_ID ="ca-app-pub-3940256099942544/1033173712";
	private boolean shouldShowAdvert=false;

	@Override
	protected void onSetContentView() {
		// TODO Auto-generated method stub
		//super.onSetContentView();
		Log.i("onSetContentView", "enter onSetContentView");
		this.mRenderSurfaceView = new RenderSurfaceView(this);

		final LayoutParams layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.gravity=Gravity.CENTER;
		
		final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams=new FrameLayout.LayoutParams(layoutParams);
		
		//adView=new AdView(this);
		//adView.setAdSize(AdSize.FULL_BANNER);
		//adView.setAdUnitId(BANNER_AD_UNIT_ID);
		//adView.setAdListener(new GoogleAdListener(getApplicationContext(), adView));
		mInterstitialAd=new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(BANNER_INTERSTITIAL_AD_UNIT_ID);
		requestNewInterstitial();
		mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
               
            }
        });
		
		//AdRequest adRequest =new AdRequest.Builder().build();
		//adView.loadAd(adRequest);
		
		final FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.BOTTOM);
		
		
		final FrameLayout frameLayout=new FrameLayout(this);
		final FrameLayout.LayoutParams frameLayoutLayoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT,Gravity.CENTER);
		
		frameLayout.addView(this.mRenderSurfaceView,surfaceViewLayoutParams);
		//frameLayout.addView(this.adView,adViewLayoutParams);
		
		this.setContentView(frameLayout,frameLayoutLayoutParams);
		this.mRenderSurfaceView.setRenderer(this.mEngine,this);
	}

	private BoundCamera camera;// camera that allow us to set the bounds
	private float WIDTH = 800; // that s a default resolution
	private float HEIGTH = 480;

	@Override
	public Engine onCreateEngine(EngineOptions engineOptions) {
		return new LimitedFPSEngine(engineOptions, 60);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {

		camera = new BoundCamera(0, 0, WIDTH, HEIGTH);
		// that means the engine will updating the given screen
		// orientation,landscape mode in this example
		// third parameter is the resolution policy
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
		// FillResolutionPolicy makes the game fit the screen so it will
		// streched if necesary
		// instead of leaving the black bars
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		ResourceManager.prepareManager(getEngine(), this, camera, getVertexBufferObjectManager(),
				getSystemService(Context.VIBRATOR_SERVICE));
		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);

	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				SceneManager.getInstance().createMainMenu();

			}
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		// System.exit(0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// if(keyCode ==KeyEvent.KEYCODE_BACK){
		// SceneManager.getInstance().get
		// }
		return super.onKeyDown(keyCode, event);
	}
	
	public boolean isShouldShowAdvert(){
		return shouldShowAdvert;
	}
	
	public void setShouldShowAdvert(boolean mShouldShowAdvert){
		this.shouldShowAdvert=mShouldShowAdvert;
	}
	
//	public void showAdvert(boolean shouldShowAdvert){
//		if(shouldShowAdvert){
//			adView.setVisibility(View.VISIBLE);
//		}else{
//			adView.setVisibility(View.GONE);
//		}
//	}
	
	public void showAdvert(boolean shouldShowAdvert){
		if (isNetworkAvailable() && mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		}
	}
	
	private void requestNewInterstitial(){
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice("xxxxx")
			.build();
		mInterstitialAd.loadAd(adRequest);
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
