package com.martaocio.farmergoody;

public class ImageProvider {

	public static String getPointImage(String imageType, String levelType) {
		if (levelType.equals(Constants.FARM)) {
			
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

		}
		if (levelType.equals(Constants.FOREST)) {
			
			if (imageType.equals(Constants.POINT5))
				return "orange_point5.png";
			if (imageType.equals(Constants.POINT10))
				return "orange_point10.png";
		}
		if (levelType.equals(Constants.LONDON)) {
			
			if (imageType.equals(Constants.POINT5))
				return "chip_point5.png";
			
			if (imageType.equals(Constants.POINT10))
				return "chip_point10.png";
		}
		
		return "noimage";
	}

}
