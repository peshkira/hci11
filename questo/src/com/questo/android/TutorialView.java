package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.questo.android.model.User;
import com.questo.android.view.TopBar;

public class TutorialView extends Activity {
    
    private static final String TUTORIAL_EXPLANATION = "This is a brief tutorial to guide you through the application." +
    		"You can <em>skip</em> it by clicking on the button above." +
    		"You can view the tutorial at any time from your profile.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
        this.init();
    }

    private void init() {
        App app = (App) this.getApplicationContext();
        User user = app.getLoggedinUser();
        
        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        Button skip = topbar.addImageButtonLeftMost(this.getApplicationContext(), R.drawable.img_skip);
        skip.setOnClickListener(new ProceedOnClickListener());
        
        TextView greet = (TextView) findViewById(R.id.greet);
        greet.setText(Html.fromHtml("Hello, " + user.getName() +"!<br>" + TUTORIAL_EXPLANATION));
    }
    
    private class ProceedOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), HomeView.class));
        }
        
    }
}
