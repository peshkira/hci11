package com.questo.android.view;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.questo.android.model.Place;

public class QuestoOverlayItem extends OverlayItem{

	private boolean isSelected = false;
	private Drawable normal;
	private Drawable selected;
	private Place place;
	
	public QuestoOverlayItem(GeoPoint point, String title,
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
