package com.questo.android.view;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.questo.android.R;

public class ProfileTabThrophies extends ListActivity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.user_profile_throphies);
		
		String[] listContent = new String[] {"foo", "bla"};
		
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.throphy_list_item, listContent);
		setListAdapter(adapter);
		
	}
}
