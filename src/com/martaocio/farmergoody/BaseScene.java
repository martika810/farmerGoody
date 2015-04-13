package com.martaocio.farmergoody;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.martaocio.farmergoody.SceneManager.SceneType;



import android.app.Activity;

public abstract class BaseScene extends Scene {
	
	protected Engine engine;
	protected MainGameActivity activity;
	protected ResourceManager resourceManager;
	protected VertexBufferObjectManager vbom;
	protected BoundCamera camera;

	public BaseScene(){
		
		this.resourceManager=ResourceManager.getInstance();
		
		this.engine=resourceManager.engine;
		this.activity=resourceManager.activity;
		this.vbom=resourceManager.vbom;
		this.camera=resourceManager.camera;
		
		createScene();
	}
	
	public abstract void createScene();
	
	public abstract void update();
	
	public abstract void onBackKeyPressed();
	
	//return the type of scene: menu or game
	public abstract SceneType getSceneType();
	
	public abstract void disposeScene();
}
