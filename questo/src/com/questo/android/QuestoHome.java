package com.questo.android;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuestoHome extends Activity implements View.OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        Button showProfile = (Button) findViewById(R.id.profileBtn);
        showProfile.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		Intent i = new Intent(this, UserProfile.class);
		startActivity(i);
	}
}