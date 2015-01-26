package com.martaocio.farmergoody;

import com.badlogic.gdx.physics.box2d.Body;

public class GameUtils {

	public static boolean isCollisionBetween(Body a, Body b, String elem1, String elem2) {

		return (a.getUserData().equals(elem1) && b.getUserData().equals(elem2) || b.getUserData().equals(elem1)
				&& a.getUserData().equals(elem2));
	}

	public static boolean isBodyTomato(Body a) {

		return (a.getUserData().equals(SpriteTag.TOMATO1) ||
				a.getUserData().equals(SpriteTag.TOMATO2)||
				a.getUserData().equals(SpriteTag.TOMATO3)||
				a.getUserData().equals(SpriteTag.TOMATO4)||
				a.getUserData().equals(SpriteTag.TOMATO5)||
				a.getUserData().equals(SpriteTag.TOMATO6)||
				a.getUserData().equals(SpriteTag.TOMATO8)||
				a.getUserData().equals(SpriteTag.TOMATO9)||
				a.getUserData().equals(SpriteTag.TOMATO10));
	}
	
	public static String extractTomatoType(Body a) {

		 if(a.getUserData().equals(SpriteTag.TOMATO1)){
			 return SpriteTag.TOMATO1;
		 }
		 else if(a.getUserData().equals(SpriteTag.TOMATO2)){
			 return SpriteTag.TOMATO2;
		 }
		 else if(a.getUserData().equals(SpriteTag.TOMATO3)){
			 return SpriteTag.TOMATO3;
		 }
		 else if(a.getUserData().equals(SpriteTag.TOMATO4)){
			 return SpriteTag.TOMATO4;
		 }
		 else if(a.getUserData().equals(SpriteTag.TOMATO5)){
			 return SpriteTag.TOMATO5;
		 }
		 else if(a.getUserData().equals(SpriteTag.TOMATO6)){
			 return SpriteTag.TOMATO6;
		 }
		 else if(a.getUserData().equals(SpriteTag.TOMATO8)){
			 return SpriteTag.TOMATO8;
		 }
		 else if(a.getUserData().equals(SpriteTag.TOMATO9)){
			 return SpriteTag.TOMATO9;
		 }
		 else if (a.getUserData().equals(SpriteTag.TOMATO10)){
			 return SpriteTag.TOMATO10;
		 }
		 else{
			 return Answer.NO;
		 }
	}

}
