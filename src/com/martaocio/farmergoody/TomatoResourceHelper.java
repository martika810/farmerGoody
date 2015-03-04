package com.martaocio.farmergoody;

import org.andengine.opengl.texture.region.ITextureRegion;

public class TomatoResourceHelper {

	public static ITextureRegion getTomatoResource(String tomatoTag) {
		
		
		if (tomatoTag.equals(SpriteTag.TOMATO5)) {
			return ResourceManager.getInstance().point5Texture;
		}
		
		if (tomatoTag.equals(SpriteTag.TOMATO10)) {
			return ResourceManager.getInstance().point10Texture;
		}
		if (tomatoTag.equals(SpriteTag.MINUSTOMATO5)) {
			return ResourceManager.getInstance().minusPoint5Texture;
		}
		if (tomatoTag.equals(SpriteTag.MINUSTOMATO10)) {
			return ResourceManager.getInstance().minusPoint10Texture;
		}
		if (tomatoTag.equals(SpriteTag.MINUSTOMATO20)) {
			return ResourceManager.getInstance().minusPoint20Texture;
		}

		return ResourceManager.getInstance().point5Texture;

	}

}
