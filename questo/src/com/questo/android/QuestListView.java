package com.questo.android;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.questo.android.model.Place;
import com.questo.android.view.TopBar;

public class QuestListView extends Activity {

	private int addQuestionBtnId;
	private int showMapBtnId;
	private GeoPoint currentLocation;
	private ModelManager modelManager;
	private App application;
	private List<Place> nearbyPlaces;

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
		Button addQuestionBtn = topBar.addButtonLeftMost(this, "+");
		Button showMapBtn = topBar.addButtonLeftMost(this, "m");

		addQuestionBtnId = addQuestionBtn.getId();
		showMapBtnId = showMapBtn.getId();
		addQuestionBtn.setOnClickListener(new ButtonListener());
		showMapBtn.setOnClickListener(new ButtonListener());

		ListView questList = (ListView) findViewById(R.id.QuestList);
		String[] listContent = new String[] { "foo", "bla" };
		// ListAdapter adapter = new ArrayAdapter<String>(this,
		// R.layout.quest_list_item, listContent);
		// questList.setAdapter(adapter);
		QuestListAdapter adapter = new QuestListAdapter(this,
				R.layout.quest_list_item);
		questList.setAdapter(adapter);

		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new ButtonListener());
	}
	
	private void refreshList() {
		this.nearbyPlaces = this.modelManager.getPlacesNearby(
				this.currentLocation.getLatitudeE6() / 1e6,
				this.currentLocation.getLongitudeE6() / 1e6);
	}

	private class ButtonListener implements OnClickListener, LocationListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == showMapBtnId) {
				Intent showMapView = new Intent(QuestListView.this,
						QuestMapView.class);
				startActivity(showMapView);
			}
			if (v.getId() == addQuestionBtnId) {

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
				if (distanceText != null) {
					distanceText.setText("unknown");
				}
				if ((current.getQuestions() != null)
						&& (questCountText != null)) {
					questCountText.setText(current.getQuestions().size());
				}
			}

			return v;
		}

	}
}
