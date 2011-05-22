package com.questo.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class InviteView extends Activity {

    private InvitationListener il;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite);

        this.init();
    }

    private void init() {
        EditText email = (EditText) findViewById(R.id.email_box);
        EditText text = (EditText) findViewById(R.id.invite_box);

        Button btnInvite = (Button) findViewById(R.id.btn_invite_companion);
        this.il = new InvitationListener(email, text);
        btnInvite.setOnClickListener(this.il);

    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class InvitationListener implements OnClickListener {

        private EditText email;
        private EditText text;

        public InvitationListener(EditText email, EditText text) {
            this.email = email;
            this.text = text;
        }

        @Override
        public void onClick(View v) {
            String addr = email.getText().toString();
            String msg = text.getText().toString();

            if (addr == null || addr.equals("")) {
                Toast.makeText(InviteView.this, "Please provide a valid email address.", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(InviteView.this, "Please provide a valid email address.", Toast.LENGTH_LONG);
                finish();
            }
        }


    }
}
