package com.martaocio.farmergoody;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.martaocio.farmergoody.SceneManager.SceneType;



public class MainMenu extends BaseScene implements IOnMenuItemClickListener {
	
	private MenuScene menuChildScene;//menu that can have buttons
	private final int PLAY = 0;//to recognize when the play buttons is gonna be played
	private final int CONTINUE = 1;

	@Override
	public void createScene() {
		
		createMenuScene();
		//create background
		this.attachChild(new Sprite(0,0,resourceManager.mainMenuBackground,vbom){
			
			@Override
			protected void preDraw(GLState pGLState,Camera pCamera){
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither(); //the avoid pixalation
			}
		});
		
	//	resourceManager.themeMusic.play();
		
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	private void createMenuScene(){
		
		this.menuChildScene=new MenuScene(camera);
		this.menuChildScene.setPosition(0,0);	
		
		//create the menu buttons
		// when the button is clicked , it is scaled it to 1.2
		final IMenuItem playMenuItem=new ScaleMenuItemDecorator(new SpriteMenuItem(this.PLAY,resourceManager.playMenuButton,vbom),1.7f,1.5f);
		
		final IMenuItem continueMenuItem=new ScaleMenuItemDecorator(new SpriteMenuItem(this.CONTINUE,resourceManager.continueMenuButton,vbom),1.2f,1);
		
		this.menuChildScene.addMenuItem(playMenuItem);
		this.menuChildScene.addMenuItem(continueMenuItem);
		
		this.menuChildScene.buildAnimations();
		this.menuChildScene.setBackgroundEnabled(false);
		
		playMenuItem.setPosition(360,260);
		continueMenuItem.setPosition(650,370);
		this.menuChildScene.setOnMenuItemClickListener(this);
		
		//attach the play menu to the scene
		this.setChildScene(menuChildScene, false, true,true);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID()){
			
		case PLAY:
			//load game
			UserState.getInstance().clear();
			SceneManager.getInstance().createGameScene();
			return true;
		case CONTINUE:			
			SceneManager.getInstance().createGameScene();
			return true;
		default:
			return false;
		}
		
	}

}
