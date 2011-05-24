package com.questo.android.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.questo.android.App;
import com.questo.android.ModelManager;
import com.questo.android.R;
import com.questo.android.common.Constants;
import com.questo.android.map.AbstractMapCache;
import com.questo.android.map.MapCacheImpl;
import com.questo.android.model.Place;

public class QuestoMapView extends MapView {

	private GeoPoint currentLocation;
	private ModelManager manager;
	private App application;
	private AbstractMapCache cache;
	private QuestoMapOverlay overlay;
	private MyLocationOverlay locationOverlay;
	private QuestoMapListener mapListener;
	private int oldZoomLevel;
	private GeoPoint oldLeftUpper, oldRightBottom;
	private Timer refreshTimer;
	private long lastRefreshTime;
	private static final String TAG = "ModelManager";

	public QuestoMapView(Context context, AttributeSet attributes) {
		super(context, attributes);
		init();
	}

	private void init() {
		try {
			application = (App) getContext().getApplicationContext();
			manager = application.getModelManager();
			cache = new MapCacheImpl(manager);
			Drawable defMarker = getContext().getResources().getDrawable(
					R.drawable.img_questo_q_stand);
			overlay = new QuestoMapOverlay(getContext(), this, defMarker);
			locationOverlay = new MyLocationOverlay(this.getContext(), this);
			locationOverlay.enableMyLocation();
			locationOverlay.enableCompass();
			getOverlays().add(overlay);
			getOverlays().add(locationOverlay);

			mapListener = new QuestoMapListener();
			initLocationUpdates();
			setBuiltInZoomControls(true);
		} catch (Exception e) {
			Log.e(TAG, "Could not initialize questo map view");
			e.printStackTrace();
		}
	}

	private void initLocationUpdates() {
		LocationManager locationManager = (LocationManager) getContext()
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, mapListener);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			GeoPoint current = new GeoPoint(
					(int) (location.getLatitude() * 1e6),
					(int) (location.getLongitude() * 1e6));
			QuestoMapView.this.getController().setCenter(current);
			QuestoMapView.this.currentLocation = current;
			QuestoMapView.this.overlay.setLocation(current);
			QuestoMapView.this.overlay.refreshPlaces();
		}
	}

	public void setSelectionEnabled(boolean selectable) {
		this.overlay.setSelectionEnabled(selectable);
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

	public AbstractMapCache getCache() {
		return cache;
	}

	public List<String> getSelectedPlacesUuid() {
		List<String> placeList = new ArrayList<String>();

		Collection<Place> places = getSelectedPlaces();
		for (Place place : places) {
			placeList.add(place.getUuid());
		}

		return placeList;
	}

	public void setSelectedPlacesUuid(String[] uuids) {
		overlay.setSelectedPlaces(uuids);
	}

	public synchronized void refresh() {
		overlay.refreshPlaces();
	}

	public synchronized void refreshWhenViewChanged() {
		boolean doRefresh = false;
		if (getZoomLevel() != oldZoomLevel)
			doRefresh = true;
		if (!getProjection().fromPixels(0, 0).equals(oldLeftUpper))
			doRefresh = true;
		if (!getProjection().fromPixels(getWidth(), getHeight()).equals(
				oldRightBottom))
			doRefresh = true;

		if (doRefresh) {
			refresh();

			oldZoomLevel = getZoomLevel();
			oldLeftUpper = getProjection().fromPixels(0, 0);
			oldRightBottom = getProjection()
					.fromPixels(getWidth(), getHeight());
		}
	}

	@Override
	public boolean onTouchEvent(android.view.MotionEvent ev) {
		if ((ev.getAction() == MotionEvent.ACTION_DOWN)
				|| (ev.getAction() == MotionEvent.ACTION_MOVE)) {
			long time = System.currentTimeMillis();
			if (time >= lastRefreshTime + Constants.MAP_REFRESH_PERIOD) {
				lastRefreshTime = time;
				Thread refreshThread = new Thread(new Runnable() {

					@Override
					public void run() {
						refresh();
					}
				});
				refreshThread.run();
			}
		}
		if ((ev.getAction() == MotionEvent.ACTION_UP)
				|| (ev.getAction() == MotionEvent.ACTION_CANCEL)
				|| (ev.getAction() == MotionEvent.ACTION_OUTSIDE)) {
			Thread refreshThread = new Thread(new Runnable() {

				@Override
				public void run() {
					refresh();
				}
			});
			refreshThread.run();
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		this.refresh();
	}

	private class QuestoMapListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
//			GeoPoint current = new GeoPoint(
//					(int) (location.getLatitude() * 1e6),
//					(int) (location.getLongitude() * 1e6));
//			QuestoMapView.this.getController().setCenter(current);
//			QuestoMapView.this.currentLocation = current;
//			QuestoMapView.this.overlay.setLocation(current);
//			QuestoMapView.this.overlay.refreshPlaces();
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
