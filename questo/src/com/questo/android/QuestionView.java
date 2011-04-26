package com.questo.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.questo.android.common.Constants;
import com.questo.android.view.TopBar;

public class QuestionView extends Activity {

    private TopBar topbar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        this.init(this.getIntent().getExtras());

    }

    private void init(Bundle extras) {
        int q = extras.getInt(Constants.QUESTIONS);
        int count = extras.getInt(Constants.NR_QUESTIONS);
        
        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar.setTopBarLabel(Constants.QUESTION_PROGRESS.replaceFirst("\\{\\}", q+"").replace("{}", count+"")); 
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
        bar.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
        bar.setProgress(this.calcProgress(q, count));
    }

    private int calcProgress(int q, int count) {
        return (int) ((q * 100)/count);
    }
}
