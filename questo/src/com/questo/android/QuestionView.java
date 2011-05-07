package com.questo.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.questo.android.common.Constants;
import com.questo.android.model.Place;
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.PossibleAnswerImpl;
import com.questo.android.model.Quest;
import com.questo.android.model.Question;
import com.questo.android.model.Question.PossibleAnswers;
import com.questo.android.view.TopBar;

public class QuestionView extends Activity {

    private TopBar topbar;

    private ModelManager mngr;

    private RadioGroup rg;

    private int currentQuestion;

    private String questUuid;

    private Place place;

    private EditText input;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        this.init(this.getIntent().getExtras());

    }

    private void init(Bundle extras) {
        mngr = ((App) getApplicationContext()).getModelManager();

        questUuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);

        Quest quest = (Quest) mngr.getGenericObjectByUuid(questUuid, Quest.class);

        place = quest.getPlace();
        mngr.refresh(place, Place.class);

        int size = place.getQuestions().size();
        currentQuestion = extras.getInt(Constants.NR_ANSWERED_QUESTIONS);
        int count = 10;
        if (size < 10) {
            count = size;
        }

        Question qtn = (Question) place.getQuestions().toArray()[currentQuestion];
        String type = qtn.getType().name();

        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar.setTopBarLabel(Constants.QUESTION_PROGRESS.replaceFirst("\\{\\}", currentQuestion + 1 + "")
                .replace("{}", count + ""));

        ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
        bar.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
        bar.setProgress(this.calcProgress(currentQuestion + 1, count));

        final Button counter = this.topbar.addButtonLeftMost(getApplicationContext(), "30");
        counter.setClickable(false);

        TextView question = (TextView) findViewById(R.id.question);
        question.setText(Html.fromHtml("<h1>" + qtn.getQuestion() + "</h1>"));

        LinearLayout layout = (LinearLayout) findViewById(R.id.qtn_scroll);

        if (type.equals(Question.Type.MULTIPLE_CHOICE.name())) {
            PossibleAnswers answers = qtn.getPossibleAnswers();
            this.createMultipleChoiceView(layout, answers.getAll());

        } else if (type.equals(Question.Type.PLAINTEXT.name())) {
            this.createPlainTextView(layout);

        } else if (type.equals(Question.Type.NUMBERS_GUESSING.name())) {
            this.createNumberGuessView(layout);
        }

        LayoutInflater li = getLayoutInflater();

        View buttons = li.inflate(R.layout.answer_buttons, null);
        layout.addView(buttons);

        Button btnAnswer = (Button) findViewById(R.id.btn_answer);
        btnAnswer.setOnClickListener(new AnswerClickListener(qtn));
        Button btnNoIdea = (Button) findViewById(R.id.btn_noidea);
        btnNoIdea.setOnClickListener(new NoIdeaClickListener(qtn));

        this.startCounter(counter, qtn.getCorrectAnswer().get().getAnswer());
    }

    private void startCounter(final Button counter, final String answer) {

        new CountDownTimer(30000, 1000) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] secPattern = {0, 200, 100};
            long[] endPattern = {0, 300, 100, 200, 100};
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
                
                
                if (sec == 3) {
                    v.vibrate(secPattern, -1);
                } else if (sec == 2) {
                    v.vibrate(secPattern, -1);
                } else if (sec == 1) {
                    v.vibrate(secPattern, -1);
                }

                return color;
            }

            public void onFinish() {
                v.vibrate(endPattern, -1);
                counter.setText("0");
                Toast.makeText(QuestionView.this, "Time is up!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(QuestionView.this, QuestionResult.class);
                intent.putExtra(Constants.NR_ANSWERED_QUESTIONS, currentQuestion);
                intent.putExtra(Constants.TRANSITION_OBJECT_UUID, questUuid);
                intent.putExtra(Constants.QUEST_SIZE, QuestionView.this.place.getQuestions().size());
                intent.putExtra(Constants.CORRECT_ANSWER, answer);
                intent.putExtra(Constants.BOOL_CORRECT_ANSWER, false);
                startActivity(intent);

            }
        }.start();
    }

    private void createNumberGuessView(LinearLayout layout) {
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        input = new EditText(this);
        input.setLayoutParams(params);
        input.setSingleLine();
        input.setHint("e.g. 1234");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(input);

    }

    private void createPlainTextView(LinearLayout layout) {
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        input = new EditText(this);
        input.setLayoutParams(params);
        input.setSingleLine();
        input.setHint("your answer");
        layout.addView(input);

    }

    private void createMultipleChoiceView(LinearLayout layout, PossibleAnswer[] answers) {
        final RadioButton[] rb = new RadioButton[5];
        rg = new RadioGroup(this);
        rg.setOrientation(RadioGroup.VERTICAL);// or RadioGroup.VERTICAL
        for (int i = 0; i < answers.length; i++) {
            rb[i] = new RadioButton(this);
            rg.addView(rb[i]); // the RadioButtons are added to the radioGroup
                               // instead of the layout
            rb[i].setText(Html.fromHtml("<big><b> &nbsp;&nbsp;" + ((PossibleAnswerImpl) answers[i]).getAnswer()
                    + "</b></big>"));
            rb[i].setTextColor(Color.BLACK);
        }
        layout.addView(rg);// you add the whole RadioGroup to the layout
        // layout.addView(submit);

    }

    private int calcProgress(int q, int count) {
        return (int) ((q * 100) / count);
    }

    private class AnswerClickListener implements OnClickListener {

        private Question question;

        public AnswerClickListener(Question q) {
            this.question = q;
        }

        @Override
        public void onClick(View v) {
            boolean correct = false;
            boolean selected = true;

            PossibleAnswerImpl ca = (PossibleAnswerImpl) question.getCorrectAnswer().get();
            if (this.question.getType().equals(Question.Type.MULTIPLE_CHOICE)) {
                int sel = QuestionView.this.rg.getCheckedRadioButtonId();

                if (sel == -1) {
                    Toast.makeText(QuestionView.this, "Please select an answer!", Toast.LENGTH_SHORT).show();
                    selected = false;
                } else {
                    RadioButton btn = (RadioButton) findViewById(sel);
                    // contains because of the &nbsp; characters...
                    if (btn.getText().toString().contains(ca.getAnswer().toString())) {
                        correct = true;
                    }
                }

            } else if (this.question.getType().equals(Question.Type.NUMBERS_GUESSING)) {
                if (QuestionView.this.input.getText().toString().equals(ca.getAnswer().toString())) {
                    correct = true;
                }
                System.out.println(ca.getAnswer());

            } else if (this.question.getType().equals(Question.Type.PLAINTEXT)) {
                if (QuestionView.this.input.getText().toString().toLowerCase().equals(ca.getAnswer().toLowerCase())) {
                    correct = true;
                }
            }

            if (selected) {
                Intent intent = new Intent(QuestionView.this, QuestionResult.class);
                intent.putExtra(Constants.NR_ANSWERED_QUESTIONS, currentQuestion);
                intent.putExtra(Constants.TRANSITION_OBJECT_UUID, questUuid);
                intent.putExtra(Constants.QUEST_SIZE, QuestionView.this.place.getQuestions().size());
                intent.putExtra(Constants.CORRECT_ANSWER, question.getCorrectAnswer().get().getAnswer());
                intent.putExtra(Constants.BOOL_CORRECT_ANSWER, correct);
                startActivity(intent);
            }

        }

    }

    private class NoIdeaClickListener implements OnClickListener {

        private Question question;

        public NoIdeaClickListener(Question q) {
            this.question = q;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(QuestionView.this, QuestionResult.class);
            intent.putExtra(Constants.NR_ANSWERED_QUESTIONS, currentQuestion);
            intent.putExtra(Constants.TRANSITION_OBJECT_UUID, questUuid);
            intent.putExtra(Constants.QUEST_SIZE, QuestionView.this.place.getQuestions().size());
            intent.putExtra(Constants.CORRECT_ANSWER, question.getCorrectAnswer().get().getAnswer());
            intent.putExtra(Constants.BOOL_CORRECT_ANSWER, false);
            startActivity(intent);

        }

    }

}
