package com.questo.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.questo.android.common.Constants;
import com.questo.android.model.Place;
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.PossibleAnswerNumberGuessing;
import com.questo.android.model.PossibleAnswerPlainText;
import com.questo.android.model.Question;

public class AddQuestion extends Activity {

	private Question question;
	private Place place;
	private ModelManager modelManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.question = new Question();
		this.modelManager = ((App) getApplication()).getModelManager();
		String placeUuid = this.getIntent().getStringExtra(Constants.EXTRA_ADD_QUESTION_PLACE_UUID);
		if(placeUuid!=null){
			this.place = this.modelManager.getGenericObjectByUuid(placeUuid, Place.class);
		}
		
		this.initView();
	}

	private void refreshObject() {
		this.question.setPlace(this.place);
		EditText questionText = (EditText) findViewById(R.id.AddQuestionQuestion);		
		this.question.setQuestion(questionText.getText().toString());
		
		RadioGroup questionType = (RadioGroup) findViewById(R.id.AddQuestionQuestionType);
		PossibleAnswer answer;
		switch (questionType.getCheckedRadioButtonId()) {
		case R.id.QuestionTypeMultipleChoice:
			this.question.setType(Question.Type.MULTIPLE_CHOICE);
			break;
		case R.id.QuestionTypeNumberGuessing:
			this.question.setType(Question.Type.NUMBERS_GUESSING);
			EditText numberGuessingEdit = (EditText)findViewById(R.id.NumberGuessingEdit);
			answer = new PossibleAnswerNumberGuessing(numberGuessingEdit.getText().toString());
			this.question.getPossibleAnswers().setAll(new PossibleAnswer[]{});
			this.question.getCorrectAnswer().set(answer);
			break;
		case R.id.QuestionTypeText:
			this.question.setType(Question.Type.PLAINTEXT);
			EditText textEdit = (EditText)findViewById(R.id.TextEdit);
			answer = new PossibleAnswerPlainText(textEdit.getText().toString());
			this.question.getPossibleAnswers().setAll(new PossibleAnswer[]{});
			this.question.getCorrectAnswer().set(answer);
			break;
		default:
			break;
		}

	}

	private void initView() {
		setContentView(R.layout.add_question);

		Button createButton = (Button) findViewById(R.id.AddQuestionCreateBtn);
		Button cancelButton = (Button) findViewById(R.id.AddQuestionCancelBtn);
		createButton.setOnClickListener(new AddQuestionListener());
		cancelButton.setOnClickListener(new AddQuestionListener());
	}

	private class AddQuestionListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.AddQuestionCreateBtn) {
				AddQuestion.this.refreshObject();
				AddQuestion.this.modelManager.create(AddQuestion.this.question,
						Question.class);
				AddQuestion.this.finish();
			}
			if (v.getId() == R.id.AddQuestionCancelBtn) {
				AddQuestion.this.finish();
			}
		}

	}
}
