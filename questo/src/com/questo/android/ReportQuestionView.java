package com.questo.android;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.questo.android.common.Constants;

public class ReportQuestionView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.question_report);
        this.init(this.getIntent().getExtras());
    }

    private void init(Bundle extras) {
        String qtn = extras.getString(Constants.QUESTION);
       
        TextView question = (TextView) findViewById(R.id.txt_question);
        question.setText(Html.fromHtml("<big><b>" + qtn + "</b></big>"));

        Button submit = (Button) findViewById(R.id.btn_submit_report);
        submit.setOnClickListener(new SubmitReportClickListener());
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner_reason);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.report_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_change_out, R.anim.push_down_in);
    }
    
    private class SubmitReportClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.no_change_out, R.anim.push_down_in);
        }
        
    }
}
