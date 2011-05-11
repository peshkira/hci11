package com.questo.android.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.App;
import com.questo.android.ModelManager;
import com.questo.android.R;
import com.questo.android.TrophyView;
import com.questo.android.common.Constants;
import com.questo.android.model.Trophy;

public class ProfileTabThrophies extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_throphies);

        this.initView();
    }

    private void initView() {
        App app = (App) getApplicationContext();
        ModelManager mngr = app.getModelManager();

        List<Trophy> trophies = mngr.getTrophyForUser(app.getLoggedinUser());
        ThrophyListAdapter adapter = new ThrophyListAdapter(trophies);

        ListView trophyList = (ListView) findViewById(R.id.ProfileThrophyList);
        trophyList.setAdapter(adapter);
        trophyList.setOnItemClickListener(new TrophyListener(trophies));

    }

    private class TrophyListener implements OnItemClickListener {

        private List<Trophy> trophies;

        public TrophyListener(List<Trophy> trophies) {
            this.trophies = trophies;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent showTrophy = new Intent(ProfileTabThrophies.this, TrophyView.class).putExtra(
                    Constants.TRANSITION_OBJECT_UUID, this.trophies.get(position).getUuid());
            startActivity(showTrophy);
        }

    }

    private class ThrophyListAdapter extends BaseAdapter {

        private List<Trophy> trophies;

        public ThrophyListAdapter(List<Trophy> trophies) {
            this.trophies = trophies;
        }

        @Override
        public int getCount() {
            return trophies.size();
        }

        @Override
        public Object getItem(int pos) {
            return trophies.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ProfileTabThrophies.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.trophy_listitem, null);
            }

            Trophy current = this.trophies.get(position);

            TextView trophyText = (TextView) v.findViewById(R.id.trophy_name);
            trophyText.setText(current.getName());

            ImageView trophyIcon = (ImageView) v.findViewById(R.id.trophy_img);
            trophyIcon.setImageDrawable(getResources().getDrawable(R.drawable.img_trophy_thumb));

            return v;
        }

    }
}
