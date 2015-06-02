package com.martaocio.farmergoody.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.common.SignInButton;
import com.martaocio.farmergoody.MainGameActivity;
import com.martaocio.farmergoody.R;

public class SignInGoogleFragment extends Fragment implements OnClickListener {
	public interface Listener {

		public void onShowAchievementsRequested();

		public void onSignInButtonClicked();

		public void onSignOutButtonClicked();
	}

	private static final String SAVED_PROGRESS = "sign_in_progress";

	Listener mListener = null;
	private SignInButton signInBtn;
	boolean mShowSignIn = true;
	private ImageButton googleServiceButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.button_sign_google_layout, container, false);

		googleServiceButton = (ImageButton) rootView.findViewById(R.id.google_play_services);
		googleServiceButton.setOnClickListener(this);

		return rootView;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
	}

	public void setListener(Listener l) {
		mListener = l;
	}

	public void setShowSignInButton(boolean showSignIn) {
		mShowSignIn = showSignIn;
		updateUi();
	}

	void updateUi() {
		if (getActivity() == null)
			return;

		getActivity().findViewById(R.id.google_play_services).setVisibility(mShowSignIn ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onClick(View v) {

		((MainGameActivity)getActivity()).addGoogleServiceFragment();

	}



}
