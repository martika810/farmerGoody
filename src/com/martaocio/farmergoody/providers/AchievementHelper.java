package com.martaocio.farmergoody.providers;

import java.util.List;

import android.content.Context;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;
import com.martaocio.farmergoody.MainGameActivity;
import com.martaocio.farmergoody.R;
import com.martaocio.farmergoody.domain.AchievementBox;
import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.domain.Vehicle;
import com.martaocio.farmergoody.util.UserStateUtil;

public class AchievementHelper {

	public final static int SYNC_GOOGLE_SUCCESS = 1;
	public final static int SYNC_GOOGLE_ERROR = 0;
	private Context context;
	private static AchievementHelper INSTANCE = null;
	public static int NUMBER_POINTS_PER_STEP = 200;
	public AchievementBox achievementBox;


	private AchievementHelper(Context context,AchievementBox achievements) {
		this.context = context;
		this.achievementBox = achievements;
	}

	public static AchievementHelper getInstance(Context context) {
		
		return INSTANCE;
	}

	public static AchievementHelper prepare(Context context,AchievementBox initialBox) {
		INSTANCE = new AchievementHelper(context,initialBox);

		return INSTANCE;

	}

	public void checkAchievements(int moneyPoints) {
		// AchievementBox achievementBox = loadAchievementBox();
		boolean justGotMonocycle = !achievementBox.isMonocycleAchievement()
				&& (moneyPoints >= Vehicle.UNICYCLE.getPrice() && moneyPoints < Vehicle.BICYCLE.getPrice());
		boolean isProgressForFirstMoto = achievementBox.isMonocycleAchievement()
				&& (moneyPoints >= Vehicle.UNICYCLE.getPrice() && moneyPoints < Vehicle.BICYCLE.getPrice())
				&& (moneyPoints % NUMBER_POINTS_PER_STEP == 0);
		boolean justGotFirstMoto = !achievementBox.isFirstMotoAchievement()
				&& (moneyPoints >= Vehicle.BICYCLE.getPrice() && moneyPoints < Vehicle.SCOOTER.getPrice());
		boolean isProgressForScooter = achievementBox.isFirstMotoAchievement()
				&& (moneyPoints > Vehicle.BICYCLE.getPrice() && moneyPoints < Vehicle.SCOOTER.getPrice())
				&& (moneyPoints % NUMBER_POINTS_PER_STEP == 0);
		boolean justGotScooter = !achievementBox.isMonocycleAchievement()
				&& (moneyPoints >= Vehicle.SCOOTER.getPrice() && moneyPoints < Vehicle.HARLEY.getPrice());
		boolean isProgressForCoolMoto = achievementBox.isMonocycleAchievement()
				&& (moneyPoints >= Vehicle.SCOOTER.getPrice() && moneyPoints < Vehicle.HARLEY.getPrice())
				&& (moneyPoints % NUMBER_POINTS_PER_STEP == 0);
		boolean justGotCoolMoto = !achievementBox.isMonocycleAchievement() && (moneyPoints >= Vehicle.HARLEY.getPrice());
		boolean justGotAntarctica = !achievementBox.isMonocycleAchievement() && (moneyPoints >= 1200);

		if (justGotMonocycle) {

			achievementBox.setMonocycleAchievement(true);
			UserStateUtil.addVehicle(Vehicle.UNICYCLE, Vehicle.UNICYCLE.getPrice());
			return;
		}
		if (isProgressForFirstMoto) {
			achievementBox.increaseNumberStepsFirstMoto();
			return;
		}
		if (justGotFirstMoto) {
			achievementBox.setFirstMotoAchievement(true);
			UserStateUtil.addVehicle(Vehicle.BICYCLE, Vehicle.BICYCLE.getPrice());
			return;
		}
		if (isProgressForScooter) {
			achievementBox.increaseNumberStepsScooter();
			return;
		}

		if (justGotScooter) {
			achievementBox.setScooterAchievement(true);
			UserStateUtil.addVehicle(Vehicle.SCOOTER, Vehicle.SCOOTER.getPrice());
			return;
		}

		if (isProgressForCoolMoto) {
			achievementBox.increaseNumberStepsCoolMoto();
			return;
		}
		if (justGotCoolMoto) {
			achievementBox.setCoolMotoAchievement(true);
			UserStateUtil.addVehicle(Vehicle.HARLEY, Vehicle.HARLEY.getPrice());
			return;
		}

	}

	public void pushAchievements(MainGameActivity activity) {
		if (achievementBox.isMonocycleAchievement()) {
			activity.unlockAchievement(R.string.monocycle_achievements, "fallback string");
		}
		if (achievementBox.getNumberStepsFirstMoto() > 0) {
			activity.incrementAchievement(R.string.moto_achievements, achievementBox.getNumberStepsFirstMoto());
		}
		if (achievementBox.isFirstMotoAchievement()) {
			activity.unlockAchievement(R.string.moto_achievements, "fallback string");
		}
		if (achievementBox.getNumberStepsScooter() > 0) {
			activity.incrementAchievement(R.string.scooter_achievements, achievementBox.getNumberStepsScooter());
		}
		if (achievementBox.isScooterAchievement()) {
			activity.unlockAchievement(R.string.scooter_achievements, "fallback string");
		}
		if (achievementBox.getNumberStepsCoolMoto() > 0) {
			activity.incrementAchievement(R.string.coolmoto_achievements, achievementBox.getNumberStepsCoolMoto());
		}
		if (achievementBox.isCoolMotoAchievement()) {
			activity.unlockAchievement(R.string.coolmoto_achievements, "fallback string");
		}

	}

	public AchievementBox loadAchievementBoxFromLocal() {
		AchievementBox achiviementBox = new AchievementBox();

		List<Vehicle> availableVehicles = UserState.getInstance().getAvailableVehicles();

		achiviementBox.setMonocycleAchievement(availableVehicles.contains(Vehicle.UNICYCLE));
		achiviementBox.setFirstMotoAchievement(availableVehicles.contains(Vehicle.BICYCLE));
		achiviementBox.setScooterAchievement(availableVehicles.contains(Vehicle.SCOOTER));
		achiviementBox.setCoolMotoAchievement(availableVehicles.contains(Vehicle.HARLEY));
		achiviementBox.setAntarcticaAchievement(UserState.getInstance().isCanGoToAntarctica());

		return achiviementBox;
	}
	
	public AchievementBox getAchievementBox() {
		return achievementBox;
	}
	
	

	public static void updateAchievementBox(MainGameActivity activity, AchievementBox boxToUpdate, String achiviementId,
			boolean isUnlocked, boolean isIncremental, int steps) {
		boolean isMonocycleAchievement = (achiviementId.equals(activity.getString(R.string.monocycle_achievements)));
		if (isMonocycleAchievement) {
			boxToUpdate.setMonocycleAchievement(isUnlocked);
			return;
		}
		boolean isFirstMotoAchievement = (achiviementId.equals(activity.getString(R.string.moto_achievements)));
		if (isFirstMotoAchievement) {
			boxToUpdate.setNumberStepsFirstMoto(steps);
			boxToUpdate.setFirstMotoAchievement(isUnlocked);
			return;

		}
		boolean isScooterAchievement = (achiviementId.equals(activity.getString(R.string.scooter_achievements)));
		if (isScooterAchievement) {
			boxToUpdate.setNumberStepsScooter(steps);
			boxToUpdate.setScooterAchievement(isUnlocked);
			return;
		}
		boolean isCoolMotoAchiviement = (achiviementId.equals(activity.getString(R.string.coolmoto_achievements)));
		if (isCoolMotoAchiviement) {
			boxToUpdate.setNumberStepsCoolMoto(steps);
			boxToUpdate.setCoolMotoAchievement(isUnlocked);
			return;
		}

	}
	
	

	public static AchievementBox syncAchievements(MainGameActivity activity) {
		AchievementBox result = new AchievementBox();
		if (activity.isSignedIn()) {
			
			boolean fullLoad = false; // set to 'true' to reload all
										// achievements (ignoring cache)
			float waitTime = 60.0f; // seconds to wait for achievements to load
									// before timing out

			// load achievements
			com.google.android.gms.common.api.PendingResult<LoadAchievementsResult> p = Games.Achievements.load(
					activity.getmGoogleApiClient(), fullLoad);
			LoadAchievementsResult r = (LoadAchievementsResult) p.await();
			int status = r.getStatus().getStatusCode();
			if (status != GamesStatusCodes.STATUS_OK) {
				r.release();
				return null; // Error Occured
			}

			// cache the loaded achievements
			AchievementBuffer buf = r.getAchievements();
			int bufSize = buf.getCount();
			for (int i = 0; i < bufSize; i++) {
				Achievement ach = buf.get(i);

				// here you now have access to the achievement's data
				String id = ach.getAchievementId(); // the achievement ID string
				boolean unlocked = ach.getState() == Achievement.STATE_UNLOCKED; // is
																					// unlocked
				boolean incremental = ach.getType() == Achievement.TYPE_INCREMENTAL; // is
																						// incremental
				int steps = 0;
				if (incremental) {
					steps = ach.getCurrentSteps(); // current incremental steps
				}
				updateAchievementBox(activity, result, id, unlocked, incremental, steps);
			}
			buf.close();
			r.release();
		}

		return result;
	}

}
