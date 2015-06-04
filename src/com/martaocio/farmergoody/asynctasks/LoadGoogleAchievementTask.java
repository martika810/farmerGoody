package com.martaocio.farmergoody.asynctasks;

import com.martaocio.farmergoody.MainGameActivity;
import com.martaocio.farmergoody.domain.AchievementBox;
import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.providers.AchievementHelper;

import android.os.AsyncTask;

public class LoadGoogleAchievementTask extends AsyncTask<MainGameActivity, Void, Integer>{
	MainGameActivity activity;
	AchievementBox achievementBoxResult;
	@Override
    protected Integer doInBackground(MainGameActivity... params) {
		this.activity=params[0];
		achievementBoxResult= AchievementHelper.syncAchievements(params[0]);
       if(achievementBoxResult.isEmpty()){
    	  // AchievementHelper.prepare(activity, achievementBoxResult);
    	   return AchievementHelper.SYNC_GOOGLE_ERROR;
       }else{
    	  // AchievementHelper.prepare(activity, achievementBoxResult);
    	   return AchievementHelper.SYNC_GOOGLE_SUCCESS;
       }
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
    	
    	//	AchievementHelper.prepare(activity, achievementBoxResult);
    	AchievementHelper.prepare(activity, achievementBoxResult);
    	UserState.getInstance().syncWithGoogleAchievements(achievementBoxResult);
    	
    }

    @Override
    protected void onPreExecute() {
    }

}
