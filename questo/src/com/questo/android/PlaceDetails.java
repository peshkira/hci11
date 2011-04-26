package com.questo.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Trophy;
import com.questo.android.model.Trophy.Type;
import com.questo.android.view.TopBar;

/**
 * A simple activity to show the details to a specific place. The activity
 * relies on some info sent within the extras in the intent. For example, the
 * place name and the number of questions.
 * 
 * @author petar
 * 
 */
public class PlaceDetails extends Activity {

    private TopBar topbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);
        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar.addButtonLeftMost(getApplicationContext(), "+");

        this.init(this.getIntent().getExtras());

    }

    private void init(Bundle extras) {
        if (extras != null) {
            String lbl = extras.getString(Constants.TOPBAR_LABEL);
            if (lbl != null) {
                this.topbar.setTopBarLabel(lbl);
            }

            // TODO check questions
            int questions = extras.getInt(Constants.NR_QUESTIONS);
            TextView v = (TextView) findViewById(R.id.nr_questions);
            v.setText(Html.fromHtml(Constants.NR_QUESTIONS_LABEL.replace(Constants.PLACEHOLDER, questions + "")));

            questions = extras.getInt(Constants.NR_ANSWERED_QUESTIONS);
            v = (TextView) findViewById(R.id.answered_questions);
            v.setText(Html.fromHtml(Constants.NR_ANSWERED_QUESTIONS_LABEL
                    .replace(Constants.PLACEHOLDER, questions + "")));

        } else {
            // TODO handle error.
            // show popup and go back on ok...
            this.topbar.setTopBarLabel("Some Place");

        }

        // init + button...
        Button b = (Button) findViewById(R.id.topbar_button);
        b.setOnClickListener(new AddQuestionListener());

        b = (Button) findViewById(R.id.start_quest);
        b.setOnClickListener(new StartQuestListener());
        b.setText(Html.fromHtml("<big><b>Start Quest</b></big><br/><br/><small>10 questions</small>"));

        ListView throphyList = (ListView) findViewById(R.id.placetrophies);
        PlaceTrophyAdapter adapt = new PlaceTrophyAdapter();
        adapt.addItem(new Trophy(UUIDgen.getUUID(), "The One Ring", Type.FOR_PLACE, null));
        adapt.addItem(new Trophy(UUIDgen.getUUID(), "The Light of Elendil", Type.FOR_PLACE, null));
        throphyList.setAdapter(adapt);
        // throphyList.setOnItemClickListener(new TrophyListener());
    }

    private class AddQuestionListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            System.out.println("Add Question");

        }

    }

    private class StartQuestListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            System.out.println("Start Quest");

        }

    }

    private class PlaceTrophyAdapter extends BaseAdapter {

        private ArrayList<Trophy> mData = new ArrayList<Trophy>();
        private LayoutInflater mInflater;

        public PlaceTrophyAdapter() {
            this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final Trophy item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Trophy trophy = mData.get(position);
            LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.place_trophy_listitem, null, false);

            ImageView image = (ImageView) view.findViewById(R.id.trophy_img);
            //image.setImageResource(trophy.getImgUrl);

            TextView name = (TextView) view.findViewById(R.id.trophy_name);
            name.setText(Html.fromHtml("<big>" + trophy.getName() + "</big>"));
            view.setOnClickListener(new TrophyClickListener(trophy));
            return view;
        }
    }
    
    private class TrophyClickListener implements OnClickListener {

        private Trophy trophy;
        
        public TrophyClickListener(Trophy t) {
            this.trophy = t;
        }
        
        @Override
        public void onClick(View v) {
            System.out.println(trophy.getName() + " clicked");
            //TODO navigate to trhophyview...
            
        }
        
    }
}
