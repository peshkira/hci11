package com.questo.android.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.questo.android.R;
import com.questo.android.model.Place;

public class QuestoMapOverlay extends ItemizedOverlay<QuestoOverlayItem> {

	private List<QuestoOverlayItem> items;
	private Context context;
	private Map<String, Place> selectedPlaces;
	private List<Place> nearbyPlaces;

	private RelativeLayout placeDetails;
	private Drawable normal;
	private Drawable selected;
	private boolean selectable;

	public QuestoMapOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		init();
	}

	public void setSelectionEnabled(boolean selectable) {
		this.selectable = selectable;
	}
	
	public boolean isSelectionEnabled(){
		return selectable;
	}
	
	public Map<String, Place> getSelectedPlaces(){
		return this.selectedPlaces;
	}

	private void init() {
		initIcons();
		items = new ArrayList<QuestoOverlayItem>();
		selectedPlaces = new HashMap<String, Place>();
		nearbyPlaces = new ArrayList<Place>();
	}

	private void initIcons() {
		this.normal = context.getResources().getDrawable(
				R.drawable.img_questo_q_stand);
		this.normal.setBounds(0, 0, this.normal.getIntrinsicWidth(),
				this.normal.getIntrinsicHeight());
		this.normal = this.boundCenterBottom(this.normal);
		this.selected = context.getResources().getDrawable(
				R.drawable.img_questo_q_stand_new);
		this.selected.setBounds(0, 0, this.selected.getIntrinsicWidth(),
				this.selected.getIntrinsicHeight());
		this.selected = this.boundCenterBottom(this.selected);
	}

	private synchronized void refreshOverlayItems() {
		this.items.clear();
		for (Place place : nearbyPlaces) {
			GeoPoint placeLocation = new GeoPoint(
					(int) (place.getLatitude() * 1E6),
					(int) (place.getLongitude() * 1E6));
			QuestoOverlayItem overlayItem = new QuestoOverlayItem(
					placeLocation, place.getName(), "");
			overlayItem.setDrawables(this.normal, this.selected);
			overlayItem.setPlace(place);
			if (selectedPlaces.containsKey(place.getUuid())) {
				overlayItem.toggleSelected();
			}
			this.items.add(overlayItem);
		}
		setLastFocusedIndex(-1);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		if (selectable) {
			QuestoOverlayItem item = items.get(index);
			Place place = nearbyPlaces.get(index);
			item.toggleSelected();
			if (item.isSelected())
				selectedPlaces.put(place.getUuid(), place);
			else
				selectedPlaces.remove(place.getUuid());

			populate();
		}
		return true;
	}

	@Override
	public synchronized int size() {
		return this.items.size();
	}

	public List<QuestoOverlayItem> getItems() {
		return this.items;
	}

	@Override
	protected QuestoOverlayItem createItem(int index) {
		return this.items.get(index);
	}
}
