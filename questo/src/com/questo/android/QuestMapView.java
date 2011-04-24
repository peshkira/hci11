package com.questo.android;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.questo.android.view.QuestoMapOverlay;
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
		
		initMapView();
	}
	
	private void initMapView(){
	    MapView questMap = (MapView) findViewById(R.id.QuestMap);
	    questMap.setBuiltInZoomControls(true);
	    List<Overlay> mapOverlays = questMap.getOverlays();
	    Drawable target = this.getResources().getDrawable(R.drawable.arrow_target);
	    
	    QuestoMapOverlay overlay = new QuestoMapOverlay(target);
	    mapOverlays.add(overlay);
	}
}
