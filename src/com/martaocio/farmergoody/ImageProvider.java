package com.martaocio.farmergoody;

import org.andengine.opengl.texture.region.ITextureRegion;

public class ImageProvider {

	public static String getPointImage(String imageType) {
		
			
			if (imageType.equals(Constants.POINT5))
				return "tomato_point5.png";
			if (imageType.equals(Constants.POINT10))
				return "tomato_point10.png";
			if (imageType.equals(Constants.MINUSPOINT5))
				return "tomato_point_minus5.png";
			if (imageType.equals(Constants.MINUSPOINT10))
				return "tomato_point_minus10.png";
			if (imageType.equals(Constants.MINUSPOINT20))
				return "tomato_point_minus20.png";

		
		
		return "noimage";
	}
	
	public static ITextureRegion getLevelIcon(String descriptionLevelType){
		if(descriptionLevelType.equals(Constants.FOREST)){
			return ResourceManager.getInstance().levelIconForest;
		}else if(descriptionLevelType.equals(Constants.FARM_EVENING)){
			return ResourceManager.getInstance().levelIconFarmEvening;
		}else if(descriptionLevelType.equals(Constants.DESERT)){
			return ResourceManager.getInstance().levelIconDesert;
		}
		
		else{
			return ResourceManager.getInstance().levelIcon;
		}
	}

}
