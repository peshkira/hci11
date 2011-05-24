package com.questo.android.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.maps.GeoPoint;
import com.questo.android.ModelManager;
import com.questo.android.model.Place;
import com.questo.android.model.Tournament;

public class TournamentMapCacheImpl extends AbstractMapCache {
	private Map<String, Place> places;
	private ModelManager manager;
	private Tournament tournament;
	private List<Place> placesDoneInTournament;
	
	public TournamentMapCacheImpl(ModelManager manager, Tournament tournament, List<Place> placesDoneInTournament){
		this.manager = manager;		
		this.tournament = tournament;
		places = new HashMap<String, Place>();
		for(Place place : manager.getPlacesForTournament(tournament)) {
			places.put(place.getUuid(), place);
		}
		this.placesDoneInTournament = placesDoneInTournament;
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
	
	public Tournament getTournament() {
		return this.tournament;
	}
	
	public List<Place> getPlacesDoneInTournament() {
		return this.placesDoneInTournament; 
	}

}
