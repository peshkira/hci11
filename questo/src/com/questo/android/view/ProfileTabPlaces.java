package com.questo.android.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileTabPlaces extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		TextView textView = new TextView(this);
		textView.setText("PlacesTab");
		setContentView(textView);
	}
}
