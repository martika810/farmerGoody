package com.martaocio.farmergoody;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.app.Activity;
import android.content.Context;

public class MainGameActivity extends BaseGameActivity {
	
	private BoundCamera camera;// camera that allow us to set the bounds
	private float WIDTH =800; //that s a default resolution
	private float HEIGTH=480;
	
	@Override
	public Engine onCreateEngine(EngineOptions engineOptions){
		return new LimitedFPSEngine(engineOptions,60);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		
		camera=new BoundCamera(0, 0, WIDTH,HEIGTH);
		//that means the engine will updating the given screen orientation,landscape mode in this example
		// third parameter is the resolution policy
		EngineOptions engineOptions =new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
		//FillResolutionPolicy makes the game fit the screen so it will streched if necesary
		//instead of leaving the black bars
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		ResourceManager.prepareManager(getEngine(),this, camera, getVertexBufferObjectManager(),getSystemService(Context.VIBRATOR_SERVICE));
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// TODO Auto-generated method stub
		SceneManager.getInstance().createMainMenu(pOnCreateSceneCallback);
		
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		pOnPopulateSceneCallback.onPopulateSceneFinished();
		
	}

	
}
