package com.martaocio.farmergoody;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Stair extends Sprite{

	private Body body;
	public static final String UP = "up";
	public static final String DOWN = "down";

	public String state = DOWN;

	public Stair(float pX, float pY, float width, float height, VertexBufferObjectManager vbom, BoundCamera camera,
			PhysicsWorld physicsWorld) {

		super(pX, pY, width, height, ResourceManager.getInstance().stairTexture, vbom);
		
		body=PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		body.setUserData(SpriteTag.STAIR);
		this.setUserData(body);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
		
		
		

	}

	public void rotate() {
		if (state.equals(DOWN)) {
			this.setRotationCenter(0, 0);
			this.setRotation(-70);
			body.setTransform(body.getPosition().x, body.getPosition().y, -1f);
			
		} else {
			this.setRotationCenter(0, 0);
			this.setRotation(70);
			body.setTransform(body.getPosition().x, body.getPosition().y, 1f);// 1.2217f
		}

	}
	
	  @Override
      public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY)
      {
		   
          this.rotate();
           
           return true;
      }
	

	
     
	
	

}
