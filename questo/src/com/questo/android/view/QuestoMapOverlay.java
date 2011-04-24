package com.questo.android.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class QuestoMapOverlay extends ItemizedOverlay<OverlayItem>{
	
	private List<OverlayItem> items;


	public QuestoMapOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		
		this.items = new ArrayList<OverlayItem>();
		OverlayItem item = new OverlayItem(new GeoPoint(19240000,-99120000),
										   "Test", 
										   "Testtext");
		this.items.add(item);
		
		this.populate();
	}

	@Override
	protected OverlayItem createItem(int index) {
		return this.items.get(index);
	}

	@Override
	public int size() {
		return this.items.size();
	}
	
	@Override
	protected boolean onTap(int index){
		  OverlayItem item = items.get(index);
		  return true;
	}

}
