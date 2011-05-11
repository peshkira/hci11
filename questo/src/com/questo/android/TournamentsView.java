package com.questo.android;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentTask;
import com.questo.android.view.TopBar;

public class TournamentsView extends Activity {
	
	App app;

	private class TournamentListAdapter extends ArrayAdapter<Tournament> {

		public TournamentListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) TournamentsView.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.tournaments_list_item, null);
			}

			Tournament current = this.getItem(position);
			TextView nameText = (TextView) v.findViewById(R.id.tournament_name);
			TextView locationText = (TextView) v.findViewById(R.id.tournament_location);
			ProgressBar questcount = (ProgressBar) v.findViewById(R.id.tournament_questcount);
			TextView questcountBarText = (TextView) v.findViewById(R.id.tournament_questcount_bar);

			List<TournamentTask>[] tasks = app.getModelManager().getTournamentTasksDoneAndRemaining(app.getLoggedinUser(), current);
			int doneTasksCount = tasks[0].size();
			int allTaskCount = doneTasksCount + tasks[1].size();
			
			nameText.setText(current.getName());
			String currentLocation;
			if ((currentLocation = current.getLocation()) != null) 
				locationText.setText(currentLocation);	
			questcount.setProgressDrawable(getResources().getDrawable(R.drawable.bg_tasksdone_progressbar));
			questcount.setMax(allTaskCount);
			questcount.setProgress(doneTasksCount);
			questcountBarText.setText(doneTasksCount + " of " + allTaskCount + " done");

			return v;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (App)getApplicationContext();
		initView();
	}

	private void initView() {
		this.setContentView(R.layout.tournaments);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		Button requestButton = topBar.addToggleButtonLeftMost(this, "Requests", false);
		requestButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent tournamentRequest = new Intent(TournamentsView.this, TournamentRequestView.class);
				startActivity(tournamentRequest);
			}
		});

		ListView tournamentList = (ListView) this.findViewById(R.id.tournamentlist);
		tournamentList.setEmptyView(findViewById(R.id.empty_tournamentlist_text));
		tournamentList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Tournament tournament = (Tournament)parent.getItemAtPosition(position);
				Intent intent = new Intent(TournamentsView.this, TournamentDetailsView.class);
                intent.putExtra(Constants.TRANSITION_OBJECT_UUID, tournament.getUuid());
                startActivity(intent);
			}
		});
		TournamentListAdapter adapter = new TournamentListAdapter(this, R.layout.user_profile_throphy_list_item);
		for(Tournament tournament : app.getModelManager().getTournamentsForUser(app.getLoggedinUser(), true))
			adapter.add(tournament);
		tournamentList.setAdapter(adapter);
	}
	
	public void onBackPressed() {
		startActivity(new Intent(this, HomeView.class));
	}
}
