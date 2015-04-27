package com.martaocio.farmergoody;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class RockSprite extends Sprite {

	private final RockSprite _rock;
	private BoundCamera camera;
	private BaseScene parentScene;
	private Body body;

	private int id;

	public RockSprite(ITextureRegion texture, VertexBufferObjectManager vbom, BoundCamera camera,BaseScene parentScene) {
		super(0,0, texture, vbom);
		this.parentScene=parentScene;
		this.camera=camera;
		_rock = this;
//		Line bodyLine = new Line(startX, startY, endX, endY, vbom);
//		bodyLine.setLineWidth(RockPool.LINE_BODY_WIDTH);
//		bodyLine.setVisible(false);
		createPhysics(camera);
		

	}

	private void createPhysics(final BoundCamera camera) {
//		this.physicsWorld=physicsWorld;
//		
//		FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(0.0f, 0.5f, 0.5f);
//		body = PhysicsFactory.createLineBody(physicsWorld, bodyLine, objectFixtureDef);
//		body.setUserData(SpriteTag.LINE);
		
		parentScene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(final float pSecondsElapsed) {
			//	_rock.getParent().onUpdate(pSecondsElapsed);
				boolean isRockOutOfScreen=!camera.isRectangularShapeVisible(_rock);
				if(isRockOutOfScreen && _rock.isVisible()){
					_rock.die();
					if(RockPool.getInstance().getUnrecycledItemCount()>0){
						RockPool.getInstance().recyclePoolItem(_rock);
					}
					 
				 }
				

			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
		});

		
		
	}
	
	public void reset(){
		 this.setVisible(true);
	     this.setIgnoreUpdate(false);
	  //   body.setActive(true);
	}
	
	public void setBody(Body body){
		this.body=body;
	}

	
	public void die() {
        Log.d("bulletDie", "See you in hell!");
        if (this.isVisible()) {
            this.setVisible(false);
            this.setIgnoreUpdate(true);
            this.detachSelf();
            
           
        }

    }
}
