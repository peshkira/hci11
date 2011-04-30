package com.questo.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TournamentRequestView extends Activity{
	
	
	private class TournamentListAdapter extends ArrayAdapter<String>{

		public TournamentListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			
			
		}
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	private void initView(){
		this.setContentView(R.layout.tournament_request);
		
		ListView tournamentRequestList = (ListView)findViewById(R.id.TournamentRequestList); 
		String[] listContent = new String[] {"foo", "bla"};
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.tournament_request_list_item, listContent);
		tournamentRequestList.setAdapter(adapter);
	}
}
