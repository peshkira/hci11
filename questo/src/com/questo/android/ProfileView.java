package com.questo.android;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.model.User;
import com.questo.android.view.ProfileTabPlaces;
import com.questo.android.view.ProfileTabThrophies;
import com.questo.android.view.TopBar;

public class ProfileView extends TabActivity {

    private View popup = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        this.initView(this.getIntent().getExtras());
    }

    private void initView(Bundle extras) {
        User user;
        App app = (App) getApplicationContext();
        ModelManager mngr = app.getModelManager();

        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        TextView state = (TextView) findViewById(R.id.UserProfileStateText);
        
        if (extras != null) {
            String userUuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
            user = mngr.getGenericObjectByUuid(userUuid, User.class);
            topbar.setLabel(user.getName());
            state.setText(Html.fromHtml(user.getName() + " is a Peasant<br/>Points earned: 12"));
        } else {
            user = app.getLoggedinUser();
            topbar.setLabel("You");
            state.setText(Html.fromHtml("You are a King<br/>Points earned: 2337"));
            Button btn = (Button) topbar.addButtonLeftMost(this, "Logout");
            btn.setOnClickListener(new LogoutClickListener(app));

        }

        TabHost tabHost = getTabHost();
        TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, ProfileTabThrophies.class).putExtra(Constants.TRANSITION_OBJECT_UUID,
                user.getUuid());
        spec = tabHost.newTabSpec("ProfileTabThrophies");
        spec.setIndicator("Throphy", getResources().getDrawable(R.drawable.img_trophy_thumb));
        spec.setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ProfileTabPlaces.class).putExtra(Constants.TRANSITION_OBJECT_UUID,
                user.getUuid());
        spec = tabHost.newTabSpec("ProfileTabPlaces");
        spec.setIndicator("Places", getResources().getDrawable(R.drawable.img_questo_sign_thumb));
        spec.setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

    @Override
    public void onBackPressed() {
    
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.popup != null) {
            System.out.println("NOT NULL");
            ((LinearLayout) this.popup).findViewById(R.id.popup_shakable).startAnimation(new ShakeAnimation());
            return true;
        } else {
            System.out.println("NULL");
            return super.onKeyDown(keyCode, event);
        }
        
    }

    private class LogoutClickListener implements OnClickListener {

        private App app;

        public LogoutClickListener(App app) {
            this.app = app;
        }

        @Override
        public void onClick(View v) {
            final Button btn = (Button) v;
            btn.setEnabled(false);

            LayoutInflater inflater = (LayoutInflater) ProfileView.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ProfileView.this.popup = inflater.inflate(R.layout.popup_window_logout,
                    (ViewGroup) findViewById(R.id.popup_logout));

            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int width = (int) (display.getWidth() * 0.80);
            int height = (int) (display.getHeight() * 0.30);

            final PopupWindow pw = new PopupWindow(popup, width, height);
            pw.setFocusable(false);
            

            final TextView txt = (TextView) popup.findViewById(R.id.poput_txt);
            txt.setText(Html.fromHtml("<big><b>Are You sure you want to abandon ship?</b></big>"));

            final Button button1 = (Button) popup.findViewById(R.id.popup_menu_button1);
            button1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View vv) {
                    LogoutClickListener.this.app.logout();
                    startActivity(new Intent(ProfileView.this, LoginView.class));
                }
            });

            final Button button2 = (Button) popup.findViewById(R.id.popup_menu_button2);
            button2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View vv) {
                    // close the popup
                    pw.dismiss();
                    ProfileView.this.popup = null;
                    btn.setEnabled(true);
                }
            });

            // finally show the popup in the center of the window
            pw.showAtLocation(popup, Gravity.CENTER, 0, 0);

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

}
