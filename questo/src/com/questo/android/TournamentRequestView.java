package com.questo.android;

import com.questo.android.model.Tournament;
import com.questo.android.model.Trophy;
import com.questo.android.view.ProfileTabThrophies;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TournamentRequestView extends Activity{
	
	
	private class TournamentListAdapter extends ArrayAdapter<Tournament>{

		public TournamentListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v==null){
				LayoutInflater inflater = (LayoutInflater)TournamentRequestView.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.tournament_request_list_item, null);
			}
			
			Tournament current = this.getItem(position);
//			TextView throphyText = (TextView)v.findViewById(R.id.ThrophyListText);
//			ImageView throphyIcon = (ImageView)v.findViewById(R.id.ThrophyListIcon);
			
			if(current!=null){
//				if(throphyText!=null){
//					throphyText.setText(current.getName());
//				}
//				
//				if(throphyIcon!=null){
//					//setting the default throphy thumb image for all throphies without an icon
//					throphyIcon.setImageDrawable(getResources().getDrawable(R.drawable.throphy_img_thumb));
//				}	
			}
			return v;
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
//		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.tournament_request_list_item, listContent);
		TournamentListAdapter adapter = new TournamentListAdapter(this, R.layout.tournament_request_list_item);
		tournamentRequestList.setAdapter(adapter);
	}
}
