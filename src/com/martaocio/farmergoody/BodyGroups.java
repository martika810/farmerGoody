package com.martaocio.farmergoody;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class BodyGroups {
	
	public static final short CATEGORYBIT_GROUND = 1;
	public static final short CATEGORYBIT_BULL = 2;
	public static final short CATEGORYBIT_TOMATO = 4;
	public static final short CATEGORYBIT_END = 8;
	public static final short CATEGORYBIT_PLAYER = 16;
	public static final short CATEGORYBIT_LIFE = 32;
	public static final short CATEGORYBIT_LINE= 64;
	public static final short CATEGORYBIT_FENCE= 128;
	
	
	/* And what should collide with what. */
	public static final short MASKBITS_BULL = CATEGORYBIT_BULL + CATEGORYBIT_GROUND+CATEGORYBIT_END+CATEGORYBIT_PLAYER;
	
	public static final short MASKBITS_LINE = CATEGORYBIT_LINE+
			CATEGORYBIT_PLAYER;
	
	public static final short MASKBITS_GROUND =CATEGORYBIT_BULL+CATEGORYBIT_GROUND+CATEGORYBIT_PLAYER;
	public static final short MASKBITS_END =CATEGORYBIT_BULL+CATEGORYBIT_END+CATEGORYBIT_PLAYER;
	public static final short MASKBITS_TOMATO =CATEGORYBIT_TOMATO+CATEGORYBIT_PLAYER;
	public static final short MASKBITS_LIFE =CATEGORYBIT_LIFE+CATEGORYBIT_PLAYER;
	public static final short MASKBITS_FENCE =CATEGORYBIT_FENCE+CATEGORYBIT_PLAYER;
	public static final short MASKBITS_PLAYER = CATEGORYBIT_PLAYER 
												+ CATEGORYBIT_BULL
												+CATEGORYBIT_GROUND
												+CATEGORYBIT_TOMATO
												+CATEGORYBIT_END
												+CATEGORYBIT_LIFE
												+CATEGORYBIT_LINE
												+CATEGORYBIT_FENCE;
	
	
	
	
	
	
	
	public static final FixtureDef BULL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORYBIT_BULL, MASKBITS_BULL, (short)0);
	public static final FixtureDef PLAYER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0f, false, CATEGORYBIT_PLAYER, MASKBITS_PLAYER, (short)0);
	public static final FixtureDef LINE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0f, 0f, false, CATEGORYBIT_LINE, MASKBITS_LINE, (short)0);
	public static final FixtureDef GROUND_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, .2f, false, CATEGORYBIT_GROUND, MASKBITS_GROUND, (short)0);
	
	public static final FixtureDef END_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, .2f, false, CATEGORYBIT_END, MASKBITS_END, (short)0);
	public static final FixtureDef TOMATO_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, .2f, false, CATEGORYBIT_TOMATO, MASKBITS_TOMATO, (short)0);
	public static final FixtureDef FENCE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, .2f, false, CATEGORYBIT_FENCE, MASKBITS_FENCE, (short)0);
	public static final FixtureDef LIFE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, .2f, false, CATEGORYBIT_LIFE, MASKBITS_LIFE, (short)0);
	//public static final FixtureDef CIRCLE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_CIRCLE, MASKBITS_CIRCLE, (short)0);

	

	


}
