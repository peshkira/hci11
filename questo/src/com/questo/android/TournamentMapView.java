package com.questo.android;

import java.util.ArrayList;
import java.util.List;

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
import com.questo.android.model.Place;
import com.questo.android.view.TopBar;

public class TournamentMapView extends MapActivity {

	public final static int ADD_PLACE_REQUEST_CODE = 1;

	private MapView questMap;
	private List<Place> nearbyPlaces;
	private GeoPoint currentLocation;
	private Place currentPlace;
	private ModelManager modelManager;
	private App application;
	private MapOverlay overlay;
	private MyLocationOverlay myLocationOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.application = (App) getApplicationContext();
		this.modelManager = application.getModelManager();
		initView();
		this.currentLocation = this.questMap.getMapCenter();
		refreshMap();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void initView() {
		setContentView(R.layout.quest_map);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		initMapView();
	}

	private void initMapView() {
		this.questMap = (MapView) findViewById(R.id.QuestMap);
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

	private class TournamentOverlayItem extends OverlayItem {

		public final static int ITEM_STATE_NORMAL = 0;
		public final static int ITEM_STATE_SELECTED = 1;
		private int state;

		public TournamentOverlayItem(GeoPoint point, String title,
				String snippet) {
			super(point, title, snippet);
			this.state = TournamentOverlayItem.ITEM_STATE_NORMAL;
		}

		public int getState() {
			return this.state;
		}

		public void setState(int state) {
			this.state = state;
		}
	}

	public class MapOverlay extends ItemizedOverlay<TournamentOverlayItem> {

		private List<TournamentOverlayItem> items;
		private RelativeLayout placeDetails;

		public MapOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));

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

		@Override
		protected boolean onTap(int index) {
			TournamentOverlayItem item = items.get(index);
			Place place = TournamentMapView.this.nearbyPlaces.get(index);
			if (item.getState() == TournamentOverlayItem.ITEM_STATE_NORMAL) {
				item.setState(TournamentOverlayItem.ITEM_STATE_SELECTED);
			}
			if (item.getState() == TournamentOverlayItem.ITEM_STATE_SELECTED) {
				item.setState(TournamentOverlayItem.ITEM_STATE_NORMAL);
			}

			return true;
		}
	}
}
