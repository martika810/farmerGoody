package com.martaocio.farmergoody.util;

import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.domain.Vehicle;

public class UserStateUtil {
	
	public static void  addVehicle(Vehicle vehicule,int pointToPay){
		int currentMoney=UserState.getInstance().getSelectedSession().getCurrentMoney();
		if(!UserState.getInstance().getAvailableVehicles().contains(vehicule)){
		
			UserState.getInstance().getAvailableVehicles().add(vehicule);
			UserState.getInstance().getSelectedSession().setCurrentMoney(currentMoney-pointToPay);
			//UserState.getInstance().getSelectedSession().setVehicleUsed(vehicule);
		}
		
	}
	

}
