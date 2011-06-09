package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.questo.android.model.User;
import com.questo.android.view.TopBar;

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
        browser.loadUrl("file:///android_asset/test.html");
    }

    private class ProceedOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), HomeView.class));
        }

    }
}
