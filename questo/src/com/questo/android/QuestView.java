package com.questo.android;

import android.app.Activity;
import android.os.Bundle;

public class QuestView extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.quests);
	}
}
