package com.questo.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.model.Place;
import com.questo.android.model.Tournament;
import com.questo.android.model.User;
import com.questo.android.view.FlexibleImageView;
import com.questo.android.view.TopBar;

public class TournamentDetailsView extends Activity {

	
	public static final String EXTRA_STARTED_BY = "EXTRA_STARTED_BY";
	public enum StartedBy {STARTED_BY_TOURNAMENT_REQUESTS, STARTED_BY_TOURNAMENTS_OVERVIEW}

	App app;
	Tournament tournament;
	StartedBy startedBy;

	private class ParticipateOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			app.getModelManager().addTournamentMembership(tournament, app.getLoggedinUser());
			loadContestants();
			setParticipateButtonVisibility(false);
			setCancelButtonVisibility(true);
		}
	}

	private class CancelTournamentOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			app.getModelManager().cancelTournamentMembershipForLoggedinUser(tournament);
			loadContestants();
			setCancelButtonVisibility(false);
			setParticipateButtonVisibility(true);
		}
	}

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
				v = inflater.inflate(R.layout.tournament_details_places_item, null);
			}

			Place place = this.getItem(position);
			TextView name = (TextView) v.findViewById(R.id.name);
			name.setText(place.getName());

			FlexibleImageView image = (FlexibleImageView) v.findViewById(R.id.place_image);
			image.setBackgroundResource(R.drawable.img_questo_sign);

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
				v = inflater.inflate(R.layout.tournament_details_contestants_item, null);
			}

			User contestant = this.getItem(position);
			TextView name = (TextView) v.findViewById(R.id.name);
			if (contestant.getUuid().equals(app.getLoggedinUser().getUuid())) {
				name.setText("You");
				v.setClickable(false);
			}

			else
				name.setText(contestant.getName());

			FlexibleImageView image = (FlexibleImageView) v.findViewById(R.id.contestant_image);
			image.setBackgroundResource(R.drawable.img_companion);
			return v;
		}
	}

	private void loadPlaces() {
		ListView placesList = (ListView) this.findViewById(R.id.tournament_questlist);
		// TODO:
		// questsList.setEmptyView(findViewById(R.id.empty_tournamentlist_text));
		PlacesAdapter placesAdapter = new PlacesAdapter(this, R.layout.tournament_details_places_item);
		for (Place place : app.getModelManager().getPlacesForTournament(tournament))
			placesAdapter.add(place);
		placesList.setAdapter(placesAdapter);
		
		TextView description_places = (TextView) this.findViewById(R.id.description_places);
		description_places.setText(Html.fromHtml("Participants of <b>" + tournament.getName()
				+ "</b> have to complete quests at these <b>" + placesAdapter.getCount() + " places</p>:"));

	}
	
	/**
	 * Returns true if the current user is a participant.
	 * @return
	 */
	private boolean loadContestants() {
		boolean userIsParticipant = false;
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

		TextView description_participants = (TextView) this.findViewById(R.id.description_participants);
		description_participants.setText(Html.fromHtml("Currently, the following <b>" + contestantsAdapter.getCount() + " opponents participate</p>:"));
		return userIsParticipant;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (App) getApplicationContext();
		initView(this.getIntent().getExtras());
	}

	private void initView(Bundle extras) {
		this.setContentView(R.layout.tournament_details);
		
		if (extras != null) {
			String tournament_uuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
			int startedby_ordinal = extras.getInt(EXTRA_STARTED_BY);
			startedBy = StartedBy.values()[startedby_ordinal];
			if (tournament_uuid != null)
				tournament = app.getModelManager().getGenericObjectByUuid(tournament_uuid, Tournament.class);
		}
		if (tournament == null)
			tournament = app.getModelManager().getGenericObjectById(1, Tournament.class);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		topBar.setLabel(tournament.getName());

		boolean userIsParticipant = loadContestants();
		loadPlaces();

		if (!userIsParticipant) {
			Button participateBtn = (Button) findViewById(R.id.participate_in_tournament);
			setParticipateButtonVisibility(true);
		} else {
			Button cancelBtn = (Button) findViewById(R.id.cancel_tournament);
			setCancelButtonVisibility(true);
		}
	}

	private void setParticipateButtonVisibility(boolean visible) {
		Button participateBtn = (Button) findViewById(R.id.participate_in_tournament);
		if (visible) {
			participateBtn.setVisibility(View.VISIBLE);
			participateBtn.setOnClickListener(new ParticipateOnClickListener());
		} else
			participateBtn.setVisibility(View.GONE);
	}

	private void setCancelButtonVisibility(boolean visible) {
		Button cancelBtn = (Button) findViewById(R.id.cancel_tournament);
		if (visible) {
			cancelBtn.setVisibility(View.VISIBLE);
			cancelBtn.setOnClickListener(new CancelTournamentOnClickListener());
		}

		else
			cancelBtn.setVisibility(View.GONE);
	}

	public void onBackPressed() {
		if (startedBy.equals(StartedBy.STARTED_BY_TOURNAMENTS_OVERVIEW))
			startActivity(new Intent(this, TournamentsView.class));
		else if (startedBy.equals(StartedBy.STARTED_BY_TOURNAMENT_REQUESTS))
			startActivity(new Intent(this, TournamentRequestView.class));
	}
}
