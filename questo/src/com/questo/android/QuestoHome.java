package com.questo.android;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Notification;
import com.questo.android.model.Notification.Type;

public class QuestoHome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        // TopBar topbar = (TopBar)findViewById(R.id.topbar);
        // topbar.setTopBarLabel("LolCat!");
        // topbar.addButtonLeftMost(getApplicationContext(), "+");

        this.initViews();
    }

    private void initViews() {
        int id = R.id.imgQuests;
        ImageView v = (ImageView) findViewById(R.id.imgQuests);
        v.setOnClickListener(new MenuOnTouchListener("quests"));

        id = R.id.imgTournaments;
        v = (ImageView) findViewById(id);
        v.setOnClickListener(new MenuOnTouchListener("tournaments"));

        id = R.id.imgProfile;
        v = (ImageView) findViewById(id);
        v.setOnClickListener(new MenuOnTouchListener("profile"));

        id = R.id.imgCompanions;
        v = (ImageView) findViewById(id);
        v.setOnClickListener(new MenuOnTouchListener("companions"));

        id = R.id.imgTrophies;
        v = (ImageView) findViewById(id);
        v.setOnClickListener(new MenuOnTouchListener("trophies"));

        id = R.id.imgSettings;
        v = (ImageView) findViewById(id);
        v.setOnClickListener(new MenuOnTouchListener("settings"));

        ListView watchtower = (ListView) findViewById(R.id.watchtower);
        //
        QuestoListAdapter adapt = new QuestoListAdapter();
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "Cato completed a quest", null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "Nuno completed a quest", null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "Lolcat completed a quest", null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "Aragorn completed a quest", null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "Cato completed a quest", null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "Gandalf completed a quest", null, null, new Date()));
        watchtower.setAdapter(adapt);

    }

    private void navigate(String to) {
        System.out.println("NAVIGATE!!! " + to);
        Intent navTo = null;
        if (to.equals("profile")) {
            navTo = new Intent(this, UserProfile.class);
        }
        if (to.equals("quests")) {
        	navTo = new Intent(this, QuestMapView.class);
        }
        
        if(navTo != null){
        	startActivity(navTo);
        }
    }

    private class MenuOnTouchListener implements OnClickListener {

        private String comp;

        public MenuOnTouchListener(String comp) {
            this.comp = comp;
        }

        @Override
        public void onClick(View v) {
            QuestoHome.this.navigate(comp);
        }
    }

    private class QuestoListAdapter extends BaseAdapter {

        private ArrayList<Notification> mData = new ArrayList<Notification>();
        private LayoutInflater mInflater;

        public QuestoListAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final Notification item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position).toString();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("getView " + position + " " + convertView);
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listitem, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(this.getItem(position));
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
    }
}