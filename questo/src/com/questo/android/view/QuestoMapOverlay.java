package com.questo.android.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.questo.android.R;

public class QuestoMapOverlay extends ItemizedOverlay<OverlayItem>{
	
	private List<OverlayItem> items;
	private Context context;
	private Activity activity;

	
	public QuestoMapOverlay(Drawable defaultMarker, Activity activity, Context context) {
		super(boundCenterBottom(defaultMarker));
		
		this.activity = activity;
		this.context = context;
		
		this.items = new ArrayList<OverlayItem>();

		OverlayItem item = new OverlayItem(new GeoPoint(47422005, 16422005), "foo", "bla");
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
		  
		  RelativeLayout mapViewLayout = (RelativeLayout)this.activity.findViewById(R.layout.quest_map);
//		  AbsoluteLayout mapViewLayout = (AbsoluteLayout)this.activity.findViewById(R.id.QuestMapLayout);
//		  LayoutInflater inflater = (LayoutInflater)this.activity.getSystemService(this.activity.LAYOUT_INFLATER_SERVICE);
		  LayoutInflater inflater = (LayoutInflater)this.activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  
		  
//		  RelativeLayout place = (RelativeLayout)inflater.inflate(R.layout.quest_map, null);
		  TextView place = new TextView(this.activity);
		  place.setText("Fooo");
		  
//		  mapViewLayout.addView(place);
		  
		  return true;
	}

}
