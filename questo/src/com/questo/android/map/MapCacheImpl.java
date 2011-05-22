package com.questo.android.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.maps.GeoPoint;
import com.questo.android.ModelManager;
import com.questo.android.model.Place;

public class MapCacheImpl extends AbstractMapCache {
	private Map<String, Place> places;
	private ModelManager manager;
	
	public MapCacheImpl(ModelManager manager){
		this.manager = manager;		
		places = new HashMap<String, Place>();
	}

	@Override
	public Map<String, Place> getPlaceMap() {
		return places;
	}

	@Override
	public Collection<Place> getPlaces() {
		return places.values();
	}

	@Override
	protected void zoomChanged(int zoom) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	protected void dimensionsChanged(GeoPoint[] dimensions){
		double lowLatitude, lowLongitude, highLatitude, highLongitude;
		lowLatitude = (double)(dimensions[1].getLatitudeE6() / 1E6);
		lowLongitude = (double)(dimensions[1].getLongitudeE6() / 1E6);
		highLatitude = (double)(dimensions[0].getLatitudeE6() / 1E6);
		highLongitude = (double)(dimensions[0].getLongitudeE6() / 1E6);
		if(lowLatitude>highLatitude){
			double tmp = lowLatitude;
			lowLatitude = highLatitude;
			highLatitude = tmp;
		}
		if(lowLongitude>highLongitude){
			double tmp = lowLongitude;
			lowLongitude = highLongitude;
			highLongitude = tmp;
		}
		List<Place> placeList = manager.getPlacesBetween(lowLatitude, lowLongitude, highLatitude, highLongitude);
		places.clear();		
		for(Place place: placeList){
			places.put(place.getUuid(), place);
		}
	}

}
