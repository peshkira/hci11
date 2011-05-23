package com.questo.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

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

		question = new Question();
		modelManager = ((App) getApplication()).getModelManager();
		String[] uuids = this.getIntent().getStringArrayExtra(
				Constants.TRANSITION_OBJECT_UUID);
		if ((uuids.length >= 1) && (uuids[0] != null)) {
			place = modelManager.getGenericObjectByUuid(uuids[0], Place.class);
		}

		this.initView();
	}

	private void refreshObject() {
		question.setPlace(this.place);
		EditText questionText = (EditText) findViewById(R.id.AddQuestionQuestion);
		question.setQuestion(questionText.getText().toString());

		RadioGroup questionType = (RadioGroup) findViewById(R.id.AddQuestionQuestionType);
		PossibleAnswer answer;
		switch (questionType.getCheckedRadioButtonId()) {
		case R.id.QuestionTypeMultipleChoice:
			this.question.setType(Question.Type.MULTIPLE_CHOICE);
			break;
		case R.id.QuestionTypeNumberGuessing:
			this.question.setType(Question.Type.NUMBERS_GUESSING);
			EditText numberGuessingEdit = (EditText) findViewById(R.id.NumberGuessingEdit);
			answer = new PossibleAnswerNumberGuessing(numberGuessingEdit
					.getText().toString());
			this.question.getPossibleAnswers().setAll(new PossibleAnswer[] {});
			this.question.getCorrectAnswer().set(answer);
			break;
		case R.id.QuestionTypeText:
			this.question.setType(Question.Type.PLAINTEXT);
			EditText textEdit = (EditText) findViewById(R.id.TextEdit);
			answer = new PossibleAnswerPlainText(textEdit.getText().toString());
			this.question.getPossibleAnswers().setAll(new PossibleAnswer[] {});
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
		RadioGroup questionType = (RadioGroup) findViewById(R.id.AddQuestionQuestionType);
		questionType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				LinearLayout multipleChoice = (LinearLayout) findViewById(R.id.TypeMultipleChoiceLayout);
				RelativeLayout numberGuessing = (RelativeLayout) findViewById(R.id.TypeNumberGuessing);
				RelativeLayout text = (RelativeLayout) findViewById(R.id.TypeText);
				switch (checkedId) {
				case R.id.QuestionTypeMultipleChoice:
					multipleChoice.setVisibility(View.VISIBLE);
					numberGuessing.setVisibility(View.GONE);
					text.setVisibility(View.GONE);

					LayoutInflater inflater = (LayoutInflater) AddQuestion.this
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					LinearLayout parent = (LinearLayout) findViewById(R.id.TypeMultipleChoice);					
					LinearLayout inflated = (LinearLayout)inflater
							.inflate(R.layout.add_question_choice_layout,
									parent);
					LinearLayout self = (LinearLayout)inflated.findViewById(R.id.MultipleChoiceLayout);
					EditText choiceEdit = (EditText) inflated
							.findViewById(R.id.MultipleChoiceEdit);
					Button removeBtn = (Button)inflated.findViewById(R.id.MultipleChoiceRemoveBtn);
					removeBtn.setOnClickListener(new RemoveClickListener(inflated, self));
					
					break;
				case R.id.QuestionTypeNumberGuessing:
					multipleChoice.setVisibility(View.GONE);
					numberGuessing.setVisibility(View.VISIBLE);
					text.setVisibility(View.GONE);
					break;
				case R.id.QuestionTypeText:
					multipleChoice.setVisibility(View.GONE);
					numberGuessing.setVisibility(View.GONE);
					text.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}
		});
	}

	private class RemoveClickListener implements OnClickListener {

		private LinearLayout parent;
		private LinearLayout self;

		public RemoveClickListener(LinearLayout parent, LinearLayout self) {
			this.parent = parent;
			this.self = self;
		}

		@Override
		public void onClick(View v) {
			parent.removeView(self);
		}
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
