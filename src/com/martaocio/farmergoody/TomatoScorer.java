package com.martaocio.farmergoody;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class TomatoScorer  extends AnimatedSprite{
	
	public static final float PORTION=8.333f;
	
	public TomatoScorer(float pX, float pY, float width, float height, VertexBufferObjectManager vbom){
		super(pX, pY, width, height, ResourceManager.getInstance().tomatoScorer, vbom);
		this.setIgnoreUpdate(true);
		this.setCurrentTileIndex(0);
	}
	
	public void update(int percentage){
		if(percentage>0 && percentage<=PORTION){
			this.setCurrentTileIndex(1);
		}else if(percentage>PORTION && percentage<=PORTION*2){
			this.setCurrentTileIndex(2);
		}else if(percentage>PORTION*2 && percentage<=PORTION*3){
			this.setCurrentTileIndex(3);
		}else if(percentage>PORTION*3 && percentage<=PORTION*4){
			this.setCurrentTileIndex(4);
		}else if(percentage>PORTION*4 && percentage<=PORTION*5){
			this.setCurrentTileIndex(5);
		}else if(percentage>PORTION*5 && percentage<=50){
			this.setCurrentTileIndex(5);
		}else if(percentage>50 && percentage<=PORTION*7){
			this.setCurrentTileIndex(6);
		}else if(percentage>PORTION*7 && percentage<=PORTION*8){
			this.setCurrentTileIndex(7);
		}else if(percentage>PORTION*8 && percentage<=PORTION*9){
			this.setCurrentTileIndex(8);
		}else if(percentage>PORTION*9 && percentage<=PORTION*10){
			this.setCurrentTileIndex(9);
		}else if(percentage>PORTION*10 && percentage<=PORTION*11){
			this.setCurrentTileIndex(10);
		}else if(percentage>PORTION*11 && percentage<=PORTION*12){
			this.setCurrentTileIndex(11);
		}else if(percentage>PORTION*12 && percentage<=100){
			this.setCurrentTileIndex(12);
		}
		
	}

}
