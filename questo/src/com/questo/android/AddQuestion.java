package com.questo.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.questo.android.model.Question;

public class AddQuestion extends Activity {

	private Question question;
	private ModelManager modelManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.question = new Question();
		this.modelManager = ((App)getApplication()).getModelManager();
		this.initView();
	}
	
	private void refreshObject(){
		EditText questionText = (EditText)findViewById(R.id.AddQuestionQuestion);
		RadioGroup questionType = (RadioGroup)findViewById(R.id.AddQuestionQuestionType);
		
		
	}	

	private void initView() {
		setContentView(R.layout.add_question);

		Button createButton = (Button)findViewById(R.id.AddQuestionCreateBtn);
		Button cancelButton = (Button)findViewById(R.id.AddQuestionCancelBtn);
		createButton.setOnClickListener(new AddQuestionListener());
		cancelButton.setOnClickListener(new AddQuestionListener());
	}

	private class AddQuestionListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.AddQuestionCreateBtn){
				AddQuestion.this.refreshObject();
				AddQuestion.this.modelManager.create(AddQuestion.this.question, Question.class);
				AddQuestion.this.finish();
			}
			if(v.getId()==R.id.AddQuestionCancelBtn){
				AddQuestion.this.finish();
			}			
		}
		
	}
}
