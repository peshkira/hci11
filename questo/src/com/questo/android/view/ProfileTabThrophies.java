package com.questo.android.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
	
	private class ThrophyListAdapter extends BaseAdapter{

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
	

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.initView();
	}
	
	private void initView(){
		setContentView(R.layout.user_profile_throphies);
		
		ListView throphyList = (ListView)findViewById(R.id.ProfileThrophyList); 
		String[] listContent = new String[] {"foo", "bla"};
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.user_profile_throphy_list_item, listContent);
		throphyList.setAdapter(adapter);
//		ThrophyListAdapter adapter = new ThrophyListAdapter();
//		throphyList.setAdapter(adapter);
		throphyList.setOnItemClickListener(new TrophyListener());
	}
}
