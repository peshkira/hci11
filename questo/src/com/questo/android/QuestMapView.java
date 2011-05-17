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
import android.view.MenuItem.OnMenuItemClickListener;
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
import com.questo.android.helper.DisplayHelper;
import com.questo.android.model.Place;
import com.questo.android.view.QuestoMapView;
import com.questo.android.view.TopBar;

public class QuestMapView extends MapActivity {

	public final static int ADD_PLACE_REQUEST_CODE = 1;
	public final static int ADD_QUESTION_REQUEST_CODE = 2;

	private QuestoMapView questMap;
	private List<Place> nearbyPlaces;
	private GeoPoint currentLocation;
	private Place currentPlace;
	private ModelManager modelManager;
	private App application;
//	private MapOverlay overlay;
	private MyLocationOverlay myLocationOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.application = (App) getApplicationContext();
		this.modelManager = application.getModelManager();
		initView();
		this.currentLocation = this.questMap.getMapCenter();
//		refreshMap();		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void initView() {
		setContentView(R.layout.quest_map);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		Button addQuestionBtn = topBar.addButtonLeftMost(
				this.getApplicationContext(), "+");
		Button showListBtn = topBar.addToggleButtonLeftMost(
				this.getApplicationContext(), "List", false);

		showListBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent showListView = new Intent(QuestMapView.this,
						QuestListView.class);
				startActivity(showListView);
			}
		});
		addQuestionBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QuestMapView.this.openContextMenu(v);
			}
		});
		this.registerForContextMenu(addQuestionBtn);

		initMapView();
	}

	private void initMapView() {
		this.questMap = (QuestoMapView) findViewById(R.id.QuestMap);
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, HomeView.class));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		MenuItem addPlace = menu.add("Add Place");
		addPlace.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent addPlaceIntent = new Intent(QuestMapView.this,
						AddPlace.class);
				startActivityForResult(addPlaceIntent, ADD_PLACE_REQUEST_CODE);
				return true;
			}
		});

		MenuItem addQuestion = menu.add("Add Question");
		addQuestion.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (QuestMapView.this.currentPlace != null) {
					String uuid = QuestMapView.this.currentPlace.getUuid();
					Intent addQuestionIntent = new Intent(QuestMapView.this,
							AddQuestion.class);
					addQuestionIntent.putExtra(
							Constants.EXTRA_ADD_QUESTION_PLACE_UUID, uuid);
					startActivityForResult(addQuestionIntent,
							ADD_QUESTION_REQUEST_CODE);
					return true;
				}
				return false;
			}
		});
	}

	// public boolean onContextItemSelected(MenuItem item) {
	//
	// if (item.getTitle().equals("Add Place")) {
	//
	// }
	// if (item.getTitle().equals("Add Question")) {
	// if(QuestMapView.this.currentPlace!=null){
	//
	// }
	// }
	//
	// return super.onContextItemSelected(item);
	//
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		this.refreshMap();
	}

//	public class MapOverlay extends ItemizedOverlay<OverlayItem> {
//
//		private List<OverlayItem> items;
//		private RelativeLayout placeDetails;
//
//		public MapOverlay(Drawable defaultMarker) {
//			super(boundCenterBottom(defaultMarker));
//
//			this.items = new ArrayList<OverlayItem>();
//		}
//
//		private synchronized void refreshOverlayItems() {
//			this.items.clear();
//			for (Place place : QuestMapView.this.nearbyPlaces) {
//				GeoPoint placeLocation = new GeoPoint(
//						(int) (place.getLatitude() * 1E6),
//						(int) (place.getLongitude() * 1E6));
//				OverlayItem overlayItem = new OverlayItem(placeLocation,
//						place.getName(), "");
//				this.items.add(overlayItem);
//			}
//
//			setLastFocusedIndex(-1);
//			populate();
//		}
//
//		@Override
//		protected OverlayItem createItem(int index) {
//			return this.items.get(index);
//		}
//
//		@Override
//		public synchronized int size() {
//			return this.items.size();
//		}
//
//		@Override
//		protected boolean onTap(int index) {
//			OverlayItem item = items.get(index);
//			Place place = QuestMapView.this.nearbyPlaces.get(index);
//			int questionCount = 0;
//			if (place.getQuestions() != null) {
//				questionCount = place.getQuestions().size();
//			}
//
//			QuestMapView.this.currentPlace = place;
//
//			if (this.placeDetails == null) {
//				LayoutInflater inflater = (LayoutInflater) QuestMapView.this
//						.getApplicationContext().getSystemService(
//								Context.LAYOUT_INFLATER_SERVICE);
//				this.placeDetails = (RelativeLayout) inflater.inflate(
//						R.layout.quest_map_item, null);
//				this.placeDetails.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (QuestMapView.this.currentPlace != null) {
//							Intent placeDetails = new Intent(QuestMapView.this,
//									PlaceDetailsView.class);
//							placeDetails.putExtra(
//									Constants.TRANSITION_OBJECT_UUID,
//									QuestMapView.this.currentPlace.getUuid());
//							startActivity(placeDetails);
//						}
//					}
//				});
//			}
////			this.togglePlaceDetails();
//			this.placeDetails.setVisibility(View.VISIBLE);
//			QuestMapView.this.questMap.removeView(this.placeDetails);
//			QuestMapView.this.questMap.addView(this.placeDetails,
//					new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
//							MapView.LayoutParams.WRAP_CONTENT, item.getPoint(),
//							0, DisplayHelper.dpToPixel(-57, QuestMapView.this),
//							MapView.LayoutParams.BOTTOM_CENTER));
//
//			TextView placeNameText = (TextView) this.placeDetails
//					.findViewById(R.id.QuestMapPlaceDetailsName);
//			TextView questionCountText = (TextView) this.placeDetails
//					.findViewById(R.id.QuestMapPlaceDetailsQuestionCount);
//			placeNameText.setText(place.getName());
//			questionCountText.setText("Questions: "
//					+ Integer.toString(questionCount));
//
//			return true;
//		}
//
//		private void togglePlaceDetails() {
//			if (this.placeDetails != null) {
//				if (this.placeDetails.getVisibility() == View.INVISIBLE)
//					this.placeDetails.setVisibility(View.VISIBLE);
//				else
//					this.placeDetails.setVisibility(View.INVISIBLE);
//			}
//		}
//	}

	private class MapListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (QuestMapView.this.questMap != null) {
				// GeoPoint current = new GeoPoint(
				// (int) (location.getLatitude() * 1e6),
				// (int) (location.getLongitude() * 1e6));
				// QuestMapView.this.questMap.getController().setCenter(current);
				// QuestMapView.this.currentLocation = current;
//				QuestMapView.this.refreshMap();
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
