package com.questo.android;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Notification;
import com.questo.android.model.Notification.Type;
import com.questo.android.model.Question;

public class HomeView extends Activity {

    private GridView mainGrid;

    private static final String[] menus = { "Quests", "Tournaments", "Profile", "Companions", "Trophy Room", "Settings" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        this.initViews();
    }

    private void initViews() {

        this.mainGrid = (GridView) findViewById(R.id.homeGrid);
        this.mainGrid.setAdapter(new ImageAdapter());
        
        ListView watchtower = (ListView) findViewById(R.id.watchtower);
        //
        QuestoListAdapter adapt = new QuestoListAdapter();
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST,
                "<b>Cato</b> has completed quest <b>Chillhouse of Terror</b>.", null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "<b>Nuno</b> completed a quest.",
                null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST,
                "<b>Lolcat</b> completed a quest.", null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST,
                "<b>Aragorn</b> completed a quest.", null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "<b>Cato</b> completed a quest.",
                null, null, new Date()));
        adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST,
                "<b>Gandalf</b> completed a quest.", null, null, new Date()));
        watchtower.setAdapter(adapt);

    }

    private void navigate(String to) {
        System.out.println("NAVIGATE!!! " + to);
        Intent navTo = null;
        if (to.equals(menus[0])) {
            navTo = new Intent(this, QuestMapView.class);
        }
        
        if (to.equals(menus[1])) {
        	navTo = new Intent(this, TournamentView.class);
        }
        
        if (to.equals(menus[2])) {
            navTo = new Intent(this, ProfileView.class);
        }
        
        if (to.equals(menus[3])) {
        }

        if (to.equals(menus[4])) {
        }
        
        if (to.equals(menus[5])) {
            navTo = new Intent(this, PlaceDetailsView.class);
            navTo.putExtra(Constants.TOPBAR_LABEL, "Stephansplatz");
            navTo.putExtra(Constants.NR_QUESTIONS, 40);
            navTo.putExtra(Constants.NR_ANSWERED_QUESTIONS, 4);
        }
        
        if (navTo != null) {
            startActivity(navTo);
        }
    }
    
	
	@Override
	public void onBackPressed() {
		// Similar result as pressing home button, but app lives on:
		moveTaskToBack(true);
	}

    private class MenuOnTouchListener implements OnClickListener {

        private String comp;

        public MenuOnTouchListener(String comp) {
            this.comp = comp;
        }

        @Override
        public void onClick(View v) {
            HomeView.this.navigate(comp);
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
            return mData.get(position).getText();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.home_listitem, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(Html.fromHtml(this.getItem(position)));
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
    }

    private class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return menus.length;
        }

        @Override
        public Object getItem(int pos) {
            return null;
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;

            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                v = li.inflate(R.layout.home_icon, null);
                TextView tv = (TextView) v.findViewById(R.id.home_icon_text);
                tv.setText(menus[position]);
                ImageView iv = (ImageView) v.findViewById(R.id.home_icon_image);
                iv.setImageResource(this.getImage(position));
                iv.setOnClickListener(new MenuOnTouchListener(menus[position]));
            } else {
                v = convertView;
            }

            return v;
        }

        private int getImage(int pos) {
            int id = -1;
            switch (pos) {
            case 0:
                id = R.drawable.imgstate_quests;
                break;
            case 1:
                id = R.drawable.imgstate_tournaments;
                break;
            case 2:
                id = R.drawable.imgstate_profiles;
                break;
            case 3:
                id = R.drawable.imgstate_companions;
                break;
            case 4:
                id = R.drawable.imgstate_trophies;
                break;
            case 5:
                id = R.drawable.imgstate_settings;
                break;
            default:
                id = R.drawable.img_arrow_target;
                break;
            }

            return id;
        }
    }
}