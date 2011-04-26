package com.questo.android;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.questo.android.view.QuestoMapOverlay;
import com.questo.android.view.TopBar;

public class QuestMapView extends MapActivity{
	
	
	private class MapListener implements OnClickListener, LocationListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==showListBtnId){
				Intent showListView = new Intent(QuestMapView.this, QuestListView.class);
				startActivity(showListView);
			}
			if(v.getId()==addQuestionBtnId){
				
			}			
		}

		@Override
		public void onLocationChanged(Location location) {
			if(QuestMapView.this.questMap!=null){
				GeoPoint current = new GeoPoint((int) (location.getLatitude() * 1e6), 
												(int) (location.getLongitude() * 1e6));
				QuestMapView.this.questMap.getController().setCenter(current);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	}
	
	
	private MapView questMap;
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
		showListBtn.setOnClickListener(new MapListener());
		addQuestionBtn.setOnClickListener(new MapListener());
		
		initMapView();
	}
	
	private void initMapView(){
	    this.questMap = (MapView) findViewById(R.id.QuestMap);
	    questMap.setBuiltInZoomControls(true);
	    List<Overlay> mapOverlays = questMap.getOverlays();
	    Drawable target = this.getResources().getDrawable(R.drawable.questo_q_stand);
	    
	    QuestoMapOverlay overlay = new QuestoMapOverlay(target);
	    mapOverlays.add(overlay);
	    
	    LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new MapListener());
	}
}
