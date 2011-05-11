package com.questo.android;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

import com.questo.android.common.Constants;
import com.questo.android.model.User;
import com.questo.android.view.ProfileTabPlaces;
import com.questo.android.view.ProfileTabThrophies;
import com.questo.android.view.TopBar;

public class ProfileView extends TabActivity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.user_profile);
        
        this.initView(this.getIntent().getExtras());
    }
    
    private void initView(Bundle extras){
        User user;
        App app = (App) getApplicationContext();
        ModelManager mngr = app.getModelManager();
        
        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        TextView state = (TextView) findViewById(R.id.UserProfileStateText);
        
        
        if (extras != null) {
            String userUuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
            user = mngr.getGenericObjectByUuid(userUuid, User.class);
            topbar.setLabel(user.getName());
            state.setText(Html.fromHtml(user.getName() + " is a Peasant<br/>Points earned: 12"));
        } else {
            user = app.getLoggedinUser();
            topbar.setLabel("You");
            state.setText(Html.fromHtml("You are a King<br/>Points earned: 2337"));
        }
        
        TabHost tabHost = getTabHost();
        TabSpec spec;
        Intent intent;
        
        intent = new Intent().setClass(this, ProfileTabThrophies.class).putExtra(Constants.TRANSITION_OBJECT_UUID, user.getUuid());
        spec = tabHost.newTabSpec("ProfileTabThrophies");
        spec.setIndicator("Throphy", getResources().getDrawable(R.drawable.img_trophy_thumb));
        spec.setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ProfileTabPlaces.class).putExtra(Constants.TRANSITION_OBJECT_UUID, user.getUuid());
        spec = tabHost.newTabSpec("ProfileTabPlaces");
        spec.setIndicator("Places", getResources().getDrawable(R.drawable.img_questo_sign_thumb));
        spec.setContent(intent);
        tabHost.addTab(spec);                      
        
        tabHost.setCurrentTab(0);
    }
}
