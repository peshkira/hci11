package com.questo.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.maps.MapActivity;
import com.questo.android.common.Constants;
import com.questo.android.model.Place;
import com.questo.android.view.QuestoMapView;
import com.questo.android.view.TopBar;

public class QuestMapView extends MapActivity {

	public final static int ADD_PLACE_REQUEST_CODE = 1;
	public final static int ADD_QUESTION_REQUEST_CODE = 2;

	private QuestoMapView questMap;
	private Place currentPlace;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void initView() {
		setContentView(R.layout.quest_map);
		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		Button addQuestionBtn = topBar.addImageButtonLeftMost(
				this.getApplicationContext(), R.drawable.img_plus);
		Button showListBtn = topBar.addImageToggleButtonLeftMost(
				this.getApplicationContext(), R.drawable.img_list, false);
		Button doneBtn = (Button) findViewById(R.id.PlaceSelectDone);
		showListBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showListView = new Intent(QuestMapView.this,
						QuestListView.class);
				startActivity(showListView);
			}
		});
		addQuestionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QuestMapView.this.openContextMenu(v);
			}
		});
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> places = QuestMapView.this.questMap
						.getSelectedPlacesUuid();
				if(places.size()!=1){
					Toast.makeText(QuestMapView.this, R.string.error_question_place_count, Toast.LENGTH_LONG).show();
				}
				else{
					String[] array = new String[0];	
					Intent addQuestionIntent = new Intent(QuestMapView.this,
							AddQuestion.class);
					addQuestionIntent.putExtra(Constants.TRANSITION_OBJECT_UUID,
							places.toArray(array));
					startActivityForResult(addQuestionIntent,
							ADD_QUESTION_REQUEST_CODE);
					Button doneBtn = (Button) QuestMapView.this
							.findViewById(R.id.PlaceSelectDone);
					doneBtn.setVisibility(View.GONE);
					QuestMapView.this.questMap.setSelectionEnabled(false);
					QuestMapView.this.questMap.setShowDetails(true);
				}
			}
		});
		registerForContextMenu(addQuestionBtn);
		questMap = (QuestoMapView) findViewById(R.id.QuestMap);
		questMap.setShowDetails(true);
		LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.ZoomControls);
		View zoomView = questMap.getZoomButtonsController().getZoomControls();
		questMap.getZoomButtonsController().getContainer().removeView(zoomView);
		zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, HomeView.class));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		MenuItem addPlace = menu.add("Add Place");
		MenuItem addQuestion = menu.add("Add Question");
		addPlace.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent addPlaceIntent = new Intent(QuestMapView.this,
						AddPlace.class);
				startActivityForResult(addPlaceIntent, ADD_PLACE_REQUEST_CODE);
				return true;
			}
		});
		addQuestion.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				QuestMapView.this.questMap.setSelectionEnabled(true);
				QuestMapView.this.questMap.setShowDetails(false);
				Button doneBtn = (Button) QuestMapView.this
						.findViewById(R.id.PlaceSelectDone);
				doneBtn.setVisibility(View.VISIBLE);
				Toast.makeText(QuestMapView.this,
						R.string.add_question_select_place, Toast.LENGTH_LONG)
						.show();
				return true;
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (questMap != null) {	
			questMap.refresh();
		}
	}
}
