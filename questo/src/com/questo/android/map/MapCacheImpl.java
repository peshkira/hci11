package com.questo.android.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.questo.android.model.Place;

public class MapCacheImpl extends AbstractMapCache {
	private Map<String, Place> places;
	
	public MapCacheImpl(){
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
	public void onLocationChange() {
		// TODO Auto-generated method stub
	}

}
