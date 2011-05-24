package com.questo.android;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.helper.FontHelper;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Place;
import com.questo.android.model.Quest;
import com.questo.android.model.Trophy;
import com.questo.android.model.Trophy.Type;
import com.questo.android.model.TrophyForUser;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentTask;
import com.questo.android.model.TournamentTaskDone;
import com.questo.android.model.User;
import com.questo.android.view.TopBar;

public class QuestCompleteView extends Activity {

    private TopBar topbar;
    private String currentTournamentTaskUuid;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_complete);
        init(this.getIntent().getExtras());
        saveTournamentTaskDone();
    }

    public void onBackPressed() {
        startActivity(new Intent(this, HomeView.class));
    }

    private void init(Bundle extras) {
        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar.setLabel("Quest Complete");

        int size = extras.getInt(Constants.QUEST_SIZE);
        int correctAns = extras.getInt(Constants.NR_ANSWERED_QUESTIONS_CORRECT);
        String questUuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
        currentTournamentTaskUuid = extras.getString(Constants.CURRENT_TOURNAMENT_TASK_UUID);

        TextView congrats = (TextView) findViewById(R.id.txt_congrats);
        congrats.setTypeface(FontHelper.getMedievalFont(this));

        TextView cngrtDetails = (TextView) findViewById(R.id.txt_congrats_details);
        cngrtDetails.setText(Constants.CONGRATS_DETAILS.replaceFirst("\\{\\}", correctAns + "")
                .replace("{}", size + ""));

        TextView points = (TextView) findViewById(R.id.txt_points);
        points.setText(Html.fromHtml("<big><b>+ " + correctAns + " points!</b></big>"));

        Button complete = (Button) findViewById(R.id.btn_quest_complete);
        complete.setOnClickListener(new QuestCompleteClickListener());

        app = (App) getApplicationContext();
        User user = app.getLoggedinUser();
        app.getModelManager().refresh(user, User.class);
        user.incrementPointsBy(correctAns);
        app.getModelManager().update(user, User.class);

        this.giveTrophies(correctAns, questUuid);
    }

    private void giveTrophies(int points, String questUuid) {
        ModelManager manager = this.app.getModelManager();
        User user = app.getLoggedinUser();
        if (points > 5) {
            Trophy trophy = manager.getNewTrophyForUser(user, Type.GLOBAL);
            if (trophy != null) {
                TrophyForUser tfu = new TrophyForUser(UUIDgen.getUUID(), user, trophy.getUuid(), null, null, new Date());
                manager.create(tfu, TrophyForUser.class);
            }
        }

        Quest quest = manager.getGenericObjectByUuid(questUuid, Quest.class);
        Place place = manager.refresh(quest.getPlace(), Place.class);
        System.out.println("PLACE: " + place.getName());
        Trophy t = manager.getGenericObjectByUuid(place.getTrophy(), Trophy.class);
        System.out.println("TROPHY: " + t.getName());
        
        if (t != null) {
            t = manager.refresh(t, Trophy.class);
            TrophyForUser tfu = new TrophyForUser(UUIDgen.getUUID(), user, t.getUuid(), place.getUuid(), null,
                    new Date());
            manager.create(tfu, TrophyForUser.class);
        }

    }

    private void saveTournamentTaskDone() {
    	if (currentTournamentTaskUuid != null) {
        	TournamentTaskDone done = new TournamentTaskDone(UUIDgen.getUUID(), app.getLoggedinUser(), currentTournamentTaskUuid, new Date());
        	app.getModelManager().create(done, TournamentTaskDone.class);
    	}
    }
    
    private class QuestCompleteClickListener implements OnClickListener {

        public void onClick(View v) {
        	if (currentTournamentTaskUuid == null)
        		startActivity(new Intent(QuestCompleteView.this, QuestMapView.class));
        	else {
        		TournamentTask task = app.getModelManager().getGenericObjectByUuid(currentTournamentTaskUuid, TournamentTask.class);
        		Tournament tournament = app.getModelManager().refresh(task.getTournament(), Tournament.class);
        		Intent intent = new Intent(QuestCompleteView.this, QuestMapView.class);
        		intent.putExtra(QuestMapView.EXTRA_TOURNAMENT_UUID, tournament.getUuid());
        		startActivity(intent);
        	}

        }

    }
}
