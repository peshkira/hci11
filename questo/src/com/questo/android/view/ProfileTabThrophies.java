package com.questo.android.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.questo.android.R;
import com.questo.android.Throphy;

public class ProfileTabThrophies extends Activity{
	
	private class TrophyListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) 
		{
			Intent showTrophy = new Intent(ProfileTabThrophies.this, Throphy.class);
			startActivity(showTrophy);
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
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.throphy_list_item, listContent);
		throphyList.setAdapter(adapter);
		throphyList.setOnItemClickListener(new TrophyListener());
	}
}
