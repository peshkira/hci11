package com.questo.android;

import java.util.ArrayList;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.CloseableIterator;
import com.questo.android.common.Constants;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Place;
import com.questo.android.model.Quest;
import com.questo.android.model.QuestHasQuestion;
import com.questo.android.model.Question;
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
public class PlaceDetailsView extends Activity {

    private TopBar topbar;
    
    private Place place;
    
    private ModelManager mngr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);
        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar.addButtonLeftMost(getApplicationContext(), "+");
        this.mngr = ((App) getApplicationContext()).getModelManager();

        this.init(this.getIntent().getExtras());

    }

    private void init(Bundle extras) {
        String id = "";
        Object object;

        if (extras != null) {
            id = extras.getString(Constants.TRANSITION_OBJECT_UUID);
            object = mngr.getGenericObjectByUuid(id, Place.class);
        } else {
            object = mngr.getGenericObjectById(1, Place.class);
        }
        
        if (object != null) {
            if (object instanceof Place) {
                this.place = (Place) object;
                this.topbar.setTopBarLabel(place.getName());
                TextView v = (TextView) findViewById(R.id.nr_questions);
                v.setText(Html.fromHtml(Constants.NR_QUESTIONS_LABEL.replace(Constants.PLACEHOLDER, ""
                        + place.getQuestions().size())));

                v = (TextView) findViewById(R.id.answered_questions);
                // TODO
                v.setText(Html.fromHtml(Constants.NR_ANSWERED_QUESTIONS_LABEL.replace(Constants.PLACEHOLDER, "" + 0)));
            }
        } else {
            // fetch test place for now...
            this.topbar.setTopBarLabel("Fetch failed");
        }

        // init + button...
        Button b = (Button) findViewById(R.id.topbar_button);
        b.setOnClickListener(new AddQuestionListener());

        b = (Button) findViewById(R.id.start_quest);
        b.setOnClickListener(new StartQuestListener());
        b.setText(Html.fromHtml("<big><b>Start Quest</b></big><br/><br/><small>10 questions</small>"));

        ListView throphyList = (ListView) findViewById(R.id.placetrophies);
        PlaceTrophyAdapter adapt = new PlaceTrophyAdapter();
        adapt.addItem(new Trophy(UUIDgen.getUUID(), "The One Ring", Type.FOR_PLACE));
        adapt.addItem(new Trophy(UUIDgen.getUUID(), "The Light of Elendil", Type.FOR_PLACE));
        throphyList.setAdapter(adapt);
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
            Quest quest = new Quest(UUIDgen.getUUID(), PlaceDetailsView.this.place);
            quest.setCompletionState(Quest.Completion.INITIALIZED);
            mngr.create(quest, Quest.class);
            
            if (place.getQuestions().size() > 10) {
                CloseableIterator<Question> iter = place.getQuestions().iterator();
                int i = 10;
                while (iter.hasNext() && i > 0) {
                    Question next = iter.next();
                    QuestHasQuestion qq = new QuestHasQuestion(quest, next, null);
                    mngr.create(qq, QuestHasQuestion.class);
                }
            } else {
                for (Question q : place.getQuestions()) {
                    QuestHasQuestion qq = new QuestHasQuestion(quest, q, null);
                    mngr.create(qq, QuestHasQuestion.class);
                }
            }
            
            Intent navTo = new Intent(PlaceDetailsView.this, QuestionView.class);
            navTo.putExtra(Constants.TRANSITION_OBJECT_UUID, quest.getUuid());
            navTo.putExtra(Constants.NR_ANSWERED_QUESTIONS, 0);
            startActivity(navTo);

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
            // image.setImageResource(trophy.getImgUrl);

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
            startActivity(new Intent(PlaceDetailsView.this, TrophyView.class));

        }

    }
}
