package com.questo.android;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.questo.android.view.ProfileTabPlaces;
import com.questo.android.view.ProfileTabThrophies;
import com.questo.android.view.ProfileTabTournaments;

public class UserProfile extends TabActivity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        
        this.initView();
    }
    
    private void initView(){
        setContentView(R.layout.user_profile);
        
        TabHost tabHost = getTabHost();
        TabSpec spec;
        Intent intent;
        
        intent = new Intent().setClass(this, ProfileTabThrophies.class);
        spec = tabHost.newTabSpec("ProfileTabThrophies");
//        spec.setIndicator("Throphy");
        spec.setIndicator("Throphy", getResources().getDrawable(R.drawable.tab));
        spec.setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ProfileTabPlaces.class);
        spec = tabHost.newTabSpec("ProfileTabPlaces");
        spec.setIndicator("Places");
        spec.setContent(intent);
        tabHost.addTab(spec);        
        
        intent = new Intent().setClass(this, ProfileTabTournaments.class);
        spec = tabHost.newTabSpec("ProfileTabTournaments");
        spec.setIndicator("Tournaments");
        spec.setContent(intent);
        tabHost.addTab(spec);               
        
        tabHost.setCurrentTab(0);
    }
}
