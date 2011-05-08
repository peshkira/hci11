package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.view.FlexibleImageView;
import com.questo.android.view.TopBar;

public class QuestionResult extends Activity {

    private TopBar topbar;

    private ModelManager mngr;

    private int currentQuestion;

    private String questUuid;

    private String correctAnswer;

    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_result);
        this.init(getIntent().getExtras());

    }

    private void init(Bundle extras) {
        mngr = ((App) getApplicationContext()).getModelManager();

        size = extras.getInt(Constants.QUEST_SIZE);
        boolean correct = extras.getBoolean(Constants.BOOL_CORRECT_ANSWER);
        questUuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
        currentQuestion = extras.getInt(Constants.NR_ANSWERED_QUESTIONS);
        correctAnswer = extras.getString(Constants.CORRECT_ANSWER);
        int count = 10;
        if (size < 10) {
            count = size;
        }

        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar.setTopBarLabel(Constants.QUESTION_PROGRESS.replaceFirst("\\{\\}", currentQuestion + 1 + "")
                .replace("{}", count + ""));

        ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
        bar.setProgressDrawable(getResources().getDrawable(R.drawable.bg_progressbar));
        bar.setProgress(this.calcProgress(currentQuestion + 1, count));

        FlexibleImageView imgAnswer = (FlexibleImageView) findViewById(R.id.img_answer);
        TextView txtAnswer = (TextView) findViewById(R.id.txt_answer);

        if (correct) {
            imgAnswer.setBackgroundResource(R.drawable.img_check);
            txtAnswer.setText(Html.fromHtml("<big>Your answer is correct!</big>"));
        } else {
            imgAnswer.setBackgroundResource(R.drawable.img_cross);
            txtAnswer.setText(Html
                    .fromHtml("<big><b>Your answer is incorrect!</b></big><br/><br/>The correct answer is:<br/><b>" + correctAnswer + "</b>"));
        }

        Button btnNext = (Button) findViewById(R.id.btn_next_question);
        btnNext.setOnClickListener(new NextQuestionClickListener());

    }

    private int calcProgress(int q, int count) {
        return (int) ((q * 100) / count);
    }

    private class NextQuestionClickListener implements OnClickListener {

        @Override
        public void onClick(View btn) {
            Intent intent;
            if (QuestionResult.this.size > QuestionResult.this.currentQuestion + 1) {
                intent = new Intent(QuestionResult.this, QuestionView.class);
                intent.putExtra(Constants.TRANSITION_OBJECT_UUID, QuestionResult.this.questUuid);
                intent.putExtra(Constants.NR_ANSWERED_QUESTIONS, QuestionResult.this.currentQuestion + 1);
            } else {
                intent = new Intent(QuestionResult.this, HomeView.class);
            }
            
            startActivity(intent);
        }

    }
}
