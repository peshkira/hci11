package com.questo.android;

import com.questo.android.view.ProfileTabPlaces;
import com.questo.android.view.ProfileTabThrophies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AddPlace extends Activity{
	
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);   
	        
	        this.initView();
	    }
	    
	    private void initView(){
	        setContentView(R.layout.add_place);
	    }
}
