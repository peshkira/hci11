package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Launcher extends Activity {

	//private static final String TAG = "Launcher Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App app = (App) getApplication();
		
		app.generateTestData();
		
		// Production login routine:
		
		if (!app.tryAutomaticLogin())
			startActivity(new Intent(this, LoginView.class));
		else {
			startActivity(new Intent(this, HomeView.class));
		}
	}
	
}
