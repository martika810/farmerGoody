package com.martaocio.farmergoody.fragments;

import com.google.android.gms.plus.PlusShare;
import com.martaocio.farmergoody.CustomContentProvider;
import com.martaocio.farmergoody.R;
import com.martaocio.farmergoody.domain.GameSession;
import com.martaocio.farmergoody.providers.ImageProvider;
import com.martaocio.farmergoody.providers.LevelProvider;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShareGoogleFragment extends Fragment implements OnClickListener {
	Button shareButton;
	GameSession dataToShare;
	
	private static final int REQ_START_SHARE = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.button_share_google, container, false);
		shareButton = (Button) rootView.findViewById(R.id.share_google_button);
		shareButton.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		PlusShare.Builder postToSend = new PlusShare.Builder(getActivity());
		
		postToSend=populatePostWithGameSessionInfo(postToSend);
		
		startActivityForResult(postToSend.getIntent(), REQ_START_SHARE);

	}
	
	private PlusShare.Builder populatePostWithGameSessionInfo( PlusShare.Builder post){
		//CustomContentProvider contentProvider =new CustomContentProvider();
//		String pathImageToShare ="file:///assets/gfx/"+ImageProvider.getLevelIconImagePath(1);
//		Uri uriImageToShare =Uri.parse(pathImageToShare);
//		ContentResolver cr = getActivity().getContentResolver();
//	    String mime = cr.getType(uriImageToShare);
	    
	    
	    post.setText("Just Got to Level " + dataToShare.getCurrentLevel()+"!!");
	    //post.addStream(uriImageToShare);
	    post.setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.martaocio.farmergoody"));
		post.setType("text/plain");
		
		
		//post.setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.martaocio.farmergoody"));
		return post;
		
	}

	public GameSession getDataToShare() {
		return dataToShare;
	}

	public void setDataToShare(GameSession dataToShare) {
		this.dataToShare = dataToShare;
	}
	

}
