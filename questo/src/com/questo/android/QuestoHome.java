package com.questo.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.questo.android.view.TopBar;

public class QuestoHome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        TopBar topbar = (TopBar)findViewById(R.id.topbar);
        topbar.setTopBarLabel("LolCat!");
        topbar.addButtonLeftMost(getApplicationContext(), "+");

        this.initViews();

    }

    private void initViews() {
        int id = R.id.imgQuests;
        ImageView v = (ImageView) findViewById(R.id.imgQuests);
        v.setOnTouchListener(new MenuOnTouchListener());

        id = R.id.imgTournaments;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener());

        id = R.id.imgProfile;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener());

        id = R.id.imgCompanions;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener());

        id = R.id.imgTrophies;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener());

        id = R.id.imgSettings;
        v = (ImageView) findViewById(id);
        v.setOnTouchListener(new MenuOnTouchListener());
    }

    private void navigate(String to) {
        // TODO get the view Name
        // and start a new intent to this view
    }

    private class MenuOnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent evt) {
            ImageView view = (ImageView) v;

            switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                view.setImageResource(R.drawable.flashget);
                // QuestoHome.this.navigate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                view.setImageResource(R.drawable.games);
                // QuestoHome.this.navigate();
                break;
            }
            }
            return true;
        }
    }
}