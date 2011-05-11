package com.questo.android;


import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.model.Trophy;
import com.questo.android.view.FlexibleImageView;
import com.questo.android.view.TopBar;

public class TrophyView extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.throphy);

		this.init(this.getIntent().getExtras());
	}

    private void init(Bundle extras) {
        ModelManager mngr = ((App) getApplicationContext()).getModelManager();
        String uuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
        Trophy trophy = mngr.getGenericObjectByUuid(uuid, Trophy.class);
        
        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        topbar.setLabel(trophy.getName());
        
        FlexibleImageView img = (FlexibleImageView) findViewById(R.id.trophy_img);
        img.setBackgroundResource(R.drawable.img_trophy_img);
        
        TextView txt = (TextView) findViewById(R.id.trophy_text);
        txt.setText(Html.fromHtml("<big><b>"+ trophy.getText() +"</b></big>"));
    }
}
