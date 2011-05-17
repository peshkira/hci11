package com.questo.android.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.questo.android.App;
import com.questo.android.ModelManager;
import com.questo.android.R;
import com.questo.android.model.Place;

public class QuestoMapView extends MapView {

	private GeoPoint currentLocation;
	private ModelManager manager;
	private App application;
	private QuestoMapOverlay overlay;
	private MyLocationOverlay locationOverlay;
	private static final String TAG = "ModelManager";

	public QuestoMapView(Context context, AttributeSet attributes) {
		super(context, attributes);
		init();
	}

	private void init(){
		try{
			application = (App)getContext().getApplicationContext();
			manager = application.getModelManager();
			Drawable defMarker = getContext().getResources().getDrawable(
						R.drawable.img_questo_q_stand);
			this.overlay = new QuestoMapOverlay(defMarker);
			this.locationOverlay = new MyLocationOverlay(this.getContext(), this);
			this.getOverlays().add(overlay);
			this.getOverlays().add(locationOverlay);
		}
		catch(Exception e){
			Log.e(TAG, "Could not innitialize questo map view");
			e.printStackTrace();
		}
	}
	
	public void setSelectionEnabled(boolean selectable){
		this.overlay.setSelectionEnabled(true);
	}
	
	public boolean isSelectionEnabled(){
		return overlay.isSelectionEnabled();
	}
	
	public Collection<Place> getSelectedPlaces(){
		return overlay.getSelectedPlaces().values();
	}
	
	public List<String> getSelectedPlacesUuid(){
		List<String> placeList = new ArrayList<String>();

		Collection<Place> places = getSelectedPlaces();
		for(Place place: places){
			placeList.add(place.getUuid());
		}
		
		return placeList;
	}
}
