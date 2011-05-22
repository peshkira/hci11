package com.questo.android.map;

import java.util.Collection;
import java.util.Map;

import com.google.android.maps.GeoPoint;
import com.questo.android.ModelManager;
import com.questo.android.model.Place;

public abstract class AbstractMapCache {
	private GeoPoint location;
	private GeoPoint[] dimensions;
	private int zoom;
	
	
	public AbstractMapCache(){
		dimensions = new GeoPoint[2];
	}

	public GeoPoint getLocation() {
		return location;
	}

	public void setLocation(GeoPoint location) {
		GeoPoint previousLocation = this.location;
		this.location = location;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
		zoomChanged(zoom);
	}
	
	public void setViewDimensions(GeoPoint leftUpper, GeoPoint rightBottom){
		dimensions[0] = leftUpper;
		dimensions[1] = rightBottom;
		dimensionsChanged(dimensions);
	}
	
	public GeoPoint[] getViewDimensions(){
		return dimensions;
	}

	public abstract Map<String, Place> getPlaceMap();

	public abstract Collection<Place> getPlaces();
	
	protected abstract void zoomChanged(int zoom);
	
	protected abstract void dimensionsChanged(GeoPoint[] dimensions);
}