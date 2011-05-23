package com.questo.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.questo.android.helper.DisplayHelper;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Place;
import com.questo.android.model.PlaceVisitation;
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

    private int questionCount;

    private ModelManager mngr;

    private App app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);
        this.topbar = (TopBar) findViewById(R.id.topbar);
        // init + button...
        Button b = this.topbar.addImageButtonLeftMost(getApplicationContext(), R.drawable.img_plus);
        b.setOnClickListener(new AddQuestionListener());
        
        this.app = ((App) getApplicationContext());
        this.mngr = this.app.getModelManager();

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
                this.questionCount = place.getQuestions().size();
                this.topbar.setLabel(place.getName());
                TextView v = (TextView) findViewById(R.id.nr_questions);
                v.setText(Html.fromHtml(Constants.NR_QUESTIONS_LABEL.replace(Constants.PLACEHOLDER, ""
                        + place.getQuestions().size())));

                v = (TextView) findViewById(R.id.answered_questions);
                // TODO
                v.setText(Html.fromHtml(Constants.NR_ANSWERED_QUESTIONS_LABEL.replace(Constants.PLACEHOLDER, "" + 0)));

                PlaceVisitation visitation = mngr.getPlaceVisitationForUserAndPlace(app.getLoggedinUser(),
                        place.getUuid());
                if (visitation == null) {
                    visitation = new PlaceVisitation(UUIDgen.getUUID(), app.getLoggedinUser(), place.getUuid(),
                            new Date());
                    mngr.create(visitation, PlaceVisitation.class);
                } else {
                    visitation.setVisitedAt(new Date());
                    mngr.update(visitation, PlaceVisitation.class);
                }

            }
        } else {
            // fetch test place for now...
            this.topbar.setLabel("Fetch failed");
            System.out.println("Fetch failed");
            finishActivity(RESULT_CANCELED);
        }

        Button b = (Button) findViewById(R.id.start_quest);
        if (questionCount > 0) {
            b.setOnClickListener(new StartQuestListener());
            int questionCountForQuest = Math.min(Constants.QUESTIONS_PER_PLACE, questionCount);
            b.setText(Html.fromHtml("<big><b>Start Quest</b></big><small><br/><br/>" + questionCountForQuest
                    + " questions</small>"));
        } else {
            b.setEnabled(false);
            b.setText(Html.fromHtml("<big><b>Start Quest</b></big><small><br/><br/>No questions</small>"));
            b.setBackgroundResource(R.drawable.btn_round_disabled);
            int dp20 = DisplayHelper.dpToPixel(20, this);
            b.setPadding(dp20, dp20, dp20, dp20);
        }

        ListView trophyList = (ListView) findViewById(R.id.placetrophies);
        trophyList.setEmptyView(findViewById(R.id.empty_trophylist_text));
        PlaceTrophyAdapter adapt = new PlaceTrophyAdapter();
        List<Trophy> trophies = mngr.getTrophyForUserAndPlace(this.app.getLoggedinUser(), this.place.getUuid());
        if (trophies != null) {
            adapt.addItems(trophies);
        }
        trophyList.setAdapter(adapt);
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
            navTo.putExtra(Constants.NR_ANSWERED_QUESTIONS_CORRECT, 0);
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

        public void addItems(final List<Trophy> items) {
            mData.addAll(items);
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
            LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.trophy_listitem, null, false);

            ImageView image = (ImageView) view.findViewById(R.id.trophy_img);
            // image.setImageResource(trophy.getImgUrl);

            TextView name = (TextView) view.findViewById(R.id.trophy_name);
            name.setText(trophy.getName());
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
            startActivity(new Intent(PlaceDetailsView.this, TrophyView.class).putExtra(
                    Constants.TRANSITION_OBJECT_UUID, trophy.getUuid()));

        }

    }
}
