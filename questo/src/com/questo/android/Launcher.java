package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.questo.android.model.User;
import com.questo.android.model.test.TestData;

public class Launcher extends Activity {

	//private static final String TAG = "Launcher Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App app = (App) getApplication();
		
		
		// Debug testdata and login routine - TODO DELETE FOR PRODUCTION!:
		User mainuser = TestData.generateTestData(app.getModelManager());
		app.setUserForDebugging(mainuser);
		startActivity(new Intent(this, HomeView.class));
		
		// Production login routine:
		/*
		if (!app.tryAutomaticLogin())
			startActivity(new Intent(this, LoginView.class));
		else {
			startActivity(new Intent(this, HomeView.class));
		}*/
	}
	
}
