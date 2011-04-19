package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.questo.android.R;

public class QuestoHome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
//        TopBar topbar = (TopBar)findViewById(R.id.topbar);
//        topbar.setTopBarLabel("LolCat!");
//        topbar.addButtonLeftMost(getApplicationContext(), "+");

        this.initViews();
    }

    private void initViews() {
        int id = R.id.imgQuests;
        ImageView v = (ImageView) findViewById(R.id.imgQuests);
        v.setOnTouchListener(new MenuOnTouchListener("quests"));

        id = R.id.imgTournaments;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener("tournaments"));

        id = R.id.imgProfile;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener("profile"));

        id = R.id.imgCompanions;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener("companions"));

        id = R.id.imgTrophies;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener("trophies"));

        id = R.id.imgSettings;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener("settings"));
        
        ListView watchtower = (ListView) findViewById(R.id.watchtower);
        //
        String lv_arr[]={"Android","iPhone","BlackBerry","AndroidPeople"};
        watchtower.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , lv_arr));
        
    }

    private void navigate(String to) {
        System.out.println("NAVIGATE!!! " + to);
        
        Intent navTo;
        
        if(to.equals("profile")){
        	navTo = new Intent(this, UserProfile.class);
        	startActivity(navTo);
        }
    }

    private class MenuOnTouchListener implements OnTouchListener {
        
        private String comp;

        public MenuOnTouchListener(String comp) {
            this.comp = comp;
        }

        @Override
        public boolean onTouch(View v, MotionEvent evt) {
            ImageView view = (ImageView) v;

            switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                view.setImageResource(R.drawable.flashget);
                break;
            }
            case MotionEvent.ACTION_UP: {
                view.setImageResource(R.drawable.games);
                QuestoHome.this.navigate(comp);
                break;
            }
            }
            return true;
        }
    }
}