package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class TutorialView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
        this.init();
    }

    private void init() {
        WebView browser = (WebView) findViewById(R.id.webview);
        browser.setVerticalScrollBarEnabled(false);
        browser.setHorizontalScrollBarEnabled(false);
        browser.loadUrl("file:///android_asset/tutorial.html");
        
        Button play = (Button) findViewById(R.id.btn_play);
        play.setOnClickListener(new ProceedOnClickListener());
    }

    private class ProceedOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), HomeView.class));
        }

    }
}
