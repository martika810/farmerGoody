package com.martaocio.farmergoody;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class Player extends AnimatedSprite {
	
	private Body body;
	
	public boolean canJump=true;
	public boolean canEat=false;

	public Player(float pX,float pY,float width,float height,VertexBufferObjectManager vbom,BoundCamera camera,PhysicsWorld physicsWorld){
		
		super(pX,pY,width,height,ResourceManager.getInstance().playerTexture,vbom);
		createPhysics(camera, physicsWorld);
		
		camera.setChaseEntity(this);
		camera.setBounds(0, 240, 17600, 290);
		camera.setBoundsEnabled(true);
		camera.offsetCenter(0, 0);
		
		
	}
	
	private void createPhysics(final BoundCamera camera,PhysicsWorld physicsWorld){
		body=PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		
		body.setUserData("player");
		body.setFixedRotation(true);//because the body has to be still, it doesnt need to rotate
		
		
		// this conector links the body to the sprite so the sprite can follow the body and viceversa
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false){
			
			//add an update handler
			@Override
			public void onUpdate(float pSecondsElapsed){
				
				//every second the body is gonna update two body so the body keep moving
				
				super.onUpdate(pSecondsElapsed);
				
				camera.onUpdate(.1f);//to keep the camera centered when the sprite is moving
				
				//detect the position of the player, this detection must be in the onnUpdate so the position of the
				//player can be check constantly
				if(getY()>=480){
					onDie();	
					camera.setChaseEntity(null);
				}
				
				// the velocyty makes the player moves  in certain direction
				//we leave the y the same bcos we just want to move it across
				body.setLinearVelocity(new Vector2(3f,body.getLinearVelocity().y));
			}
		});
		
	}
	
	public abstract void onDie();
	
	// to animate the player
	public void setRunning(){
		
		if(!this.isAnimationRunning()){//if animation is not running
			canEat=false;
			final long[] PLAYER_ANIMATE=new long[]{100,100,100,100};//array to hold the animations
			animate(PLAYER_ANIMATE,0,3,true);
		}
	}
	
	//make the player jumps
	public void jump(){
		//first stop the player from running
		if(canJump){
			
			//ResourceManager.getInstance().jumpSound.play();
			//comment this if the walking animation is gonna keep running while jumping
			this.stopAnimation();
			this.setCurrentTileIndex(5);//set the application to the jump picture
			
			canJump=false;
			
			Vector2 velocity=new Vector2(0,-13); //set the height of the jump(10 metres off the ground)
			
			body.setLinearVelocity(velocity);
		}
	}
	public void eat(){
		
		if(!canEat){
			this.stopAnimation();
			
			this.setCurrentTileIndex(4);
			
			canEat=true;
			
			Vector2 velocity=new Vector2(0,0);
			
			body.setLinearVelocity(velocity);
			
		}
	}
	
	public void runFaster(BoundCamera camera){
		//float currentVelocity=body.getLinearVelocity().x;
		Vector2 velocity=new Vector2(5f,body.getLinearVelocity().y); //set the height of the jump(10 metres off the ground)
		
		body.setTransform(body.getPosition().x+1f,body.getPosition().y, 0);
		camera.offsetCenter(camera.getCenterX()+1, camera.getCenterY());
		
		
	}
	
	public void runSlower(){
		//float currentVelocity=body.getLinearVelocity().x;
		body.setTransform(body.getPosition().x-1f,body.getPosition().y, 0);
		
		
	}
	

}
