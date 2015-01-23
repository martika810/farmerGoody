package com.martaocio.farmergoody;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class Tomato extends AnimatedSprite {

	private Body body;

	public Body getBody() {
		return body;
	}

	public Tomato(float pX, float pY, float width, float height,
			VertexBufferObjectManager vbom, BoundCamera camera,
			PhysicsWorld physicWorld) {

		super(pX, pY, width, height,
				ResourceManager.getInstance().tomatoTexture, vbom);
		createPhysics(camera, physicWorld);
		
		
	}

	private void createPhysics(final BoundCamera camera,
			PhysicsWorld physicsWorld) {

		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		body.setUserData("tomato");
		body.setFixedRotation(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, true) {
			@Override
			public void onUpdate(float pSecondsElapsed) {

				super.onUpdate(pSecondsElapsed);

				camera.onUpdate(.1f);// to keep the camera centered when the
										// sprite is moving
				//getY()>=480
				if((getY()>=480 || getY()<0) && isVisible()){
					onDie();	
					//camera.setChaseEntity(null);
				}
				body.setLinearVelocity(new Vector2(body.getLinearVelocity().x,body.getLinearVelocity().y));
			}
		});
		
		
		

	}
	
	public abstract void onDie();
	
	public void setRunning(){
		
		if(!this.isAnimationRunning()){//if animation is not running
			
			final long[] TOMATO_ANIMATE=new long[]{100,100,100,100};//array to hold the animations
			animate(TOMATO_ANIMATE,0,3,true);
		}
	}
	
	public void smash(){
		
	
		this.stopAnimation();
			
		this.setCurrentTileIndex(4);
		
		//that stops the body moving
		Vector2 velocity=new Vector2(0,0);
			
		body.setLinearVelocity(velocity);
		
		//make the sprite fade
		this.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.registerEntityModifier(new AlphaModifier(1, 0, 255));
			
		
	}

}
