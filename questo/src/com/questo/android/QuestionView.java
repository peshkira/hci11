package com.questo.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.InputType;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
        question.setText(Html.fromHtml("<h1>How tall (meters) is the southern tower of Stephansdom cathedral?</h1>"));

        LinearLayout layout = (LinearLayout) findViewById(R.id.question_layout);

        if (type.equals(Question.Type.MULTIPLE_CHOICE.name())) {
            String[] answers = { "123", "423", "231", "142" };
            this.createMultipleChoiceView(layout, answers);

        } else if (type.equals(Question.Type.PLAINTEXT.name())) {
            this.createPlainTextView(layout);

        } else if (type.equals(Question.Type.NUMBERS_GUESSING.name())) {
            this.createNumberGuessView(layout);
        }
        
        LayoutInflater li = getLayoutInflater();
        
        View buttons = li.inflate(R.layout.answer_buttons, null);
        layout.addView(buttons);

        this.startCounter(counter);
    }

    private void startCounter(final Button counter) {

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

    private void createNumberGuessView(LinearLayout layout) {
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        EditText input = new EditText(this);
        input.setLayoutParams(params);
        input.setSingleLine();
        input.setHint("e.g. 1234");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(input);

    }

    private void createPlainTextView(LinearLayout layout) {
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        EditText input = new EditText(this);
        input.setLayoutParams(params);
        input.setSingleLine();
        input.setHint("your answer");
        layout.addView(input);

    }

    private void createMultipleChoiceView(LinearLayout layout, String[] answers) {
        final RadioButton[] rb = new RadioButton[5];
        RadioGroup rg = new RadioGroup(this); // create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);// or RadioGroup.VERTICAL
        for (int i = 0; i < answers.length; i++) {
            rb[i] = new RadioButton(this);
            rg.addView(rb[i]); // the RadioButtons are added to the radioGroup
                               // instead of the layout
            rb[i].setText(Html.fromHtml("<big><b> &nbsp;&nbsp;" + answers[i] + "</b></big>"));
            rb[i].setTextColor(Color.BLACK);
        }
        layout.addView(rg);// you add the whole RadioGroup to the layout
        // layout.addView(submit);

    }

    private int calcProgress(int q, int count) {
        return (int) ((q * 100) / count);
    }

}
