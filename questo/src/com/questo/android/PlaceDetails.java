package com.questo.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.view.TopBar;

/**
 * A simple activity to show the details to a specific place.
 * The activity relies on some info sent within the extras in the intent.
 * For example, the place name and the number of questions.
 * @author petar
 *
 */
public class PlaceDetails extends Activity {

    private TopBar topbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);
        this.topbar = (TopBar)findViewById(R.id.topbar);
        this.topbar.addButtonLeftMost(getApplicationContext(), "+");
        
        this.init(this.getIntent().getExtras());
       
    }
    
    private void init(Bundle extras) {
        if (extras != null) {
            String lbl = extras.getString(Constants.TOPBAR_LABEL);
            if (lbl != null) {
                this.topbar.setTopBarLabel(lbl);
            }
            
            //TODO check questions
            int questions = extras.getInt(Constants.NR_QUESTIONS);
            TextView v = (TextView) findViewById(R.id.nr_questions);
            v.setText(Constants.NR_QUESTIONS_LABEL.replace(Constants.PLACEHOLDER, questions+""));
            
            questions = extras.getInt(Constants.NR_ANSWERED_QUESTIONS);
            v = (TextView) findViewById(R.id.answered_questions);
            v.setText(Constants.NR_ANSWERED_QUESTIONS_LABEL.replace(Constants.PLACEHOLDER, questions+""));
            
        } else {
            //TODO handle error.
            // show popup and go back on ok...
            this.topbar.setTopBarLabel("Some Place");
            
        }
        
        //init + button...
        Button b = (Button) findViewById(R.id.topbar_button);
        b.setOnClickListener(new AddQuestionListener());
        
        b = (Button) findViewById(R.id.start_quest);
        b.setOnClickListener(new StartQuestListener());
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
}
