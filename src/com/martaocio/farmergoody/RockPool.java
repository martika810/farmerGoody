package com.martaocio.farmergoody;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

import com.martaocio.farmergoody.customsprites.RockSprite;
import com.martaocio.farmergoody.scenes.BaseScene;

import android.util.Log;

public class RockPool extends GenericPool<RockSprite> {
	private ITextureRegion mTextureRegion;
	private BaseScene parentScene;
	private VertexBufferObjectManager mVbom;
	private BoundCamera mCamera;
	public static float LINE_BODY_WIDTH=0.2f;
	private static RockPool INSTANCE=null;
	
	public static void prepareRockPool(ITextureRegion pTextureRegion,VertexBufferObjectManager vbom,BoundCamera camera,BaseScene parentScene){
		INSTANCE=new RockPool(pTextureRegion, vbom,camera,parentScene);
	}
	
	public static RockPool getInstance(){
		return INSTANCE;
	}

	private RockPool(ITextureRegion pTextureRegion,VertexBufferObjectManager vbom,BoundCamera camera,BaseScene parentScene) {
		if (pTextureRegion == null) {
			// Need to be able to create a Sprite so the Pool needs to have a
			// TextureRegion
			throw new IllegalArgumentException("The texture region must not be NULL");
		}
		mTextureRegion = pTextureRegion;
		this.parentScene=parentScene;
		mVbom=vbom;
		mCamera=camera;
	}

	@Override
	protected RockSprite onAllocatePoolItem() {
		return new RockSprite(mTextureRegion,mVbom,mCamera,parentScene);
	}
	
	/**
	  * Called when a Bullet is sent to the pool
	 */
	 @Override
	 protected void onHandleRecycleItem(final RockSprite pRock) {
		 Log.i("RockPool","Rock Sprite recycled");
		 pRock.setIgnoreUpdate(true);
		 pRock.setVisible(false);
	 }
	 
	 @Override
	 protected void onHandleObtainItem(final RockSprite pRock) {
	  pRock.reset();
	 }

}
