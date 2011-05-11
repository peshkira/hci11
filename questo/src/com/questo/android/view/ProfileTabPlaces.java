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
import com.questo.android.PlaceDetailsView;
import com.questo.android.R;
import com.questo.android.common.Constants;
import com.questo.android.model.Place;
import com.questo.android.model.Trophy;

public class ProfileTabPlaces extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_places);

        this.initView();

    }

    private void initView() {
        App app = (App) getApplicationContext();
        ModelManager mngr = app.getModelManager();

        List<Place> places = mngr.getPlacesForUser(app.getLoggedinUser());
        PlaceListAdapter adapter = new PlaceListAdapter(places);

        ListView placeList = (ListView) findViewById(R.id.ProfilePlaceList);
        placeList.setAdapter(adapter);
        placeList.setOnItemClickListener(new PlaceListener(places));

    }

    private class PlaceListener implements OnItemClickListener {

        private List<Place> places;

        public PlaceListener(List<Place> places) {
            this.places = places;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent showTrophy = new Intent(ProfileTabPlaces.this, PlaceDetailsView.class).putExtra(
                    Constants.TRANSITION_OBJECT_UUID, this.places.get(position).getUuid());
            startActivity(showTrophy);

        }

    }

    private class PlaceListAdapter extends BaseAdapter {

        private List<Place> places;

        public PlaceListAdapter(List<Place> places) {
            this.places = places;
        }

        @Override
        public int getCount() {
            return this.places.size();
        }

        @Override
        public Object getItem(int pos) {
            return this.places.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ProfileTabPlaces.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.place_listitem, null);
            }

            Place current = this.places.get(position);

            TextView trophyText = (TextView) v.findViewById(R.id.place_name);
            trophyText.setText(current.getName());

            ImageView trophyIcon = (ImageView) v.findViewById(R.id.place_img);
            trophyIcon.setImageDrawable(getResources().getDrawable(R.drawable.img_questo_sign_thumb));

            return v;
        }

    }
}
