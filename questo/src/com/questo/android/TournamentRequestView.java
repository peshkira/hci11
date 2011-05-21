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
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentRequest;
import com.questo.android.model.User;
import com.questo.android.view.TopBar;

public class TournamentRequestView extends Activity {
	App app;
	TournamentRequestListAdapter adapter;

	private class TournamentRequestListAdapter extends ArrayAdapter<TournamentRequest> {

		public TournamentRequestListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) TournamentRequestView.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.requests_list_item, null);
			}
			final TournamentRequest current = this.getItem(position);
			TextView reqDescription = (TextView) v.findViewById(R.id.request_description);

			User requestor = app.getModelManager().getGenericObjectByUuid(current.getRequestorUUID(), User.class);
			final Tournament tournament = app.getModelManager().getGenericObjectByUuid(current.getTournamentUUID(),
					Tournament.class);
			String requestorName = requestor.getName() == null ? "Someone" : requestor.getName();
			String tournamentName = tournament.getName() == null ? "Unknown" : tournament.getName();

			reqDescription.setText(Html.fromHtml("<b>" + requestorName
					+ "</b> has challenged you to the tournament <b>" + tournamentName + "</b>."));

			Button acceptBtn = (Button) v.findViewById(R.id.accept);
			acceptBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					app.getModelManager().acceptTournamentRequest(current);
					loadRequests();
				}
			});

			Button rejectBtn = (Button) v.findViewById(R.id.reject);
			rejectBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					app.getModelManager().delete(current, TournamentRequest.class);
					loadRequests();
				}
			});
			
			Button detailsBtn = (Button) v.findViewById(R.id.details);
			detailsBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(TournamentRequestView.this, TournamentDetailsView.class);
	                intent.putExtra(Constants.TRANSITION_OBJECT_UUID, tournament.getUuid());
	                intent.putExtra(TournamentDetailsView.EXTRA_STARTED_BY, TournamentDetailsView.StartedBy.STARTED_BY_TOURNAMENT_REQUESTS.ordinal());
	                startActivity(intent);
				}
			});

			return v;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (App) getApplicationContext();
		initView();
	}

	private void initView() {
		this.setContentView(R.layout.tournament_requests);
		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		Button requestButton = topBar.addImageToggleButtonLeftMost(this, R.drawable.img_request, true);
		requestButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(TournamentRequestView.this, TournamentsView.class));
			}
		});
		ListView tournamentRequestList = (ListView) findViewById(R.id.requestlist);
		tournamentRequestList.setEmptyView(findViewById(R.id.empty_tournamentrequestlist_text));
		String[] listContent = new String[] { "foo", "bla" };
		adapter = new TournamentRequestListAdapter(this, R.layout.requests_list_item);
		tournamentRequestList.setAdapter(adapter);
		loadRequests();
	}

	private void loadRequests() {
		adapter.clear();
		for (TournamentRequest req : app.getModelManager().getTournamentRequestsForUser(app.getLoggedinUser()))
			adapter.add(req);
	}

	public void onBackPressed() {
		startActivity(new Intent(this, TournamentsView.class));
	}
}
