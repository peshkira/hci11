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
        if (this.il.isPopupShown()) {
            this.il.closePopup();
        }
        super.onBackPressed();
    }

    private class InvitationListener implements OnClickListener {

        private EditText email;
        private EditText text;
        private boolean shown;
        private PopupWindow pw;

        public InvitationListener(EditText email, EditText text) {
            this.email = email;
            this.text = text;
        }

        public void closePopup() {
            this.pw.dismiss();
        }

        public boolean isPopupShown() {
            return this.shown;
        }

        @Override
        public void onClick(View v) {
            String addr = email.getText().toString();
            String msg = text.getText().toString();

            if (addr == null || addr.equals("")) {
                this.displayPopup("The email provided is not valid", false);

            } else {
                this.displayPopup("The invitation has been sent!", true);
            }
        }

        private void displayPopup(String msg, final boolean finishOnDismiss) {
            this.createPopup();
            
            final TextView txt = (TextView) pw.getContentView().findViewById(R.id.poput_txt);
            txt.setText(Html.fromHtml("<big><b>" + msg + "</b></big>"));

            final Button button1 = (Button) pw.getContentView().findViewById(R.id.popup_menu_button1);
            button1.setText("OK");
            button1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View vv) {
                    pw.dismiss();
                    InvitationListener.this.shown = false;
                    if (finishOnDismiss) {
                        InviteView.this.finish();
                    }
                }
            });

            final Button button2 = (Button) pw.getContentView().findViewById(R.id.popup_menu_button2);
            button2.setVisibility(View.GONE);
            
            pw.showAtLocation(pw.getContentView(), Gravity.CENTER, 0, 0);
            this.shown = true;
        }

        private void createPopup() {
            LayoutInflater inflater = (LayoutInflater) InviteView.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popup = inflater.inflate(R.layout.popup_window_logout, (ViewGroup) findViewById(R.id.popup_logout));

            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int width = (int) (display.getWidth() * 0.80);
            int height = (int) (display.getHeight() * 0.25);

            this.pw = new PopupWindow(popup, width, height);
        }

    }
}
