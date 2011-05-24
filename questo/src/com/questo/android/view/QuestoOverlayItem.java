package com.questo.android.view;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.questo.android.model.Place;

public class QuestoOverlayItem extends OverlayItem{

	private boolean isSelected = false;
	private boolean isDone = false;
	private boolean clickable;
	private Drawable normal;
	private Drawable selected;
	private Drawable done;
	private Place place;
	
	public QuestoOverlayItem(GeoPoint point, String title,
			String snippet) {
		super(point, title, snippet);
		this.clickable = true;
	}
	
	public void setDrawables(Drawable normal, Drawable selected, Drawable done){
		this.normal = normal;
		this.selected = selected;
		this.done = done;
	}

	public boolean isSelected() {
		return this.isSelected;
	}
	
	public boolean isDone() {
		return this.isDone;
	}

	public void toggleSelected(){
		this.isSelected = !this.isSelected;
	}
	
	public void setDone(boolean done) {
		this.isDone = done;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
	
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
	
	public boolean isClickable() {
		return clickable;
	}

	@Override
	public Drawable getMarker(int stateBitset) {
		if(this.isSelected())
			return this.selected;
		else if (this.isDone())
			return this.done;
		else
			return this.normal;
	}
}
