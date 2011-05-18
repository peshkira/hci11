package com.questo.android.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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
	private QuestoMapListener mapListener;
	private static final String TAG = "ModelManager";

	public QuestoMapView(Context context, AttributeSet attributes) {
		super(context, attributes);
		init();
	}

	private void init() {
		try {
			application = (App) getContext().getApplicationContext();
			manager = application.getModelManager();
			Drawable defMarker = getContext().getResources().getDrawable(
					R.drawable.img_questo_q_stand);
			this.overlay = new QuestoMapOverlay(getContext(), this, defMarker);
			this.locationOverlay = new MyLocationOverlay(this.getContext(),
					this);
			getOverlays().add(overlay);
			getOverlays().add(locationOverlay);

			mapListener = new QuestoMapListener();
			LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, mapListener);
			setBuiltInZoomControls(true);
//			getController().setZoom(18);
		} catch (Exception e) {
			Log.e(TAG, "Could not initialize questo map view");
			e.printStackTrace();
		}
	}

	public void setSelectionEnabled(boolean selectable) {
		this.overlay.setSelectionEnabled(true);
	}

	public boolean isSelectionEnabled() {
		return overlay.isSelectionEnabled();
	}
	
	public Collection<Place> getSelectedPlaces() {
		return overlay.getSelectedPlaces().values();
	}

	public boolean isShowDetails() {
		return overlay.isShowDetails();
	}

	public void setShowDetails(boolean showDetails) {
		overlay.setShowDetails(showDetails);
	}
	
	public List<String> getSelectedPlacesUuid() {
		List<String> placeList = new ArrayList<String>();

		Collection<Place> places = getSelectedPlaces();
		for (Place place : places) {
			placeList.add(place.getUuid());
		}

		return placeList;
	}

	private class QuestoMapListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			GeoPoint current = new GeoPoint(
					(int) (location.getLatitude() * 1e6),
					(int) (location.getLongitude() * 1e6));
			QuestoMapView.this.getController().setCenter(current);
			QuestoMapView.this.currentLocation = current;
			QuestoMapView.this.overlay.setLocation(current);
			QuestoMapView.this.overlay.refreshPlaces();
//			QuestoMapView.this.invalidate();
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
