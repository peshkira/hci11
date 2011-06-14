package com.questo.android.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.questo.android.AddPlace;
import com.questo.android.App;
import com.questo.android.ModelManager;
import com.questo.android.PlaceDetailsView;
import com.questo.android.R;
import com.questo.android.common.Constants;
import com.questo.android.helper.DisplayHelper;
import com.questo.android.map.TournamentMapCacheImpl;
import com.questo.android.model.Place;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentTask;

public class QuestoMapOverlay extends ItemizedOverlay<QuestoOverlayItem> {

	private List<QuestoOverlayItem> items;
	private Context context;
	private Place currentPlace;
	private ModelManager manager;
	private Map<String, Place> selectedPlaces;
	private List<Place> nearbyPlaces;
	private GeoPoint currentLocation;
	private QuestoMapView map;
	private GestureDetector gestures;
	private Activity activity;

	private RelativeLayout placeDetails;
	private Drawable normal;
	private Drawable selected;
	private Drawable done;
	private boolean selectable;
	private boolean showDetails;
	private MotionEvent lastEvent;

	public QuestoMapOverlay(Context context, QuestoMapView map,
			Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		init(context, map);
	}

	public void setSelectionEnabled(boolean selectable) {
		this.selectable = selectable;
		this.selectedPlaces.clear();
		this.refreshOverlayItems();
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

	public boolean isSelectionEnabled() {
		return selectable;
	}

	public Map<String, Place> getSelectedPlaces() {
		return this.selectedPlaces;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void setSelectedPlaces(String[] uuids) {
		for (String uuid : uuids) {
			Place place = manager.getGenericObjectByUuid(uuid, Place.class);
			selectedPlaces.put(uuid, place);
		}
		refreshPlaces();
	}

	public void setLocation(GeoPoint location) {
		this.currentLocation = location;
	}

	public synchronized void refreshPlaces() {
		GeoPoint leftUpper = map.getProjection().fromPixels(0, 0);
		GeoPoint rightBottom = map.getProjection().fromPixels(map.getWidth(), map.getHeight());
		map.getCache().setViewDimensions(leftUpper, rightBottom);
		nearbyPlaces.clear();
		nearbyPlaces.addAll(map.getCache().getPlaces());
		if (placeDetails != null) {
			placeDetails.setVisibility(View.INVISIBLE);
		}
		refreshOverlayItems();
	}

	private void init(Context context, QuestoMapView map) {
		this.context = context;
		this.map = map;
		this.gestures = new GestureDetector(new MapGestures());
		App application = (App) context.getApplicationContext();
		manager = application.getModelManager();
		items = new ArrayList<QuestoOverlayItem>();
		selectedPlaces = new HashMap<String, Place>();
		nearbyPlaces = new ArrayList<Place>();
		initIcons();
		populate();
		refreshPlaces();
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
		this.done = context.getResources().getDrawable(
				R.drawable.img_questo_q_stand_done);
		this.done.setBounds(0, 0, this.done.getIntrinsicWidth(),
				this.done.getIntrinsicHeight());
		this.done = this.boundCenterBottom(this.done);
	}
	
	private synchronized void refreshOverlayItems() {
		this.items.clear();
		for (Place place : nearbyPlaces) {
			GeoPoint placeLocation = new GeoPoint(
					(int) (place.getLatitude() * 1E6),
					(int) (place.getLongitude() * 1E6));
			QuestoOverlayItem overlayItem = new QuestoOverlayItem(
					placeLocation, place.getName(), "");
			overlayItem.setDrawables(this.normal, this.selected, this.done);
			overlayItem.setPlace(place);
			if (selectedPlaces.containsKey(place.getUuid())) {
				overlayItem.toggleSelected();
			}
			if (isTournamentMode()) {
				if (getTournamentPlacesDone().contains(place)) {
					overlayItem.setDone(true);
					overlayItem.setClickable(false);
				}
					
			}
			this.items.add(overlayItem);
		}
		setLastFocusedIndex(-1);
		populate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent evt, MapView view){
		if(gestures.onTouchEvent(evt))
			return true;
		else
			return false;
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
		} else {
			QuestoOverlayItem item = items.get(index);
			if (item.isClickable()) {
				currentPlace = nearbyPlaces.get(index);
				togglePlaceDetails(currentPlace, item);
			}
		}

		return true;
	}

	private void togglePlaceDetails(Place place, QuestoOverlayItem item) {
		if (showDetails) {
			if (placeDetails == null)
				createPlaceDetails();

			if (placeDetails.getVisibility() == View.INVISIBLE) {
				updatePlaceDetails(place);
				placeDetails.setVisibility(View.VISIBLE);
				map.removeView(placeDetails);
				map.addView(placeDetails, new MapView.LayoutParams(
						MapView.LayoutParams.WRAP_CONTENT,
						MapView.LayoutParams.WRAP_CONTENT, item.getPoint(), 0,
						DisplayHelper.dpToPixel(-57, context),
						MapView.LayoutParams.BOTTOM_CENTER));
			} else {
				this.placeDetails.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void createPlaceDetails() {
		if (placeDetails == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			placeDetails = (RelativeLayout) inflater.inflate(
					R.layout.quest_map_item, null);
			placeDetails.setVisibility(View.INVISIBLE);
			this.placeDetails.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (currentPlace != null) {
						Intent placeDetails = new Intent(context,
								PlaceDetailsView.class);
						Tournament t;
						if((t = getTournament()) != null) {
							TournamentTask currentTTask = manager.getTournamentTaskForPlace(t, currentPlace);
							placeDetails.putExtra(Constants.CURRENT_TOURNAMENT_TASK_UUID,
									currentTTask.getUuid());
						}
						placeDetails.putExtra(Constants.TRANSITION_OBJECT_UUID,
								currentPlace.getUuid());
						context.startActivity(placeDetails);
					}
				}
			});
		}
	}
	
	private Tournament getTournament() {
		if (isTournamentMode()) {
			TournamentMapCacheImpl tournamentCache = (TournamentMapCacheImpl) map.getCache();
			return tournamentCache.getTournament();
		}
		return null;
	}
	
	private List<Place> getTournamentPlacesDone() {
		if (isTournamentMode()) {
			TournamentMapCacheImpl tournamentCache = (TournamentMapCacheImpl) map.getCache();
			return tournamentCache.getPlacesDoneInTournament();
		}
		return new ArrayList<Place>();
	}
	
	private boolean isTournamentMode() {
		return map.getCache() instanceof TournamentMapCacheImpl;
	}

	private void updatePlaceDetails(Place place) {
		int questionCount = 0;
		if (place.getQuestions() != null) {
			questionCount = place.getQuestions().size();
		}

		TextView placeNameText = (TextView) placeDetails
				.findViewById(R.id.QuestMapPlaceDetailsName);
		TextView questionCountText = (TextView) placeDetails
				.findViewById(R.id.QuestMapPlaceDetailsQuestionCount);
		placeNameText.setText(place.getName());
		questionCountText.setText("Questions: "
				+ Integer.toString(questionCount));
	}
	
	public MotionEvent getLastEvent(){
		return lastEvent;
	}

	@Override
	public synchronized int size() {
		return this.items.size();
	}

	public synchronized List<QuestoOverlayItem> getItems() {
		return this.items;
	}

	@Override
	protected synchronized QuestoOverlayItem createItem(int index) {
		return this.items.get(index);
	}
	
	
	private class MapGestures extends SimpleOnGestureListener{
		@Override
		public void onLongPress(MotionEvent evt){
			setShowDetails(false);
			lastEvent = evt;
			activity.openContextMenu(map);
		}
	}
}
