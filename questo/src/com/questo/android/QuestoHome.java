package com.questo.android;

import android.app.Activity;
import android.os.Bundle;

import com.questo.android.view.TopBar;

public class QuestoHome extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        TopBar topbar = (TopBar)findViewById(R.id.topbar);
        topbar.setTopBarLabel("LolCat!");
        topbar.addButtonLeftMost(getApplicationContext(), "+");
    }
}