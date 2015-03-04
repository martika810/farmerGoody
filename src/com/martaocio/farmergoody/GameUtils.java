package com.martaocio.farmergoody;

import com.badlogic.gdx.physics.box2d.Body;

public class GameUtils {

	public static boolean isCollisionBetween(Body a, Body b, String elem1, String elem2) {

		return (a.getUserData().equals(elem1) && b.getUserData().equals(elem2) || b.getUserData().equals(elem1)
				&& a.getUserData().equals(elem2));
	}

	public static boolean isBodyTomato(Body a) {

		return (a.getUserData().equals(SpriteTag.TOMATO5)||
				a.getUserData().equals(SpriteTag.TOMATO10)||
				a.getUserData().equals(SpriteTag.MINUSTOMATO5)||
				a.getUserData().equals(SpriteTag.MINUSTOMATO10)||
				a.getUserData().equals(SpriteTag.MINUSTOMATO20));
	}
	
	public static String extractTomatoType(Body a) {

		 if(a.getUserData().equals(SpriteTag.TOMATO5)){
			 return SpriteTag.TOMATO5;
		 }else if (a.getUserData().equals(SpriteTag.TOMATO10)){
			 return SpriteTag.TOMATO10;
		 }
		 else{
			 return Answer.NO;
		 }
	}

}
