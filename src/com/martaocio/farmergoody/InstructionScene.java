package com.martaocio.farmergoody;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import com.martaocio.farmergoody.SceneManager.SceneType;

public class InstructionScene extends BaseScene {
	private Sprite instructionImage;
	private Sprite backBtn;

	@Override
	public void createScene() {
		createBackground();
		createBackButton();
		
		
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
		return SceneType.SCENE_INSTRUCTION;
	}

	@Override
	public void disposeScene() {
		instructionImage.detachSelf();
		instructionImage.dispose();
		this.detachSelf();
		this.dispose();
		
	}
	
	private void createBackground(){
		instructionImage=new Sprite(0,0,resourceManager.instructionBackground,vbom){
			@Override
			protected void preDraw(GLState pGLState,Camera pCamera){
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither(); // the avoid pixalation
			}
			
		};
		//splash.setScale(1.5f);
		//splash.setPosition(400,240);
		this.attachChild(instructionImage);
	}
	
	private void createBackButton(){
		backBtn=new Sprite(0,0,resourceManager.backInstructionBtnTexture,vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					SceneManager.getInstance().loadMainMenuFromInstruction(engine);
					
				}
				return true;
			};
		};
		backBtn.setPosition(15, 365);
		this.registerTouchArea(backBtn);
		this.attachChild(backBtn);
	}

}
