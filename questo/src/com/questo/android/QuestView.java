package com.questo.android;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.questo.android.view.TopBar;

public class QuestView extends MapActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	private void initView(){
		setContentView(R.layout.quests);
		
		TopBar topBar = (TopBar)findViewById(R.id.topbar);
		topBar.addButtonLeftMost(this, "-");
		topBar.addButtonLeftMost(this, "+");
		
		
	}
}
