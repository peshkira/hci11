package com.questo.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.questo.android.common.Constants;
import com.questo.android.model.Place;
import com.questo.android.view.TopBar;

public class TournamentMapView extends MapActivity {

	public final static int ADD_PLACE_REQUEST_CODE = 1;
	public final static String EXTRA_PLACE_UUID_ARRAY = "EXTRA_PLACE_UUID_ARRAY";

	private MapView questMap;
	private List<Place> nearbyPlaces;
	private GeoPoint currentLocation;
	private ModelManager modelManager;
	private App application;
	private MapOverlay overlay;
	private MyLocationOverlay myLocationOverlay;
	private Map<String, Place> selectedPlaces;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.selectedPlaces = new HashMap<String, Place>();
		this.application = (App) getApplicationContext();
		this.modelManager = application.getModelManager();
		initView();
		receivePlaces();
		this.currentLocation = this.questMap.getMapCenter();
		refreshMap();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void initView() {
		setContentView(R.layout.tournament_map_view);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		Button doneBtn = (Button)findViewById(R.id.TournamentMapDoneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent tournamentView = new Intent(TournamentMapView.this, NewTournamentView.class);
				String[] selected = TournamentMapView.this.getSelectedPlaces();
				tournamentView.putExtra(EXTRA_PLACE_UUID_ARRAY, TournamentMapView.this.getSelectedPlaces());
				TournamentMapView.this.setResult(RESULT_OK, tournamentView);
				TournamentMapView.this.finish();
			}
		});
		
		initMapView();
	}
	
	private void initMapView() {
		this.questMap = (MapView) findViewById(R.id.TournamentMap);
		questMap.setBuiltInZoomControls(true);
		List<Overlay> mapOverlays = questMap.getOverlays();
		Drawable target = this.getResources().getDrawable(
				R.drawable.img_questo_q_stand);

		this.overlay = new MapOverlay(target);
		this.myLocationOverlay = new MyLocationOverlay(this, this.questMap);
		this.myLocationOverlay.enableMyLocation();
		this.myLocationOverlay.enableCompass();
		mapOverlays.add(this.overlay);
		mapOverlays.add(this.myLocationOverlay);

		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		this.questMap.getController().setZoom(18);
	}

	private void refreshMap() {
		if (this.currentLocation != null) {
			this.nearbyPlaces = TournamentMapView.this.modelManager
					.getPlacesNearby(
							this.currentLocation.getLatitudeE6() / 1e6,
							this.currentLocation.getLongitudeE6() / 1e6);
			this.overlay.refreshOverlayItems();
			this.questMap.invalidate();
		}
	}
	
	private void receivePlaces(){
//		Intent tournamentView = new Intent(TournamentMapView.this, NewTournamentView.class);
//		String[] selected = TournamentMapView.this.getSelectedPlaces();
//		tournamentView.putExtra(Constants.EXTRA_COMPANION_UUID_ARRAY, TournamentMapView.this.getSelectedPlaces());
//		TournamentMapView.this.setResult(RESULT_OK, tournamentView);
//		TournamentMapView.this.finish();
		String[] uuids = getIntent().getStringArrayExtra(Constants.EXTRA_COMPANION_UUID_ARRAY);
		if(uuids!=null){
			setSelectedPlaces(uuids);	
		}
	}
	
	private String[] getSelectedPlaces(){
		List<String> uuids = new ArrayList<String>();
		for(TournamentOverlayItem item: this.overlay.getItems()){
			if(item.isSelected())
				uuids.add(item.getPlace().getUuid());
		}
		
		String[] s = new String[0];
		return uuids.toArray(s);
	}
	
	private void setSelectedPlaces(String[] uuids){
		ModelManager manager = ((App)this.getApplication()).getModelManager();
		for(String uuid: uuids){
			Place place = manager.getGenericObjectByUuid(uuid, Place.class);
			this.selectedPlaces.put(uuid, place);
		}
	}

	private class TournamentOverlayItem extends OverlayItem {

		private boolean isSelected = false;
		private Drawable normal;
		private Drawable selected;
		private Place place;
		
		public TournamentOverlayItem(GeoPoint point, String title,
				String snippet) {
			super(point, title, snippet);
		}
		
		public void setDrawables(Drawable normal, Drawable selected){
			this.normal = normal;
			this.selected = selected;
		}

		public boolean isSelected() {
			return this.isSelected;
		}

		public void toggleSelected(){
			this.isSelected = !this.isSelected;
		}

		public Place getPlace() {
			return place;
		}

		public void setPlace(Place place) {
			this.place = place;
		}

		@Override
		public Drawable getMarker(int stateBitset) {
			if(this.isSelected())
				return this.selected;
			else
				return this.normal;
		}
	}

	public class MapOverlay extends ItemizedOverlay<TournamentOverlayItem> {

		private List<TournamentOverlayItem> items;
		private RelativeLayout placeDetails;
		private Drawable normal;
		private Drawable selected;

		public MapOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
			this.normal = TournamentMapView.this.getResources().getDrawable(
					R.drawable.img_questo_q_stand);
			this.normal.setBounds(0, 0, this.normal.getIntrinsicWidth(), this.normal.getIntrinsicHeight());
			this.normal = this.boundCenterBottom(this.normal);
			this.selected = TournamentMapView.this.getResources().getDrawable(
					R.drawable.img_questo_q_stand_new);
			this.selected.setBounds(0, 0, this.selected.getIntrinsicWidth(), this.selected.getIntrinsicHeight());
			this.selected = this.boundCenterBottom(this.selected);

			this.items = new ArrayList<TournamentOverlayItem>();
		}

		private synchronized void refreshOverlayItems() {
			this.items.clear();
			for (Place place : TournamentMapView.this.nearbyPlaces) {
				GeoPoint placeLocation = new GeoPoint(
						(int) (place.getLatitude() * 1E6),
						(int) (place.getLongitude() * 1E6));
				TournamentOverlayItem overlayItem = new TournamentOverlayItem(
						placeLocation, place.getName(), "");
				overlayItem.setDrawables(this.normal, this.selected);
				overlayItem.setPlace(place);
				if(TournamentMapView.this.selectedPlaces.containsKey(place.getUuid())){
					overlayItem.toggleSelected();
				}
				this.items.add(overlayItem);
			}

			setLastFocusedIndex(-1);
			populate();
		}

		@Override
		protected TournamentOverlayItem createItem(int index) {
			return this.items.get(index);
		}

		@Override
		public synchronized int size() {
			return this.items.size();
		}
		
		public List<TournamentOverlayItem> getItems(){
			return this.items;
		}

		@Override
		protected boolean onTap(int index) {
			TournamentOverlayItem item = items.get(index);
			Place place = TournamentMapView.this.nearbyPlaces.get(index);
			item.toggleSelected();
			TournamentMapView.this.selectedPlaces.put(place.getUuid(), place);

			populate();
			return true;
		}
	}
}
