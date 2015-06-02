package com.martaocio.farmergoody;

import java.sql.PreparedStatement;
import java.util.concurrent.TimeUnit;

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

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.martaocio.farmergoody.asynctasks.LoadGoogleAchievementTask;
import com.martaocio.farmergoody.domain.AchievementBox;
import com.martaocio.farmergoody.domain.GameSession;
import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.fragments.FatJackAdvertFragment;
import com.martaocio.farmergoody.fragments.ShareGoogleFragment;
import com.martaocio.farmergoody.fragments.SignInGoogleFragment;
import com.martaocio.farmergoody.fragments.GooglePlayServicesFragment;
import com.martaocio.farmergoody.providers.AchievementHelper;


public class MainGameActivity extends BaseGameActivity implements GooglePlayServicesFragment.Listener, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {

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

	private static final String TAG = "GoogleSignIn";

	private boolean shouldShowAdvert = false;
	FrameLayout fatJackPanelAdvert;
	FatJackAdvertFragment fatjackAdvertFragment;
	SignInGoogleFragment signInGoogleFragment;
	ShareGoogleFragment shareGoogleFragment;
	GooglePlayServicesFragment googlePlayServiceFragment;

	// Client used to interact with Google APIs
	private GoogleApiClient mGoogleApiClient;

	// Are we currently resolving a connection failure?
	private boolean mResolvingConnectionFailure = false;

	// Has the user clicked the sign-in button?
	private boolean mSignInClicked = false;

	// Automatically start the sign-in flow when the Activity starts
	private boolean mAutoStartSignInFlow = true;

	// request codes we use when invoking an external activity
	private static final int RC_RESOLVE = 5000;
	private static final int RC_UNUSED = 5001;
	private static final int RC_SIGN_IN = 9001;

	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);

		buildGoogleApiClient();

		signInGoogleFragment = new SignInGoogleFragment();
		
		new LoadGoogleAchievementTask().execute(this);
		UserState.getInstance().loadAchievementBox(AchievementHelper.getInstance(this).getAchievementBox());
		
		// signInGoogleFragment.setListener(this);

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
		// hideFatJackAdvert();
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
		// hideFatJackAdvert();
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
		// showFatJackAdvert();
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
		getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
				.add(CONTENT_VIEW_ID, fatjackAdvertFragment).commit();

	}

	private void buildGoogleApiClient() {
		GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN)
				.addApi(Games.API).addScope(Games.SCOPE_GAMES);
		mGoogleApiClient = builder.build();
	}

	public void addSignInGoogleButton() {
		signInGoogleFragment = new SignInGoogleFragment();
		// signInGoogleFragment.setListener(this);
		getFragmentManager().beginTransaction().add(CONTENT_VIEW_ID, signInGoogleFragment).addToBackStack(null).commit();
	}

	public void removeSignInGoogleButton() {
		if (signInGoogleFragment != null) {
			// getFragmentManager().beginTransaction().remove(signInGoogleFragment).commit();
			getFragmentManager().popBackStack();
		}
	}

	public void addGoogleServiceFragment() {
		googlePlayServiceFragment = new GooglePlayServicesFragment();
		googlePlayServiceFragment.updateUi(true);
		googlePlayServiceFragment.setListener(this);
		getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
				.add(CONTENT_VIEW_ID, googlePlayServiceFragment).addToBackStack(null).commit();
	}

	public void removeGoogleServiceFragment() {

		if (googlePlayServiceFragment != null) {
			// getFragmentManager().beginTransaction().remove(googlePlayServiceFragment).commit();
			getFragmentManager().popBackStack();
		}
	}

	public void hideFatJackAdvert() {

		getFragmentManager().beginTransaction().remove(fatjackAdvertFragment).commit();

	}

	public void addShareGoogleButton(GameSession gameSessionInfo) {
		shareGoogleFragment = new ShareGoogleFragment();
		shareGoogleFragment.setDataToShare(gameSessionInfo);
		getFragmentManager().beginTransaction().add(CONTENT_VIEW_ID, shareGoogleFragment).commit();
	}

	public void removeShareGoogleButton() {
		if (shareGoogleFragment != null) {
			getFragmentManager().beginTransaction().hide(shareGoogleFragment).commit();
		}
	}

	public boolean isSignedIn() {
		return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "Try connect onStart");
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "Try disconnect onStop");
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onShowAchievementsRequested() {
		Log.i(TAG, "Try show Achiviements if SignIN");
		if (isSignedIn()) {
			startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), RC_UNUSED);
		} else {
			BaseGameUtils.makeSimpleDialog(this, getString(R.string.achievements_not_available)).show();
		}
	}

	@Override
	public void onShowLeaderboardsRequested() {
		if (isSignedIn()) {
			startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), RC_UNUSED);
		} else {
			BaseGameUtils.makeSimpleDialog(this, getString(R.string.leaderboards_not_available)).show();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mResolvingConnectionFailure) {
			Log.i(TAG, "GiveUp onConnectionFailed");
			googlePlayServiceFragment.updateUi(false);
			return;
		}

		if (mSignInClicked || mAutoStartSignInFlow) {
			mAutoStartSignInFlow = false;
			mSignInClicked = false;
			mResolvingConnectionFailure = true;
			if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, result, RC_SIGN_IN, "Not able resolve connection")) {
				Log.i(TAG, "Resolve Connection failed");
				mResolvingConnectionFailure = false;
				googlePlayServiceFragment.updateUi(false);
			}
		}

		signInGoogleFragment.setShowSignInButton(true);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == RC_SIGN_IN) {
			mSignInClicked = false;
			mResolvingConnectionFailure = false;

			if (resultCode == RESULT_OK) {
				Log.i(TAG, "OnActivityResult : try to connect");
				mGoogleApiClient.connect();
			} else {
				Log.i(TAG, "OnActivityResult : show the result error dialog requestCOde" + requestCode + " resultcode: " + resultCode);
				BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.achievements_not_available);
			}
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// Set the greeting appropriately on main menu
		Log.i(TAG, "OnConnected : connected!!");
		Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
		String displayName;
		if (p == null) {
			// Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
			displayName = "???";
		} else {
			displayName = p.getDisplayName();
		}
		
		
		// mMainMenuFragment.setGreeting("Hello, " + displayName);

	}

	@Override
	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();

	}

	@Override
	public void onSignInButtonClicked() {
		Log.i(TAG, "onSignInButtonClicked : try to connect");
		mSignInClicked = true;
		mGoogleApiClient.connect();

	}

	@Override
	public void onSignOutButtonClicked() {
		mSignInClicked = false;
		mGoogleApiClient.disconnect();

	}

	@Override
	public void onEnteredScore(int score) {
		// TODO accept a score

	}

	public void unlockAchievement(int achievementId, String fallbackString) {
		if (isSignedIn()) {
			Games.Achievements.unlock(mGoogleApiClient, getString(achievementId));
		} else {
			Toast.makeText(this, getString(R.string.sample_achievement) + ": " + fallbackString, Toast.LENGTH_LONG).show();
		}
	}
	
	public void loadAchiviements(){
		if(isSignedIn()){
			Games.Achievements.load(mGoogleApiClient, true);
		}
	}
	
	

	public void submitLeaderBoard(long pointScore) {

		if (isSignedIn()) {
			Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_fatjack), pointScore);
		}

	}

	public void incrementAchievement(int achievementId) {
		if (isSignedIn()) {
			Games.Achievements.increment(mGoogleApiClient, getString(achievementId), 1);
		}
	}
	
	public void incrementAchievement(int achievementId, int numberSteps) {
		if (isSignedIn()) {
			Games.Achievements.increment(mGoogleApiClient, getString(achievementId), numberSteps);
		}
	}

	public void achievementToast(String achievement) {
		// Only show toast if not signed in. If signed in, the standard Google
		// Play
		// toasts will appear, so we don't need to show our own.
		if (!isSignedIn()) {
			Toast.makeText(this, getString(R.string.sample_achievement) + ": " + achievement, Toast.LENGTH_LONG).show();
		}
	}
	
	public GoogleApiClient getmGoogleApiClient() {
		return mGoogleApiClient;
	}

	

}
