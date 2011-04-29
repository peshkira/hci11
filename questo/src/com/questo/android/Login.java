package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.questo.android.error.LoginError;
import com.questo.android.helper.FontHelper;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		TextView welcomeText = (TextView)findViewById(R.id.welcome);
		welcomeText.setTypeface(FontHelper.getMedievalFont(getBaseContext()));
		
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
		startActivity(new Intent(getBaseContext(), QuestoHome.class));
	}
	
}
