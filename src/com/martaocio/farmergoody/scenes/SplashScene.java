package com.martaocio.farmergoody.scenes;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.engine.camera.Camera;

import com.martaocio.farmergoody.SceneManager;
import com.martaocio.farmergoody.SceneManager.SceneType;

public class SplashScene extends BaseScene{
	private Sprite splash;

	@Override
	public void createScene() {
		splash=new Sprite(0,0,resourceManager.splashMenuBackground,vbom){
			@Override
			protected void preDraw(GLState pGLState,Camera pCamera){
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither(); // the avoid pixalation
			}
			
		};
		//splash.setScale(1.5f);
		//splash.setPosition(400,240);
		this.attachChild(splash);
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		
		return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene() {
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
		this.dispose();
		
	}

}
