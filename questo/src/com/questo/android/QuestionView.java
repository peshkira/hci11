package com.questo.android;

import java.util.Date;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.questo.android.common.Constants;
import com.questo.android.helper.DisplayHelper;
import com.questo.android.helper.QuestoFieldFocusListener;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Place;
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.PossibleAnswerImpl;
import com.questo.android.model.Quest;
import com.questo.android.model.Question;
import com.questo.android.model.Question.PossibleAnswers;
import com.questo.android.model.QuestionAnswered;
import com.questo.android.view.TopBar;

public class QuestionView extends Activity {

    private TopBar topbar;
    private ModelManager mngr;
    private RadioGroup rg;
    private int currentQuestion;
    private int correctQtnAnswer;
    private String questUuid;
    private String currentTournamentTaskUuid;
    private Place place;
    private EditText input;
    private QuestionTimer timer;
    private App app;

    // Only used for backbutton presses:
    long lastBackPressTime = 0;
    Toast backToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        this.init(this.getIntent().getExtras());

    }

    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 2000) {
            backToast = Toast.makeText(this, "Press 'Back' again to end this quest - your progress will be lost!",
                    Toast.LENGTH_SHORT);
            backToast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (backToast != null)
                backToast.cancel();
            timer.cancel();
            startActivity(new Intent(this, HomeView.class));
        }
    }

    private void init(Bundle extras) {
        this.app = ((App) getApplicationContext());
        this.mngr = app.getModelManager();

        questUuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
        currentQuestion = extras.getInt(Constants.NR_ANSWERED_QUESTIONS);
        correctQtnAnswer = extras.getInt(Constants.NR_ANSWERED_QUESTIONS_CORRECT);
        currentTournamentTaskUuid = extras.getString(Constants.CURRENT_TOURNAMENT_TASK_UUID);

        Quest quest = (Quest) mngr.getGenericObjectByUuid(questUuid, Quest.class);

        place = quest.getPlace();
        mngr.refresh(place, Place.class);

        int size = place.getQuestions().size();
        int count = 10;
        if (size < 10) {
            count = size;
        }

        Question qtn = (Question) place.getQuestions().toArray()[currentQuestion];
        String type = qtn.getType().name();

        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar.setLabel(Constants.QUESTION_PROGRESS.replaceFirst("\\{\\}", currentQuestion + 1 + "").replace("{}",
                count + ""));

        ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
        bar.setProgressDrawable(getResources().getDrawable(R.drawable.bg_progressbar));
        bar.setProgress(this.calcProgress(currentQuestion + 1, count));

        final Button counter = this.topbar.addButtonLeftMost(getApplicationContext(), "30");
        counter.setClickable(false);
        counter.setTextSize(14.0f);
        counter.setBackgroundResource(R.drawable.img_counter1);

        ScrollView background = (ScrollView) findViewById(R.id.qtn_scroll);
        background.setOnTouchListener(new QuestoFieldFocusListener((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)));

        TextView question = (TextView) findViewById(R.id.question);
        question.setText(qtn.getQuestion());

        LinearLayout layout = (LinearLayout) findViewById(R.id.qtn_questioncontent);

        if (type.equals(Question.Type.MULTIPLE_CHOICE.name())) {
            PossibleAnswers answers = qtn.getPossibleAnswers();
            this.createMultipleChoiceView(layout, answers.getAll());

        } else if (type.equals(Question.Type.PLAINTEXT.name())) {
            this.createPlainTextView(layout);

        } else if (type.equals(Question.Type.NUMBERS_GUESSING.name())) {
            this.createNumberGuessView(layout);
        }

        LayoutInflater li = getLayoutInflater();

        LinearLayout buttonsLayout = (LinearLayout) findViewById(R.id.qtn_answerbuttons);
        View buttons = li.inflate(R.layout.answer_buttons, null);
        buttonsLayout.addView(buttons);

        Button btnAnswer = (Button) findViewById(R.id.btn_answer);
        btnAnswer.setOnClickListener(new AnswerClickListener(qtn));
        Button btnNoIdea = (Button) findViewById(R.id.btn_noidea);
        btnNoIdea.setOnClickListener(new NoIdeaClickListener(qtn));

        this.timer = new QuestionTimer(30000, 1000, counter, qtn);
        this.timer.start();

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
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_DATETIME_VARIATION_NORMAL);
        layout.addView(input);

    }

    private void createMultipleChoiceView(LinearLayout layout, PossibleAnswer[] answers) {
        final RadioButton[] rb = new RadioButton[5];
        rg = new RadioGroup(this);
        rg.setOrientation(RadioGroup.VERTICAL);
        for (int i = 0; i < answers.length; i++) {
            rb[i] = new RadioButton(this);
            rg.addView(rb[i]);
            // TODO for some reason, the shadow is always grey! wtf?
            // rb[i].setShadowLayer(1.0f, 1.0f, 1.0f,
            // R.color.light_text_shadow);
            rb[i].setText(Html.fromHtml("<big>" + ((PossibleAnswerImpl) answers[i]).getAnswer() + "</big>"));
            rb[i].setTextColor(Color.BLACK);

            int paddingTopAndBottom = DisplayHelper.dpToPixel(5, this);
            int paddingLeft = DisplayHelper.dpToPixel(50, this);
            rb[i].setPadding(paddingLeft, paddingTopAndBottom, 0, paddingTopAndBottom);
        }
        layout.addView(rg);
    }

    private int calcProgress(int q, int count) {
        return (int) ((q * 100) / count);
    }

    private void putExtrasForNextView(Intent intent, Question question, boolean questionCorrect) {
        intent.putExtra(Constants.NR_ANSWERED_QUESTIONS, currentQuestion);
        intent.putExtra(Constants.NR_ANSWERED_QUESTIONS_CORRECT, correctQtnAnswer);
        intent.putExtra(Constants.TRANSITION_OBJECT_UUID, questUuid);
        intent.putExtra(Constants.QUEST_SIZE, QuestionView.this.place.getQuestions().size());
        intent.putExtra(Constants.QUESTION, question.getQuestion());
        intent.putExtra(Constants.CORRECT_ANSWER, question.getCorrectAnswer().get().getAnswer());
        intent.putExtra(Constants.BOOL_CORRECT_ANSWER, questionCorrect);
        intent.putExtra(Constants.CURRENT_TOURNAMENT_TASK_UUID, currentTournamentTaskUuid);
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

            } else if (this.question.getType().equals(Question.Type.PLAINTEXT)) {
                if (QuestionView.this.input.getText().toString().toLowerCase().equals(ca.getAnswer().toLowerCase())) {
                    correct = true;
                }
            }

            if (selected) {

                if (correct) {
                    QuestionAnswered qa = new QuestionAnswered(UUIDgen.getUUID(), app.getLoggedinUser(), this.question,
                            new Date());
                    mngr.create(qa, QuestionAnswered.class);
                    correctQtnAnswer++;
                }

                Intent intent = new Intent(QuestionView.this, QuestionResult.class);
                putExtrasForNextView(intent, question, correct);
                QuestionView.this.timer.cancel();
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
            putExtrasForNextView(intent, question, false);
            QuestionView.this.timer.cancel();
            startActivity(intent);

        }

    }

    private class QuestionTimer extends CountDownTimer {

        private long[] secPattern = { 0, 200, 100 };
        private long[] endPattern = { 0, 300, 100, 200, 100 };
        private Vibrator v;
        private Button counter;
        private Question question;
        private long calls;
        private int call;

        public QuestionTimer(long time, long thick, Button counter, Question qtn) {
            super(time, thick);
            this.v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            this.counter = counter;
            this.question = qtn;
            this.calls = (time / thick / 10);
            this.call = 1;
        }

        @Override
        public void onFinish() {
            v.vibrate(endPattern, -1);
            counter.setText("0");
            Toast.makeText(QuestionView.this, "Time is up!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(QuestionView.this, QuestionResult.class);
            putExtrasForNextView(intent, question, false);
            startActivity(intent);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            long sec = (millisUntilFinished / 1000);
            counter.setTextColor(this.calcColor(sec));
            counter.setText("" + sec);
            if (sec % this.calls == 0) {
                counter.setBackgroundResource(this.calcCircleImage());
            }

        }

        private int calcCircleImage() {
            this.call++;
            switch (this.call) {
            case 1:
                return R.drawable.img_counter1;
            case 2:
                return R.drawable.img_counter2;
            case 3:
                return R.drawable.img_counter3;
            case 4:
                return R.drawable.img_counter4;
            case 5:
                return R.drawable.img_counter5;
            case 6:
                return R.drawable.img_counter6;
            case 7:
                return R.drawable.img_counter7;
            case 8:
                return R.drawable.img_counter8;
            case 9:
                return R.drawable.img_counter9;
            case 10:
                return R.drawable.img_counter10;
            default:
                return R.drawable.img_counter1;
            }
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

    }
}
