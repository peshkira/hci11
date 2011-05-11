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

public class NewTournamentView extends Activity {

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
				LayoutInflater inflater = (LayoutInflater) NewTournamentView.this
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
				LayoutInflater inflater = (LayoutInflater) NewTournamentView.this
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
		this.setContentView(R.layout.tournament_new);
		
		ListView placesList = (ListView)findViewById(R.id.tournament_questlist);
		PlacesAdapter placesAdapter = new PlacesAdapter(this, R.layout.tournament_details_places_item);
		placesList.setAdapter(placesAdapter);

		ListView contestantsList = (ListView)findViewById(R.id.tournament_contestantslist);
		ContestantsAdapter contestantsAdapter = new ContestantsAdapter(this, R.layout.tournament_details_contestants_item);
		contestantsList.setAdapter(contestantsAdapter);	
	}

	public void onBackPressed() {
		startActivity(new Intent(this, TournamentsView.class));
	}
}
