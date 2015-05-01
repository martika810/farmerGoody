package com.martaocio.farmergoody;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import com.martaocio.farmergoody.SceneManager.SceneType;

public class StoryScene extends BaseScene {
	
	private Sprite storyImage;
	private Sprite playBtn;

	@Override
	public void createScene() {
		createBackground();
		createPlayButton();
	

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
		return SceneType.SCENE_STORY;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}
	
	private void createBackground(){
		storyImage=new Sprite(0,0,resourceManager.storyBackground,vbom){
			@Override
			protected void preDraw(GLState pGLState,Camera pCamera){
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither(); // the avoid pixalation
			}
			
		};
		//splash.setScale(1.5f);
		//splash.setPosition(400,240);
		this.attachChild(storyImage);
	}
	
	private void createPlayButton(){
		
		playBtn=new Sprite(0,0,resourceManager.playStoryBtnTexture,vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					SceneManager.getInstance().createTrainingGameScene(engine);
					
				}
				return true;
			};
		};
		playBtn.setPosition(15, 365);
		this.registerTouchArea(playBtn);
		this.attachChild(playBtn);
		
		
	}

}
