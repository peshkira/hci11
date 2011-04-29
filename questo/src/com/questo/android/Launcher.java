package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.questo.android.model.test.TestData;

public class Launcher extends Activity {

	//private static final String TAG = "Launcher Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App app = (App) getApplication();
		TestData.generateTestData(app.getModelManager());
		if (!app.tryAutomaticLogin())
			startActivity(new Intent(this, Login.class));
		else {
			startActivity(new Intent(this, QuestoHome.class));
		}
	}
	
}
