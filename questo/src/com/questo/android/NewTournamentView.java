package com.questo.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.questo.android.helper.QuestoFieldFocusListener;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Place;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentMembership;
import com.questo.android.model.TournamentRequest;
import com.questo.android.model.TournamentTask;
import com.questo.android.model.User;
import com.questo.android.view.FlexibleImageView;

public class NewTournamentView extends Activity {

	
	App app;
	Tournament tournament;
	PlacesAdapter placesAdapter;
	ContestantsAdapter participantsAdapter;

	public static final int COMPANION_REQUEST_CODE = 1;
	public final static int TOURNAMENT_MAP_REQUEST_CODE = 0;

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
	 * 
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
		description_participants.setText(Html.fromHtml("Currently, the following <b>" + contestantsAdapter.getCount()
				+ " opponents participate</p>:"));
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

		QuestoFieldFocusListener focusListener = new QuestoFieldFocusListener((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
		ScrollView background = (ScrollView) findViewById(R.id.new_tournament_scroll);
        background.setOnTouchListener(focusListener);
		
		ListView placesList = (ListView) findViewById(R.id.tournament_questlist);
		placesAdapter = new PlacesAdapter(this, R.layout.tournament_details_places_item);
		placesList.setAdapter(placesAdapter);
		placesList.setOnTouchListener(focusListener);

		ListView participantsList = (ListView) findViewById(R.id.tournament_contestantslist);
		participantsAdapter = new ContestantsAdapter(this, R.layout.tournament_details_contestants_item);
		participantsList.setAdapter(participantsAdapter);
		participantsList.setOnTouchListener(focusListener);

		Button addPlacesBtn = (Button) findViewById(R.id.add_place);
		addPlacesBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewTournamentView.this, TournamentMapView.class);
				String[] preselectedUUIDs = new String[placesAdapter.getCount()];
				for (int i = 0; i < placesAdapter.getCount(); i++)
					preselectedUUIDs[i] = placesAdapter.getItem(i).getUuid();
				intent.putExtra(TournamentMapView.EXTRA_PLACE_UUID_ARRAY, preselectedUUIDs);
				startActivityForResult(intent, NewTournamentView.TOURNAMENT_MAP_REQUEST_CODE);
			}
		});

		Button addParticipantsBtn = (Button) findViewById(R.id.add_participant);
		addParticipantsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewTournamentView.this, CompanionChooserView.class);
				String[] preselectedUUIDs = new String[participantsAdapter.getCount()];
				for (int i = 0; i < participantsAdapter.getCount(); i++)
					preselectedUUIDs[i] = participantsAdapter.getItem(i).getUuid();
				intent.putExtra(CompanionChooserView.EXTRA_COMPANION_UUID_ARRAY, preselectedUUIDs);
				startActivityForResult(intent, COMPANION_REQUEST_CODE);
			}
		});

		Button createBtn = (Button) findViewById(R.id.create_tournament);
		createBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView name = (TextView) findViewById(R.id.name);
				if (name.getText().length() == 0) {
					Toast noNameToast = Toast.makeText(NewTournamentView.this, "Please provide a name!", Toast.LENGTH_LONG);
					noNameToast.show();
					return;
				}
				ModelManager mngr = app.getModelManager();
				
				TextView location = (TextView) findViewById(R.id.location);
				Tournament newTournament = new Tournament(UUIDgen.getUUID(), new Date(), name.getText().toString(),
						location.getText().toString(), Tournament.Type.COOP);
				mngr.create(newTournament, Tournament.class);
				
				for(Place place : getPlaces()) {
					TournamentTask task = new TournamentTask(UUIDgen.getUUID(), newTournament, place.getUuid());
					mngr.create(task, TournamentTask.class);
				}
				
				String requestorUUID = app.getLoggedinUser().getUuid();
				TournamentMembership creatorMembership = new TournamentMembership(UUIDgen.getUUID(), requestorUUID,
						newTournament.getUuid(), new Date());
				mngr.create(creatorMembership, TournamentMembership.class);
				
				for (User participant : getParticipants()) {
					TournamentRequest tR = new TournamentRequest(UUIDgen.getUUID(), participant.getUuid(),
							requestorUUID, newTournament.getUuid(), new Date());
					mngr.create(tR, TournamentRequest.class);
				}
				
				startActivity(new Intent(NewTournamentView.this, TournamentsView.class));
			}
		});
	}

	private List<User> getParticipants() {
		ArrayList<User> participants = new ArrayList<User>();
		for (int i = 0; i < participantsAdapter.getCount(); i++)
			participants.add(participantsAdapter.getItem(i));
		return participants;
	}
	
	private List<Place> getPlaces() {
		ArrayList<Place> places = new ArrayList<Place>();
		for (int i = 0; i < placesAdapter.getCount(); i++)
			places.add(placesAdapter.getItem(i));
		return places;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COMPANION_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String[] participantUUIDs = data.getExtras().getStringArray(
						CompanionChooserView.EXTRA_COMPANION_UUID_ARRAY);
				participantsAdapter.clear();
				for (String participantUUID : participantUUIDs) {
					User participant = app.getModelManager().getGenericObjectByUuid(participantUUID, User.class);
					participantsAdapter.add(participant);
				}
			}
		}
		else if (requestCode == TOURNAMENT_MAP_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String[] placesUUIDs = data.getExtras().getStringArray(
						TournamentMapView.EXTRA_PLACE_UUID_ARRAY);
				placesAdapter.clear();
				for (String placeUUID: placesUUIDs) {
					Place place = app.getModelManager().getGenericObjectByUuid(placeUUID, Place.class);
					placesAdapter.add(place);
				}
			}
		}
	}

	public void onBackPressed() {
		startActivity(new Intent(this, TournamentsView.class));
	}
}
