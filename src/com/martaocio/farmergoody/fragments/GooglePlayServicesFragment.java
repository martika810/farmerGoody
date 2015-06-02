package com.martaocio.farmergoody.fragments;

import com.martaocio.farmergoody.MainGameActivity;
import com.martaocio.farmergoody.R;
import com.martaocio.farmergoody.fragments.SignInGoogleFragment.Listener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class GooglePlayServicesFragment extends Fragment implements OnClickListener {
	Button connectButton;
	Button disconnectButton;
	Button achievementsButton;
	Button leaderboardButton;

	Listener mListener = null;

	public interface Listener {

		public void onShowAchievementsRequested();

		public void onShowLeaderboardsRequested();

		public void onEnteredScore(int score);

		public void onSignInButtonClicked();

		public void onSignOutButtonClicked();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.google_play_services_layout, container, false);
		achievementsButton = (Button) rootView.findViewById(R.id.achievements_google);
		achievementsButton.setOnClickListener(this);
		leaderboardButton = (Button) rootView.findViewById(R.id.leaderboard_google);
		leaderboardButton.setOnClickListener(this);
		connectButton = (Button) rootView.findViewById(R.id.connect_google);
		connectButton.setOnClickListener(this);
		disconnectButton = (Button) rootView.findViewById(R.id.disconnect_google);
		disconnectButton.setOnClickListener(this);
		rootView.findViewById(R.id.btn_google_services_close).setOnClickListener(this);

		return rootView;
	}

	public void updateUi(boolean isSignIn) {
		if (getActivity() == null) return;
		if (isSignIn) {
			connectButton.setVisibility(View.GONE);
			disconnectButton.setVisibility(View.VISIBLE);
		} else {
			connectButton.setVisibility(View.VISIBLE);
			disconnectButton.setVisibility(View.GONE);
		}

	}

	public void setListener(Listener l) {
		mListener = l;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.achievements_google:
			mListener.onShowAchievementsRequested();
			break;
			
		case R.id.leaderboard_google:
			mListener.onShowLeaderboardsRequested();
			break;
		case R.id.connect_google:

			mListener.onSignInButtonClicked();
			break;
		case R.id.btn_google_services_close:
			((MainGameActivity) getActivity()).removeGoogleServiceFragment();
			break;
		case R.id.disconnect_google:

			mListener.onSignOutButtonClicked();

		}

	}
}
