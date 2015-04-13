package com.martaocio.farmergoody;

import java.util.Vector;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bull extends AnimatedSprite {
	
	private Body body;
	
	public Bull(float pX,float pY,float width,float height,VertexBufferObjectManager vbom,BoundCamera camera,PhysicsWorld physicsWorld){
		super(pX,pY,width,height,ResourceManager.getInstance().bullTexture,vbom);
		createPhysics(camera,physicsWorld);
		
	}
	
	private void createPhysics(final BoundCamera camera,PhysicsWorld physicsWorld){
		
		body=PhysicsFactory.createBoxBody(physicsWorld,this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		
		body.setUserData("bull");
		body.setFixedRotation(true);
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false){
			
			@Override
			public void onUpdate(float pSecondsElapsed){
				
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(pSecondsElapsed);
				//add 1 to the velocity every 3 levels
				float velocityX=VelocityProvider.getBullVelocityByLevel(UserState.getInstance().getSelectedSession().getCurrentLevel());
				
				body.setLinearVelocity(new Vector2(velocityX,body.getLinearVelocity().y));
			}
		});
	}
	
	public void setRunning(){
		
		if(!this.isAnimationRunning()){
			final long[] BULL_ANIMATE = new long[]{100,100};
			animate(BULL_ANIMATE,0,1,true);
		}
	}

}
