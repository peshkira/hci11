package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.questo.android.view.TopBar;

public class QuestListView extends Activity{
	
	
	private class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==showMapBtnId){
				Intent showMapView = new Intent(QuestListView.this, QuestMapView.class);
				startActivity(showMapView);
			}
			if(v.getId()==addQuestionBtnId){
				
			}
		}
		
	}
	
	
	private int addQuestionBtnId;
	private int showMapBtnId;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.quest_list);
		
		TopBar topBar = (TopBar)findViewById(R.id.topbar);
		Button addQuestionBtn = topBar.addButtonLeftMost(this, "+");
		Button showMapBtn = topBar.addButtonLeftMost(this, "m");
		
		addQuestionBtnId = addQuestionBtn.getId();
		showMapBtnId = showMapBtn.getId();
		addQuestionBtn.setOnClickListener(new ButtonListener());
		showMapBtn.setOnClickListener(new ButtonListener());
		
		ListView questList = (ListView)findViewById(R.id.QuestList);
		String[] listContent = new String[] {"foo", "bla"};
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.throphy_list_item, listContent);
		questList.setAdapter(adapter);
	}
}
