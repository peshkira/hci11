package com.questo.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.questo.android.error.LoginError;
import com.questo.android.helper.FontHelper;
import com.questo.android.helper.QuestoFieldFocusListener;

public class LoginView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		TextView welcomeText = (TextView)findViewById(R.id.welcome);
		welcomeText.setTypeface(FontHelper.getMedievalFont(getBaseContext()));
		
		RelativeLayout background = (RelativeLayout) findViewById(R.id.homeGrid);
		background.setOnTouchListener(new QuestoFieldFocusListener((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)));
		
		Button loginButton = (Button)findViewById(R.id.login_button);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView email = (TextView)findViewById(R.id.login_email);
				TextView password = (TextView)findViewById(R.id.login_password);
				LoginError error = ((App)getApplicationContext()).login(email.getText().toString(), password.getText().toString());
				if (error != null) {
					if (error.getEmailWrong() != null && error.getEmailWrong()) 
						Toast.makeText(getBaseContext(), R.string.error_login_email_notfound, Toast.LENGTH_LONG).show();
					else if (error.getPasswordWrong() != null && error.getPasswordWrong())
						Toast.makeText(getBaseContext(), R.string.error_login_password_wrong, Toast.LENGTH_LONG).show();
					else // no error found, although error obj was not null -> unlikely to occur!
						gotoHomeScreen();
				}
				else
					gotoHomeScreen();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// Similar result as pressing home button, but app lives on:
		moveTaskToBack(true);
	}
	
	
	private void gotoHomeScreen() {
	    App app = (App) getApplicationContext();
	    //if (app.isFirstLogin()) {
	        startActivity(new Intent(this, TutorialView.class));
//	    } else {
//	        startActivity(new Intent(getBaseContext(), HomeView.class));
//	    }
	}
	
}
