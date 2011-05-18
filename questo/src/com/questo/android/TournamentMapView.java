package com.questo.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.maps.MapActivity;
import com.questo.android.view.QuestoMapView;
import com.questo.android.view.TopBar;

public class TournamentMapView extends MapActivity {

	public final static int ADD_PLACE_REQUEST_CODE = 1;
	public final static String EXTRA_PLACE_UUID_ARRAY = "EXTRA_PLACE_UUID_ARRAY";

	private QuestoMapView questMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		receivePlaces();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void initView() {
		setContentView(R.layout.tournament_map_view);
		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		Button doneBtn = (Button) findViewById(R.id.TournamentMapDoneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent tournamentView = new Intent(TournamentMapView.this,
						NewTournamentView.class);
				tournamentView.putExtra(EXTRA_PLACE_UUID_ARRAY,
						TournamentMapView.this.getSelectedPlaces());
				TournamentMapView.this.setResult(RESULT_OK, tournamentView);
				TournamentMapView.this.finish();
			}
		});
		initMapView();
	}

	private void initMapView() {
		questMap = (QuestoMapView) findViewById(R.id.TournamentMap);
		questMap.setShowDetails(false);
		questMap.setSelectionEnabled(true);
		LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.layout_zoom);
		View zoomView = questMap.getZoomButtonsController().getZoomControls();
		questMap.getZoomButtonsController().getContainer().removeView(zoomView);
		zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	private void receivePlaces() {
		String[] uuids = getIntent()
				.getStringArrayExtra(EXTRA_PLACE_UUID_ARRAY);
		if (uuids != null) {
			questMap.setSelectedPlacesUuid(uuids);
		}
	}

	private String[] getSelectedPlaces() {
		String[] s = new String[0];
		List<String> uuids = this.questMap.getSelectedPlacesUuid();
		return uuids.toArray(s);
	}
}
