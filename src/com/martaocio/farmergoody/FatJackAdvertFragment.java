package com.martaocio.farmergoody;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FatJackAdvertFragment extends Fragment {
	
	public final static String URL_FREE_ADS_VERSION = "https://play.google.com/store/apps/details?id=com.martaocio.fatjack";
	
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.advert_layout, container, false);
         
         rootView.findViewById(R.id.advert_image).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
	            i.setData(Uri.parse(URL_FREE_ADS_VERSION));
	            getActivity().startActivity(i);
				
			}
		});
         
         rootView.findViewById(R.id.close_advert_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainGameActivity)getActivity()).hideFatJackAdvert();
				
			}
		});
       
         return rootView;
     }

}
