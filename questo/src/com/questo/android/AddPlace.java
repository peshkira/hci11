package com.questo.android;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.questo.android.model.Place;

public class AddPlace extends MapActivity {

	private Place place;
	private ModelManager modelManager;
	private MapView map;
	private AddPlaceOverlay overlay;
	private AddPlaceListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.place = new Place();
		this.modelManager = ((App) getApplication()).getModelManager();
		this.initView();
	}

	private void initView() {
		this.setContentView(R.layout.add_place);
		this.map = (MapView) findViewById(R.id.AddPlaceMap);

		this.map.setBuiltInZoomControls(true);
		List<Overlay> mapOverlays = map.getOverlays();
		Drawable target = this.getResources().getDrawable(
				R.drawable.img_questo_q_stand);

		this.overlay = new AddPlaceOverlay(target);
		this.map.getOverlays().add(overlay);
		this.listener = new AddPlaceListener();

		Button cancelBtn = (Button) findViewById(R.id.AddPlaceCancelBtn);
		Button createBtn = (Button) findViewById(R.id.AddPlaceCreateBtn);
		cancelBtn.setOnClickListener(this.listener);
		createBtn.setOnClickListener(this.listener);

		this.map.getController().setZoom(18);
	}

	private void refreshObject() {
		EditText nameEdit = (EditText) findViewById(R.id.AddPlaceName);
		if (nameEdit != null) {
			this.place.setName(nameEdit.getText().toString());
		}
		GeoPoint location = this.overlay.marker.getPoint();
		this.place.setLatitude(location.getLatitudeE6() / 1E6);
		this.place.setLongitude(location.getLongitudeE6() / 1E6);
	}

	private class AddPlaceListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.AddPlaceCreateBtn) {
				AddPlace.this.refreshObject();
				AddPlace.this.modelManager.create(AddPlace.this.place,
						Place.class);
				AddPlace.this.finish();
			}
			if (v.getId() == R.id.AddPlaceCancelBtn) {
				AddPlace.this.finish();
			}
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public class AddPlaceOverlay extends ItemizedOverlay<OverlayItem> {

		private OverlayItem marker;
		private GeoPoint location;

		public AddPlaceOverlay(Drawable marker) {
			super(boundCenter(marker));
			this.location = AddPlace.this.map.getMapCenter();
			this.marker = new OverlayItem(this.location, "New Place", "");
			populate();
		}

		public synchronized void refresh(GeoPoint location) {
			this.location = location;
			this.marker = new OverlayItem(this.location, "New Place", "");
			// setLastFocusedIndex(-1);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return this.marker;
		}

		@Override
		public synchronized int size() {
			return 1;
		}

		@Override
		public boolean onTouchEvent(android.view.MotionEvent event,
				MapView mapView) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				GeoPoint location = AddPlace.this.map.getProjection()
						.fromPixels((int) event.getX(), (int) event.getY());
				AddPlace.this.overlay.refresh(location);
				return true;
			}
			return false;
		}
	}
}
