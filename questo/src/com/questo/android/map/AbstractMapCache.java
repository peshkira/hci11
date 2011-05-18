package com.questo.android.map;

import java.util.Collection;
import java.util.Map;

import com.google.android.maps.GeoPoint;
import com.questo.android.model.Place;

public abstract class AbstractMapCache {
	private GeoPoint location;
	private int zoom;

	public GeoPoint getLocation() {
		return location;
	}

	public void setLocation(GeoPoint location) {
		this.location = location;
		onLocationChange();
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public abstract Map<String, Place> getPlaceMap();

	public abstract Collection<Place> getPlaces();
	
	public abstract void onLocationChange();
}
