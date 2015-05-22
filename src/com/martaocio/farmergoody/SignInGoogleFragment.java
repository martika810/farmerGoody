package com.martaocio.farmergoody;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

import android.app.Fragment;
import android.content.IntentSender.SendIntentException;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class SignInGoogleFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {

	private GoogleApiClient mGoogleApiClient;

	public static final int RC_SIGN_IN = 0;
	private boolean mIntentInProgress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.button_sign_google_layout, container, false);

		SignInButton signInBtn=(SignInButton) rootView.findViewById(R.id.google_sign_in_button);
		signInBtn.setOnClickListener(this);
		signInBtn.setSize(SignInButton.SIZE_ICON_ONLY);
		
		mGoogleApiClient = buildGoogleApiClient();
		return rootView;

	}

	@Override
	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
		;
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
			;
		}
	}

	@Override
	public void onClick(View v) {
		if (!mGoogleApiClient.isConnecting()) {

			switch (v.getId()) {
			case R.id.google_sign_in_button:
				Toast.makeText(getActivity().getApplicationContext(), "connecting", Toast.LENGTH_LONG).show();

			}
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress && result.hasResolution()) {
			try {
				mIntentInProgress = true;
				getActivity().startIntentSenderForResult(result.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

	private GoogleApiClient buildGoogleApiClient() {
		return new GoogleApiClient.Builder(getActivity().getApplicationContext()).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	public GoogleApiClient getmGoogleApiClient() {
		return mGoogleApiClient;
	}

	public boolean ismIntentInProgress() {
		return mIntentInProgress;
	}
	
	public void setmIntentInProgress(boolean intentInProgress ) {
		mIntentInProgress=intentInProgress;
	}


}
