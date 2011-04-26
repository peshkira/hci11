package com.questo.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.model.Question;
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
        String type = extras.getString(Constants.QUESTION_TYPE);

        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar
                .setTopBarLabel(Constants.QUESTION_PROGRESS.replaceFirst("\\{\\}", q + "").replace("{}", count + ""));

        ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
        bar.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
        bar.setProgress(this.calcProgress(q, count));

        final Button counter = this.topbar.addButtonLeftMost(getApplicationContext(), "30");
        counter.setClickable(false);

        TextView question = (TextView) findViewById(R.id.question);
        question.setText(Html.fromHtml("<h1>How tall is the southern tower of Stephansdom cathedral?</h1>"));

        // TODO init components
        if (type.equals(Question.Type.MULTIPLE_CHOICE.name())) {

        } else if (type.equals(Question.Type.PLAINTEXT.name())) {

        } else if (type.equals(Question.Type.NUMBERS_GUESSING.name())) {

        }

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                long sec = (millisUntilFinished / 1000);
                counter.setTextColor(this.calcColor(sec));
                counter.setText("" + sec);
            }

            private int calcColor(long sec) {
                int color = Color.GREEN;

                if (sec < 15 && sec > 5) {
                    color = Color.YELLOW;
                } else if (sec <= 5) {
                    color = Color.RED;
                }

                return color;
            }

            public void onFinish() {
                counter.setText("0");
                // TODO end activity
            }
        }.start();
    }

    private int calcProgress(int q, int count) {
        return (int) ((q * 100) / count);
    }

}
