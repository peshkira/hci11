package com.questo.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.model.Place;
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
	
	private class QuestListAdapter extends ArrayAdapter<Place>{

		public QuestListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v==null){
				LayoutInflater inflater = (LayoutInflater)QuestListView.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.quest_list_item, null);
			}
			
			Place current = this.getItem(position);
			TextView nameText = (TextView)v.findViewById(R.id.QuestListNameText);
			TextView distanceText = (TextView)v.findViewById(R.id.QuestListDistance);
			TextView questCountText = (TextView)v.findViewById(R.id.QuestListQuestCount);
			
			if(current!=null){
				if(nameText!=null){
						nameText.setText(current.getName());
				}
				if(distanceText!=null){
					distanceText.setText("unknown");
				}
				if((current.getQuestions()!=null) && (questCountText!=null)){
					questCountText.setText(current.getQuestions().size());
				}				
			}
			
			
			return v;
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
//		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.quest_list_item, listContent);
//		questList.setAdapter(adapter);
		QuestListAdapter adapter = new QuestListAdapter(this, R.layout.quest_list_item);
		questList.setAdapter(adapter);
	}
}
