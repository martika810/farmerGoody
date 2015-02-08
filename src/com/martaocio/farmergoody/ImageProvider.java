package com.martaocio.farmergoody;

public class ImageProvider {

	public static String getPointImage(String imageType, String levelType) {
		if (levelType.equals(Constants.FARM)) {
			if (imageType.equals(Constants.POINT1))
				return "tomato_point1.png";
			if (imageType.equals(Constants.POINT2))
				return "tomato_point2.png";
			if (imageType.equals(Constants.POINT3))
				return "tomato_point3.png";
			if (imageType.equals(Constants.POINT4))
				return "tomato_point4.png";
			if (imageType.equals(Constants.POINT5))
				return "tomato_point5.png";
			if (imageType.equals(Constants.POINT6))
				return "tomato_point6.png";
			if (imageType.equals(Constants.POINT8))
				return "tomato_point8.png";
			if (imageType.equals(Constants.POINT9))
				return "tomato_point9.png";
			if (imageType.equals(Constants.POINT10))
				return "tomato_point10.png";

		}
		if (levelType.equals(Constants.FOREST)) {
			if (imageType.equals(Constants.POINT1))
				return "orange_point1.png";
			if (imageType.equals(Constants.POINT2))
				return "orange_point2.png";
			if (imageType.equals(Constants.POINT3))
				return "orange_point3.png";
			if (imageType.equals(Constants.POINT4))
				return "orange_point4.png";
			if (imageType.equals(Constants.POINT5))
				return "orange_point5.png";
			if (imageType.equals(Constants.POINT6))
				return "orange_point6.png";
			if (imageType.equals(Constants.POINT8))
				return "orange_point8.png";
			if (imageType.equals(Constants.POINT9))
				return "orange_point9.png";
			if (imageType.equals(Constants.POINT10))
				return "orange_point10.png";
		}
		if (levelType.equals(Constants.LONDON)) {
			if (imageType.equals(Constants.POINT1))
				return "chip_point1.png";
			if (imageType.equals(Constants.POINT2))
				return "chip_point2.png";
			if (imageType.equals(Constants.POINT3))
				return "chip_point3.png";
			if (imageType.equals(Constants.POINT4))
				return "chip_point4.png";
			if (imageType.equals(Constants.POINT5))
				return "chip_point5.png";
			if (imageType.equals(Constants.POINT6))
				return "chip_point6.png";
			if (imageType.equals(Constants.POINT8))
				return "chip_point8.png";
			if (imageType.equals(Constants.POINT9))
				return "chip_point9.png";
			if (imageType.equals(Constants.POINT10))
				return "chip_point10.png";
		}
		
		return "noimage";
	}

}
