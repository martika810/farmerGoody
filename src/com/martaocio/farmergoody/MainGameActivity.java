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
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.plus.Plus;


public class MainGameActivity extends BaseGameActivity{

	public final static boolean FREE_ADS = false;
	private BoundCamera camera;// camera that allow us to set the bounds
	private float WIDTH = 800; // that s a default resolution
	private float HEIGTH = 480;

	// private AdView adView;
	FrameLayout frameLayout;
	InterstitialAd mInterstitialAd;
	private final static String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
	private final static String BANNER_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5407859542823392/6486370465";

	private static final int CONTENT_VIEW_ID = 10101010;
	private static final int CONTENT_SIGIN_GOOGLE_VIEW_ID = 10101011;
	private boolean shouldShowAdvert = false;
	FrameLayout fatJackPanelAdvert;
	FatJackAdvertFragment fatjackAdvertFragment;
	SignInGoogleFragment signInGoogleFragment;
	
	
	
	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		
	}
	

	

	@Override
	protected void onSetContentView() {
		// TODO Auto-generated method stub
		// super.onSetContentView();
		Log.i("onSetContentView", "enter onSetContentView");
		this.mRenderSurfaceView = new RenderSurfaceView(this);

		final LayoutParams layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		
		final LayoutParams wrapContentLayoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;

		final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams = new FrameLayout.LayoutParams(layoutParams);
		
		final android.widget.FrameLayout.LayoutParams signInGoogleViewLayoutParams = new FrameLayout.LayoutParams(wrapContentLayoutParams);

		// adView=new AdView(this);
		// adView.setAdSize(AdSize.FULL_BANNER);
		// adView.setAdUnitId(BANNER_AD_UNIT_ID);
		// adView.setAdListener(new GoogleAdListener(getApplicationContext(),
		// adView));
		if (!FREE_ADS) {
			mInterstitialAd = new InterstitialAd(this);
			mInterstitialAd.setAdUnitId(BANNER_INTERSTITIAL_AD_UNIT_ID);
			requestNewInterstitial();
			mInterstitialAd.setAdListener(new AdListener() {
				@Override
				public void onAdClosed() {
					requestNewInterstitial();

				}
			});
		}

		// AdRequest adRequest =new AdRequest.Builder().build();
		// adView.loadAd(adRequest);

		frameLayout = new FrameLayout(this);
		frameLayout.setId(CONTENT_VIEW_ID);
		final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);

		frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
		addSignInGoogleButton();
		
		// frameLayout.addView(this.adView,adViewLayoutParams);

		this.setContentView(frameLayout, frameLayoutLayoutParams);

		this.mRenderSurfaceView.setRenderer(this.mEngine, this);

	}

	

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
	protected void onPause() {

		super.onPause();
		//hideFatJackAdvert();
		// System.exit(0);
	}

	@Override
	public synchronized void onResumeGame() {
		// TODO Auto-generated method stub
		super.onResumeGame();
	}

	@Override
	public synchronized void onGameDestroyed() {
		// TODO Auto-generated method stub
		super.onGameDestroyed();
		//hideFatJackAdvert();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// if(keyCode ==KeyEvent.KEYCODE_BACK){
		// SceneManager.getInstance().get
		// }
		return super.onKeyDown(keyCode, event);
	}

	public boolean isShouldShowAdvert() {
		return shouldShowAdvert;
	}

	public void setShouldShowAdvert(boolean mShouldShowAdvert) {
		this.shouldShowAdvert = mShouldShowAdvert;
	}

	// public void showAdvert(boolean shouldShowAdvert){
	// if(shouldShowAdvert){
	// adView.setVisibility(View.VISIBLE);
	// }else{
	// adView.setVisibility(View.GONE);
	// }
	// }

	@Override
	public void onGameCreated() {
		super.onGameCreated();
		//showFatJackAdvert();
	}

	public void showAdvert(boolean shouldShowAdvert) {
		if (isNetworkAvailable() && mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		}
	}

	private void requestNewInterstitial() {
		AdRequest adRequest = new AdRequest.Builder().addTestDevice("xxxxx").build();
		mInterstitialAd.loadAd(adRequest);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public void showFatJackAdvert() {
		fatjackAdvertFragment = new FatJackAdvertFragment();
		getFragmentManager()
			.beginTransaction()
			.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
			.add(CONTENT_VIEW_ID, fatjackAdvertFragment)
			.commit();

	}
	
	public void addSignInGoogleButton(){
		signInGoogleFragment =new SignInGoogleFragment();
		getFragmentManager().beginTransaction().add(CONTENT_VIEW_ID,signInGoogleFragment).commit();
	}

	public void hideFatJackAdvert() {

		getFragmentManager().beginTransaction().remove(fatjackAdvertFragment).commit();

	}


	
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		if (requestCode == SignInGoogleFragment.RC_SIGN_IN) {
			signInGoogleFragment.setmIntentInProgress(false);

			if (responseCode!=ConnectionResult.SUCCESS && isNetworkAvailable()) {
				signInGoogleFragment.getmGoogleApiClient().connect();
			}
		}
	}


}
