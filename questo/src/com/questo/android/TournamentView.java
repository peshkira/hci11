package com.questo.android;

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
import android.widget.TextView;

import com.j256.ormlite.dao.ForeignCollection;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentTask;
import com.questo.android.view.TopBar;

public class TournamentView extends Activity{
	
	private class TournamentListener implements OnItemClickListener, OnClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) 
		{
			
		}

		@Override
		public void onClick(View v) {
			if(v.getId()==TournamentView.this.requestsButtonId){
				Intent tournamentRequest = new Intent(TournamentView.this, TournamentRequestView.class);
				startActivity(tournamentRequest);
			}
		}
		
	}
	
	private class TournamentListAdapter extends ArrayAdapter<Tournament>{

		public TournamentListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v==null){
				LayoutInflater inflater = (LayoutInflater)TournamentView.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.tournament_request_list_item, null);
			}
			
			Tournament current = this.getItem(position);
			TextView nameText = (TextView)v.findViewById(R.id.TournamentListName);
			TextView infoText = (TextView)v.findViewById(R.id.TournamentListInfotext);
			
			if(current!=null){
				if(nameText!=null){
					nameText.setText(current.getName());
				}
				if(infoText!=null){
					ForeignCollection<TournamentTask> tournamentTasks = current.getTasks();
					if(tournamentTasks!=null){
						infoText.setText(tournamentTasks.size() + " quests");
					}
				}
				
			}
			
			
			return v;
		}		
	}
	
	
	private int requestsButtonId;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	private void initView(){
		this.setContentView(R.layout.tournament);
		
		TopBar topBar = (TopBar)findViewById(R.id.topbar);
		Button requestButton = topBar.addButtonLeftMost(this, "request");
		requestsButtonId = requestButton.getId();
		requestButton.setOnClickListener(new TournamentListener());
		
		ListView tournamentList = (ListView)this.findViewById(R.id.TournamentTournamentList);
		tournamentList.setOnItemClickListener(new TournamentListener());
		TournamentListAdapter adapter = new TournamentListAdapter(this, R.layout.user_profile_throphy_list_item);		
//		adapter.add(tournament1);
		tournamentList.setAdapter(adapter);
	}
}
