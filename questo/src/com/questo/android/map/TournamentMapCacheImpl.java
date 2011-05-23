package com.questo.android.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.android.maps.GeoPoint;
import com.questo.android.ModelManager;
import com.questo.android.model.Place;
import com.questo.android.model.Tournament;

public class TournamentMapCacheImpl extends AbstractMapCache {
	private Map<String, Place> places;
	private ModelManager manager;
	private Tournament tournament;
	
	public TournamentMapCacheImpl(ModelManager manager, Tournament tournament){
		this.manager = manager;		
		this.tournament = tournament;
		places = new HashMap<String, Place>();
		for(Place place : manager.getPlacesForTournament(tournament)) {
			places.put(place.getUuid(), place);
		}
	}

	@Override
	public synchronized Map<String, Place> getPlaceMap() {
		return places;
	}

	@Override
	public synchronized Collection<Place> getPlaces() {
		return places.values();
	}

	@Override
	protected void zoomChanged(int zoom) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	protected void dimensionsChanged(GeoPoint[] dimensions){
		// Nothing to do here, right?
	}

}
