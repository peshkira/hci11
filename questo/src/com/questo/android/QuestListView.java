package com.questo.android;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.questo.android.common.Constants;
import com.questo.android.model.Place;
import com.questo.android.view.TopBar;

public class QuestListView extends Activity {

	private int addQuestionBtnId;
	private int showMapBtnId;
	private GeoPoint currentLocation;
	private ModelManager modelManager;
	private App application;
	private List<Place> nearbyPlaces;
	private QuestListAdapter questListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.application = (App) getApplicationContext();
		this.modelManager = this.application.getModelManager();

		initView();
	}

	private void initView() {
		setContentView(R.layout.quest_list);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		Button addQuestionBtn = topBar.addButtonLeftMost(this.getApplicationContext(), "+", false);
		Button showMapBtn = topBar.addButtonLeftMost(this.getApplicationContext(), "m", false);

		addQuestionBtnId = addQuestionBtn.getId();
		showMapBtnId = showMapBtn.getId();
		addQuestionBtn.setOnClickListener(new QuestListListener());
		showMapBtn.setOnClickListener(new QuestListListener());
		registerForContextMenu(addQuestionBtn);

		ListView questList = (ListView) findViewById(R.id.QuestList);
		this.questListAdapter = new QuestListAdapter(this,
				R.layout.quest_list_item);
		questList.setAdapter(this.questListAdapter);

		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new QuestListListener());
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location!=null){
			this.currentLocation = new GeoPoint(
					(int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
		}

		questList.setOnItemClickListener(new QuestListListener());
		this.refreshList();
	}

	private void refreshList() {
		if(this.currentLocation!=null){
			this.nearbyPlaces = this.modelManager.getPlacesNearby(
					this.currentLocation.getLatitudeE6() / 1e6,
					this.currentLocation.getLongitudeE6() / 1e6);
			ListView questList = (ListView) findViewById(R.id.QuestList);
			this.questListAdapter.clear();
			for (Place place : this.nearbyPlaces) {
				this.questListAdapter.add(place);
			}
		}
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
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, HomeView.class));
	}

	private class QuestListListener implements OnClickListener,
			LocationListener, OnItemClickListener {

		@Override
		public void onClick(View v) {
			if (v instanceof Button) {
				Button btn = (Button) v;
				if (btn.getText().equals("m")) {
					Intent showMapView = new Intent(QuestListView.this,
							QuestMapView.class);
					startActivity(showMapView);
				}
				if (btn.getText().equals("+")) {
					QuestListView.this.openContextMenu(v);
				}
			}
		}

		@Override
		public void onLocationChanged(Location location) {
			QuestListView.this.currentLocation = new GeoPoint(
					(int) (location.getLatitude() * 1e6),
					(int) (location.getLongitude() * 1e6));
			QuestListView.this.refreshList();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Place place = QuestListView.this.nearbyPlaces.get(position);
			Intent placeDetails = new Intent(QuestListView.this,
					PlaceDetailsView.class);
			placeDetails.putExtra(Constants.TRANSITION_OBJECT_UUID,
					place.getUuid());
			startActivity(placeDetails);
		}

	}

	private class QuestListAdapter extends ArrayAdapter<Place> {

		public QuestListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) QuestListView.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.quest_list_item, null);
			}

			Place current = this.getItem(position);
			TextView nameText = (TextView) v
					.findViewById(R.id.QuestListNameText);
			TextView distanceText = (TextView) v
					.findViewById(R.id.QuestListDistance);
			TextView questCountText = (TextView) v
					.findViewById(R.id.QuestListQuestCount);

			if (current != null) {
				if (nameText != null) {
					nameText.setText(current.getName());
				}
				if ((distanceText != null) && (QuestListView.this.currentLocation != null)) {
					Location placeLocation = new Location("gps");
					placeLocation.setLatitude(current.getLatitude());
					placeLocation.setLongitude(current.getLongitude());
					Location currentLoc = new Location("gps");
					currentLoc.setLatitude(QuestListView.this.currentLocation
							.getLatitudeE6() / 1E6);
					currentLoc.setLongitude(QuestListView.this.currentLocation
							.getLongitudeE6() / 1E6);
					float distance = placeLocation.distanceTo(currentLoc);
					int roundedDistance = Math.round(distance);
					distanceText.setText(Integer.toString(roundedDistance)
							+ "m");
				}
				if ((current.getQuestions() != null)
						&& (questCountText != null)) {
					questCountText.setText(Integer.toString(current
							.getQuestions().size()) + " Questions");
				}
			}

			return v;
		}

	}
}
