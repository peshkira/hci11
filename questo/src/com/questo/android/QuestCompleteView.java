package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.view.TopBar;

public class QuestCompleteView extends Activity {

    private TopBar topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_complete);

        this.init(this.getIntent().getExtras());
    }

    private void init(Bundle extras) {
        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar.setTopBarLabel("Quest Complete");

        TextView cngrtDetails = (TextView) findViewById(R.id.txt_congrats_details);
        cngrtDetails.setText(Constants.CONGRATS_DETAILS.replaceFirst("\\{\\}", 8+"").replace("{}", 10 + ""));
        
        TextView points = (TextView) findViewById(R.id.txt_points);
        points.setText(Html.fromHtml("<big><b>+ 8 points!</b></big>"));
        
        Button complete = (Button) findViewById(R.id.btn_quest_complete);
        complete.setOnClickListener(new QuestCompleteClickListener());
    }
    
    private class QuestCompleteClickListener implements OnClickListener {

        public void onClick(View v) {
            startActivity(new Intent(QuestCompleteView.this, HomeView.class));
            
        }
        
    }
}
