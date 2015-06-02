package com.martaocio.farmergoody.asynctasks;

import com.martaocio.farmergoody.MainGameActivity;
import com.martaocio.farmergoody.domain.AchievementBox;
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
    	   return AchievementHelper.SYNC_GOOGLE_ERROR;
       }else{
    	   return AchievementHelper.SYNC_GOOGLE_SUCCESS;
       }
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
    	if(resultCode==AchievementHelper.SYNC_GOOGLE_SUCCESS){
    		AchievementHelper.prepare(activity, achievementBoxResult);
    	}
    }

    @Override
    protected void onPreExecute() {
    }

}
