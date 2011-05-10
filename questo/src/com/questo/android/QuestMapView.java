package com.questo.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class QuestMapView extends MapActivity {

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
		Button addQuestionBtn = topBar.addButtonLeftMost(this, "+");
		Button showListBtn = topBar.addButtonLeftMost(this, "-");

		showListBtn.setOnClickListener(new MapListener());
		addQuestionBtn.setOnClickListener(new MapListener());
		this.registerForContextMenu(addQuestionBtn);

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
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new MapListener());

		this.questMap.getController().setZoom(18);
	}

	public void onBackPressed() {
		startActivity(new Intent(this, HomeView.class));
	}

	private void refreshMap() {
		this.nearbyPlaces = QuestMapView.this.modelManager.getPlacesNearby(
				this.currentLocation.getLatitudeE6() / 1e6,
				this.currentLocation.getLongitudeE6() / 1e6);
		this.overlay.refreshOverlayItems();
		this.questMap.invalidate();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		menu.add("Add Place");
		menu.add("Add Question");
	}

	public boolean onContextItemSelected(MenuItem item) {

		if (item.getTitle().equals("Add Place")) {
			Intent addPlaceIntent = new Intent(this, AddPlace.class);
			startActivity(addPlaceIntent);
		}
		if (item.getTitle().equals("Add Question")) {
			Intent addQuestionIntent = new Intent(this, AddQuestion.class);
			startActivity(addQuestionIntent);
		}

		return super.onContextItemSelected(item);

	}

	public class MapOverlay extends ItemizedOverlay<OverlayItem> {

		private List<OverlayItem> items;
		private RelativeLayout placeDetails;

		public MapOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));

			this.items = new ArrayList<OverlayItem>();
		}

		private synchronized void refreshOverlayItems() {
			this.items.clear();
			for (Place place : QuestMapView.this.nearbyPlaces) {
				GeoPoint placeLocation = new GeoPoint(
						(int) (place.getLatitude() * 1E6),
						(int) (place.getLongitude() * 1E6));
				OverlayItem overlayItem = new OverlayItem(placeLocation,
						place.getName(), "");
				this.items.add(overlayItem);
			}

			setLastFocusedIndex(-1);
			populate();
		}

		@Override
		protected OverlayItem createItem(int index) {
			return this.items.get(index);
		}

		@Override
		public synchronized int size() {
			return this.items.size();
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = items.get(index);
			Place place = QuestMapView.this.nearbyPlaces.get(index);
			int questionCount = 0;
			if (place.getQuestions() != null) {
				questionCount = place.getQuestions().size();
			}

			QuestMapView.this.currentPlace = place;
			
			if (this.placeDetails == null) {
				LayoutInflater inflater = (LayoutInflater) QuestMapView.this
						.getApplicationContext().getSystemService(
								Context.LAYOUT_INFLATER_SERVICE);
				this.placeDetails = (RelativeLayout) inflater.inflate(
						R.layout.quest_map_item, null);
				this.placeDetails.setOnClickListener(new MapListener());
			}
			QuestMapView.this.questMap.removeView(this.placeDetails);
			QuestMapView.this.questMap.addView(this.placeDetails,
					new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
							MapView.LayoutParams.WRAP_CONTENT, item.getPoint(),
							MapView.LayoutParams.BOTTOM_CENTER));

			TextView placeNameText = (TextView) this.placeDetails
					.findViewById(R.id.QuestMapPlaceDetailsName);
			TextView questionCountText = (TextView) this.placeDetails
					.findViewById(R.id.QuestMapPlaceDetailsQuestionCount);
			placeNameText.setText(place.getName());
			questionCountText.setText("Questions: "
					+ Integer.toString(questionCount));

			return true;
		}
	}

	private class MapListener implements OnClickListener, LocationListener {

		@Override
		public void onClick(View v) {
			if(v instanceof Button){
				Button pressedButton = (Button)v;
				if (pressedButton.getText().equals("-")) {
					Intent showListView = new Intent(QuestMapView.this,
							QuestListView.class);
					startActivity(showListView);
				}
				if (pressedButton.getText().equals("+")) {
					QuestMapView.this.openContextMenu(v);
				}
			}
			if(v.getId()==R.id.QuestMapPlaceDetailsLayout){
				if(QuestMapView.this.currentPlace!=null){
					Intent placeDetails = new Intent(QuestMapView.this, PlaceDetailsView.class);
					placeDetails.putExtra(Constants.TRANSITION_OBJECT_UUID, QuestMapView.this.currentPlace.getUuid());					
					startActivity(placeDetails);
				}
			}
		}

		@Override
		public void onLocationChanged(Location location) {
			if (QuestMapView.this.questMap != null) {
				GeoPoint current = new GeoPoint(
						(int) (location.getLatitude() * 1e6),
						(int) (location.getLongitude() * 1e6));
				QuestMapView.this.questMap.getController().setCenter(current);
				QuestMapView.this.currentLocation = current;
				QuestMapView.this.refreshMap();
			}
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
