package com.questo.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

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
				
			}
		}
		
	}
	
	private class TournamentListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return new String("foo");
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
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
		String[] listContent = new String[] {"The Neverending Story", "Quest for Glory"};
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.user_profile_throphy_list_item, listContent);		
		tournamentList.setAdapter(adapter);
	}
}
