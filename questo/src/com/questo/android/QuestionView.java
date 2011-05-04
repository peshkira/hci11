package com.questo.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.InputType;
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
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.PossibleAnswerMultipleChoice;
import com.questo.android.model.Quest;
import com.questo.android.model.Question;
import com.questo.android.model.Question.PossibleAnswers;
import com.questo.android.view.TopBar;

public class QuestionView extends Activity {

    private TopBar topbar;

    private ModelManager mngr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        this.init(this.getIntent().getExtras());

    }

    private void init(Bundle extras) {
//        mngr = ((App) getApplicationContext()).getModelManager();
//
//        String uuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
//
//        Quest quest = (Quest) mngr.getGenericObjectByUuid(uuid, Quest.class);
//        if (quest.getPlace().getQuestions() == null) {
//            System.out.println("NULNULLNULLNULL");
//        }
//        
//        int size = quest.getPlace().getQuestions().size();
//        int q = extras.getInt(Constants.NR_ANSWERED_QUESTIONS);
//        int count = 10;
//        if (size < 10) {
//            count = size;
//        }
//
//        Question qtn = (Question) quest.getPlace().getQuestions().toArray()[q];
//        String type = qtn.getType().name();
//
//        this.topbar = (TopBar) findViewById(R.id.topbar);
//        this.topbar
//                .setTopBarLabel(Constants.QUESTION_PROGRESS.replaceFirst("\\{\\}", q + "").replace("{}", count + ""));
//
//        ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
//        bar.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
//        bar.setProgress(this.calcProgress(q, count));
//
//        final Button counter = this.topbar.addButtonLeftMost(getApplicationContext(), "30");
//        counter.setClickable(false);
//
//        TextView question = (TextView) findViewById(R.id.question);
//        question.setText(Html.fromHtml("<h1>" + qtn.getQuestion() + "</h1>"));
//
//        LinearLayout layout = (LinearLayout) findViewById(R.id.question_layout);
//
//        if (type.equals(Question.Type.MULTIPLE_CHOICE.name())) {
//            PossibleAnswers answers = qtn.getPossibleAnswers();
//            this.createMultipleChoiceView(layout, answers.getAll());
//
//        } else if (type.equals(Question.Type.PLAINTEXT.name())) {
//            this.createPlainTextView(layout);
//
//        } else if (type.equals(Question.Type.NUMBERS_GUESSING.name())) {
//            this.createNumberGuessView(layout);
//        }
//
//        LayoutInflater li = getLayoutInflater();
//
//        View buttons = li.inflate(R.layout.answer_buttons, null);
//        layout.addView(buttons);
//
//        this.startCounter(counter);
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

    private void createMultipleChoiceView(LinearLayout layout, PossibleAnswer[] answers) {
        final RadioButton[] rb = new RadioButton[5];
        RadioGroup rg = new RadioGroup(this); // create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);// or RadioGroup.VERTICAL
        for (int i = 0; i < answers.length; i++) {
            rb[i] = new RadioButton(this);
            rg.addView(rb[i]); // the RadioButtons are added to the radioGroup
                               // instead of the layout
            rb[i].setText(Html.fromHtml("<big><b> &nbsp;&nbsp;" + ((PossibleAnswerMultipleChoice) answers[i]).getText() + "</b></big>"));
            rb[i].setTextColor(Color.BLACK);
        }
        layout.addView(rg);// you add the whole RadioGroup to the layout
        // layout.addView(submit);

    }

    private int calcProgress(int q, int count) {
        return (int) ((q * 100) / count);
    }

}
