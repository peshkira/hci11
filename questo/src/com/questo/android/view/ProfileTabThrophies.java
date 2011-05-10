package com.questo.android.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.R;
import com.questo.android.model.Trophy;

public class ProfileTabThrophies extends Activity{
	
	private class TrophyListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) 
		{
			Intent showTrophy = new Intent(ProfileTabThrophies.this, Trophy.class);
			startActivity(showTrophy);
		}
		
	}
	
	private class ThrophyListAdapter extends ArrayAdapter<Trophy>{

		public ThrophyListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v==null){
				LayoutInflater inflater = (LayoutInflater)ProfileTabThrophies.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.tournament_requests_list_item, null);
			}
			
			Trophy current = this.getItem(position);
			TextView throphyText = (TextView)v.findViewById(R.id.ThrophyListText);
			ImageView throphyIcon = (ImageView)v.findViewById(R.id.ThrophyListIcon);
			
			if(current!=null){
				if(throphyText!=null){
					throphyText.setText(current.getName());
				}
				
				if(throphyIcon!=null){
					//setting the default throphy thumb image for all throphies without an icon
					throphyIcon.setImageDrawable(getResources().getDrawable(R.drawable.img_throphy_img_thumb));
				}
				
			}
			
			
			return v;
		}	
		
		
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.initView();
	}
	
	private void initView(){
		setContentView(R.layout.user_profile_throphies);
		
		ListView throphyList = (ListView)findViewById(R.id.ProfileThrophyList); 
		String[] listContent = new String[] {"foo", "bla"};
//		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.user_profile_throphy_list_item, listContent);
//		throphyList.setAdapter(adapter);
		ThrophyListAdapter adapter = new ThrophyListAdapter(this, R.layout.user_profile_throphy_list_item);
		throphyList.setAdapter(adapter);
		throphyList.setOnItemClickListener(new TrophyListener());
	}
}
