package com.questo.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.questo.android.common.Constants;
import com.questo.android.model.Place;
import com.questo.android.model.Tournament;
import com.questo.android.model.User;
import com.questo.android.view.TopBar;

public class TournamentDetailsView extends Activity {

	App app;
	Tournament tournament;

	private class PlacesAdapter extends ArrayAdapter<Place> {

		public PlacesAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) TournamentDetailsView.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.tournaments_list_item, null);
			}

			Place place = this.getItem(position);
			return v;
		}
	}

	private class ContestantsAdapter extends ArrayAdapter<User> {

		public ContestantsAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) TournamentDetailsView.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.tournaments_list_item, null);
			}

			User contestant = this.getItem(position);
			return v;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (App) getApplicationContext();
		initView(this.getIntent().getExtras());
	}

	private void initView(Bundle extras) {
		this.setContentView(R.layout.tournament_details);

		boolean userIsParticipant = false;

		if (extras != null) {
			String tournament_uuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
			if (tournament_uuid != null)
				tournament = app.getModelManager().getGenericObjectByUuid(tournament_uuid, Tournament.class);
		}
		if (tournament == null)
			tournament = app.getModelManager().getGenericObjectById(1, Tournament.class);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		topBar.setLabel(tournament.getName());

		ListView placesList = (ListView) this.findViewById(R.id.tournament_questlist);
		// TODO:
		// questsList.setEmptyView(findViewById(R.id.empty_tournamentlist_text));
		PlacesAdapter placesAdapter = new PlacesAdapter(this, R.layout.tournament_details_places_item);
		for (Place place : app.getModelManager().getPlacesForTournament(tournament))
			placesAdapter.add(place);
		placesList.setAdapter(placesAdapter);

		ListView contestantsList = (ListView) this.findViewById(R.id.tournament_contestantslist);
		// TODO:
		// contestantsList.setEmptyView(findViewById(R.id.empty_tournamentlist_text));
		ContestantsAdapter contestantsAdapter = new ContestantsAdapter(this,
				R.layout.tournament_details_contestants_item);
		for (User user : app.getModelManager().getContestantsForTournament(tournament)) {
			contestantsAdapter.add(user);
			if (user.equals(app.getLoggedinUser()))
				userIsParticipant = true;
		}
		contestantsList.setAdapter(contestantsAdapter);

		if (!userIsParticipant) {
			Button participateBtn = (Button) findViewById(R.id.participate_in_tournament);
			participateBtn.setVisibility(View.VISIBLE);
			participateBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					app.getModelManager().addTournamentMembership(tournament, app.getLoggedinUser());
					setParticipateButtonVisibility(false);
					setCancelButtonVisibility(true);
				}
			});
		} else {
			Button cancelBtn = (Button) findViewById(R.id.cancel_tournament);
			cancelBtn.setVisibility(View.VISIBLE);
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					app.getModelManager().cancelTournamentMembershipForLoggedinUser(
							tournament);
					setCancelButtonVisibility(false);
					setParticipateButtonVisibility(true);
				}
			});
		}
	}

	private void setParticipateButtonVisibility(boolean visible) {
		Button participateBtn = (Button) findViewById(R.id.participate_in_tournament);
		if (visible)
			participateBtn.setVisibility(View.VISIBLE);
		else
			participateBtn.setVisibility(View.GONE);
	}

	private void setCancelButtonVisibility(boolean visible) {
		Button cancelBtn = (Button) findViewById(R.id.cancel_tournament);
		if (visible)
			cancelBtn.setVisibility(View.VISIBLE);
		else
			cancelBtn.setVisibility(View.GONE);
	}

	public void onBackPressed() {
		startActivity(new Intent(this, HomeView.class));
	}
}
