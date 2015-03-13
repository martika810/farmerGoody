package com.martaocio.farmergoody;

public enum Vehicle {
	UNICYCLE(1,"Unicyle",50), BICYCLE(2,"Bicyle",250), SCOOTER(3,"Scooter",800), HARLEY(4,"Hardley",1000),NONE(0,"",0);

	private int id;
	private String description;
	private int price;

	Vehicle(int id,String description,int price) {
		this.id=id;
		this.description=description;
		this.price=price;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public int getPrice() {
		return price;
	}
	

	

	
	

}
