package com.questo.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.model.Trophy;
import com.questo.android.view.FlexibleImageView;

public class TrophyRoomView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trophy_room);
        App app = ((App) getApplicationContext());
        ModelManager mngr = app.getModelManager();
        List<Trophy> trophies = mngr.getTrophyForUser(app.getLoggedinUser());
        GridView gridview = (GridView) findViewById(R.id.trophy_grid);
        gridview.setAdapter(new TrophyImageAdapter(trophies));
    }

    private class TrophyImageAdapter extends BaseAdapter {

        List<Trophy> trophies;

        public TrophyImageAdapter(List<Trophy> trophies) {
            if (trophies != null)
                this.trophies = trophies;
            else
                this.trophies = new ArrayList<Trophy>();
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
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView == null) {
                final LayoutInflater infl = (LayoutInflater) TrophyRoomView.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout layout = (RelativeLayout) infl.inflate(R.layout.home_icon, null);
                FlexibleImageView icon = (FlexibleImageView) layout.findViewById(R.id.home_icon_image);
                icon.setBackgroundResource(R.drawable.img_trophy_img);
                icon.setOnClickListener(new TrophyClickListener(trophies.get(position)));
                TextView tv = (TextView) layout.findViewById(R.id.home_icon_text);
                tv.setText(trophies.get(position).getName());
                v = layout;
            } else {
                v = convertView;
            }
            
            return v;
        }
    }
    
    private class TrophyClickListener implements OnClickListener {

        private Trophy trophy;

        public TrophyClickListener(Trophy trophy) {
            this.trophy = trophy;
        }
        
        @Override
        public void onClick(View v) {
            startActivity(new Intent(TrophyRoomView.this, TrophyView.class).putExtra(Constants.TRANSITION_OBJECT_UUID, this.trophy.getUuid()));
        }
        
    }
}
