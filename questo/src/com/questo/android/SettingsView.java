package com.questo.android;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsView extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        addPreferencesFromResource(R.xml.preferences);

        this.init();
    }

    private void init() {
        // App app = (App) this.getApplicationContext();
        // TopBar topbar = (TopBar) findViewById(R.id.topbar);

    }

}
