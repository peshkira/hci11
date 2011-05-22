package com.questo.android;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
		registerForContextMenu(addQuestionBtn);
		questMap = (QuestoMapView) findViewById(R.id.QuestMap);
		questMap.setShowDetails(true);
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
				if (QuestMapView.this.currentPlace != null) {
					String uuid = QuestMapView.this.currentPlace.getUuid();
					Intent addQuestionIntent = new Intent(QuestMapView.this,
							AddQuestion.class);
					addQuestionIntent.putExtra(
							Constants.EXTRA_ADD_QUESTION_PLACE_UUID, uuid);
					startActivityForResult(addQuestionIntent,
							ADD_QUESTION_REQUEST_CODE);
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(questMap!=null){
			questMap.refresh();
		}
	}
}
