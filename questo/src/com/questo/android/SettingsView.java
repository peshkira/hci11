package com.questo.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.questo.android.view.TopBar;

public class SettingsView extends PreferenceActivity {

    private boolean toggled = false;

    private View popup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        addPreferencesFromResource(R.xml.preferences);

        this.init();
    }

    private void init() {
        App app = (App) this.getApplicationContext();
        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        topbar.addButtonLeftMost(this, "Logout");

        ToggleButton btn = (ToggleButton) findViewById(R.id.topbar_button);
        btn.setOnClickListener(new LogoutClickListener(app));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("IN THE METHOD");
        if (keyCode == KeyEvent.KEYCODE_BACK && this.popup != null) {
            ((LinearLayout) this.popup).findViewById(R.id.popup_shakable).startAnimation(new ShakeAnimation());
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private class ShakeAnimation extends Animation {

        public ShakeAnimation() {
            setDuration(500);
        }

        public void applyTransformation(float interpolatedTime, Transformation t) {
            t.getMatrix().reset();
            t.getMatrix().postTranslate((float) Math.random() * 6 - 3, (float) Math.random() * 6 - 3);
        }
    }

    private class LogoutClickListener implements OnClickListener {

        private App app;

        public LogoutClickListener(App app) {
            this.app = app;
        }

        @Override
        public void onClick(View v) {
            final ToggleButton btn = (ToggleButton) v;
            if (!SettingsView.this.toggled) {
                SettingsView.this.toggled = true;

                LayoutInflater inflater = (LayoutInflater) SettingsView.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                SettingsView.this.popup = inflater.inflate(R.layout.popup_window_logout,
                        (ViewGroup) findViewById(R.id.popup_logout));

                Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int width = (int) (display.getWidth() * 0.75);
                int height = (int) (display.getHeight() * 0.25);

                final PopupWindow pw = new PopupWindow(popup, width, height);

                final TextView txt = (TextView) popup.findViewById(R.id.poput_txt);
                txt.setText(Html.fromHtml("<big><b>Are You sure you want to abandon ship?</b></big>"));

                final Button button1 = (Button) popup.findViewById(R.id.popup_menu_button1);
                button1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View vv) {
                        LogoutClickListener.this.app.logout();
                        startActivity(new Intent(SettingsView.this, LoginView.class));
                    }
                });

                final Button button2 = (Button) popup.findViewById(R.id.popup_menu_button2);
                button2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View vv) {
                        // close the popup
                        btn.toggle();
                        pw.dismiss();
                        SettingsView.this.toggled = false;
                        SettingsView.this.popup = null;
                    }
                });

                // finally show the popup in the center of the window
                pw.showAtLocation(popup, Gravity.CENTER, 0, 0);

            } else {
                btn.toggle();
            }
        }
    }
}
