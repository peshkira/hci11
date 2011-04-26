package com.questo.android;

import com.questo.android.view.TopBar;

import android.app.Activity;
import android.os.Bundle;

public class QuestionView extends Activity {

    private TopBar topbar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        this.init(this.getIntent().getExtras());

    }

    private void init(Bundle extras) {
        this.topbar = (TopBar) findViewById(R.id.topbar);
        //this.topbar.setTopBarLabel(label) 
    }
}
