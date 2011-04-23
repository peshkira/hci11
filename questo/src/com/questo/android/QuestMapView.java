package com.questo.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.questo.android.view.TopBar;

public class QuestMapView extends MapActivity{
	
	
	private class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==showListBtnId){
				Intent showListView = new Intent(QuestMapView.this, QuestListView.class);
				startActivity(showListView);
			}
			if(v.getId()==addQuestionBtnId){
				
			}			
		}
		
	}
	
	
	private int showListBtnId;
	private int addQuestionBtnId;
	
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
		setContentView(R.layout.quest_map);
		
		TopBar topBar = (TopBar)findViewById(R.id.topbar);
		Button showListBtn = topBar.addButtonLeftMost(this, "+");
		Button addQuestionBtn = topBar.addButtonLeftMost(this, "-");
		
		showListBtnId = showListBtn.getId();
		addQuestionBtnId = addQuestionBtn.getId();
		showListBtn.setOnClickListener(new ButtonListener());
		addQuestionBtn.setOnClickListener(new ButtonListener());
		
	    MapView questMap = (MapView) findViewById(R.id.QuestMap);
	    questMap.setBuiltInZoomControls(true);
	}
}
