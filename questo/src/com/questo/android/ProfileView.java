package com.questo.android;

import java.util.Date;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
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
import com.questo.android.common.Level;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Companionship;
import com.questo.android.model.Notification;
import com.questo.android.model.User;
import com.questo.android.view.ProfileTabPlaces;
import com.questo.android.view.ProfileTabThrophies;
import com.questo.android.view.TopBar;

public class ProfileView extends TabActivity {

    private View popup = null;
    private TabHost tabHost;
    private App app;
    private ModelManager mngr;
    private Button btnAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        this.initView(this.getIntent().getExtras());
    }

    private void initView(Bundle extras) {
        User user;
        String text;
        app = (App) getApplicationContext();
        mngr = app.getModelManager();

        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        TextView state = (TextView) findViewById(R.id.UserProfileStateText);

        if (extras != null) {
            String userUuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
            user = mngr.getGenericObjectByUuid(userUuid, User.class);
            topbar.setLabel(user.getName());
            Integer points = mngr.getPointsForUser(user);
            text = user.getName() + " is a <b>" + Level.getHumanReadableCode(points) + "</b><br/>Points earned: " + points;  

            String type = extras.getString(Constants.PROFILE_BTN_TYPE);
            this.switchButtonTo(type, user);
            
        } else {
            user = app.getLoggedinUser();
            topbar.setLabel("You");
            Integer points = mngr.getPointsForUser(user);
            text = "You are a <b>" + Level.getHumanReadableCode(points) + "</b><br/>Points earned: " + points;
            Button btn = (Button) topbar.addButtonLeftMost(this, "Logout");
            btn.setOnClickListener(new LogoutClickListener(app));

        }
        
        state.setText(Html.fromHtml(text));

        this.tabHost = getTabHost();
        TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, ProfileTabThrophies.class).putExtra(Constants.TRANSITION_OBJECT_UUID,
                user.getUuid());
        spec = this.tabHost.newTabSpec("ProfileTabThrophies");
        spec.setIndicator("Throphy", getResources().getDrawable(R.drawable.img_trophy_thumb));
        spec.setContent(intent);
        this.tabHost.addTab(spec);

        intent = new Intent().setClass(this, ProfileTabPlaces.class).putExtra(Constants.TRANSITION_OBJECT_UUID,
                user.getUuid());
        spec = this.tabHost.newTabSpec("ProfileTabPlaces");
        spec.setIndicator("Places", getResources().getDrawable(R.drawable.img_questo_sign_thumb));
        spec.setContent(intent);
        this.tabHost.addTab(spec);

        this.tabHost.setCurrentTab(0);
        this.tabHost.setFocusable(false);
    }

    @Override
    public void onBackPressed() {
        if (this.popup != null) {
            ((LinearLayout) this.popup).findViewById(R.id.popup_shakable).startAnimation(new ShakeAnimation());
        } else {
            super.onBackPressed();
        }
    }
    
    private class ProfileButtonActionListener implements OnClickListener {

        private String action;
        
        private User confirmer;

        public ProfileButtonActionListener(String action, User user) {
            this.action = action;
            this.confirmer = user;
        }
        
        @Override
        public void onClick(View v) {
            //send request
            if (this.action.equals(Constants.PROFILE_BTN_TYPES[0])) {
                String text = "<b>" + app.getLoggedinUser().getName() + "</b> wants to be your companion.";
                Notification notification = new Notification(UUIDgen.getUUID(), Notification.Type.COMPANIONSHIP_REQUEST, text, confirmer, null, new Date());

                Companionship companionship = new Companionship(UUIDgen.getUUID(), app.getLoggedinUser().getUuid(), confirmer.getUuid(), new Date());
                companionship.setConfirmed(false);
                
                mngr.create(notification, Notification.class);
                mngr.create(companionship, Companionship.class);
                ProfileView.this.switchButtonTo(Constants.PROFILE_BTN_TYPES[1], confirmer);
                
                //cancel request
            } else if (this.action.equals(Constants.PROFILE_BTN_TYPES[1])) {
                Companionship companionship = mngr.getCompanionshipFor(app.getLoggedinUser(), confirmer);
                if (companionship != null) {
                    mngr.delete(companionship, Companionship.class);
                } else {
                    System.out.println("SOMETHING MUST HAVE GONE WRONG");
                }
                ProfileView.this.switchButtonTo(Constants.PROFILE_BTN_TYPES[0], confirmer);
                //cancel companionship
            } else if (this.action.equals(Constants.PROFILE_BTN_TYPES[2])) {
                Companionship c1 = mngr.getCompanionshipFor(app.getLoggedinUser(), confirmer);
                Companionship c2 = mngr.getCompanionshipFor(confirmer, app.getLoggedinUser());
                
                if (c1 != null) {
                    System.out.println("REMOVING COMPANIONSHIP");
                    mngr.delete(c1, Companionship.class);
                   
                } else if (c2 != null) {
                    System.out.println("REMOVING COMPANIONSHIP");
                    mngr.delete(c2, Companionship.class);
                    
                } else {
                    System.out.println("SOMETHING MUST HAVE GONE WRONG");
                }
                
                ProfileView.this.switchButtonTo(Constants.PROFILE_BTN_TYPES[0], confirmer);
                
                //accept companionship
            } else {
                String text = "<b>" + app.getLoggedinUser().getName() + "</b> is your companion now.";
                Notification notification = new Notification(UUIDgen.getUUID(), Notification.Type.COMPANIONSHIP_ACCEPTED, text, confirmer, null, new Date());
                
                Companionship companionship = mngr.getCompanionshipFor(confirmer, app.getLoggedinUser());
                companionship.setConfirmed(true);
                companionship.setConfirmedAt(new Date());
                
                mngr.update(companionship, Companionship.class);
                mngr.create(notification, Notification.class);
                ProfileView.this.switchButtonTo(Constants.PROFILE_BTN_TYPES[2], confirmer);
            }
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
            tabHost.setVisibility(View.GONE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            LayoutInflater inflater = (LayoutInflater) ProfileView.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ProfileView.this.popup = inflater.inflate(R.layout.popup_window_logout,
                    (ViewGroup) findViewById(R.id.popup_logout));

            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int width = (int) (display.getWidth() * 0.80);
            int height = (int) (display.getHeight() * 0.30);

            final PopupWindow pw = new PopupWindow(popup, width, height);

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
                    tabHost.setVisibility(View.VISIBLE);

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

    public void switchButtonTo(String type, User confirmer) {
        Button btn;
        if (type.equals(Constants.PROFILE_BTN_TYPES[0])) { // request
            btn = (Button) findViewById(R.id.btn_send_request);

            // cancel request
        } else if (type.equals(Constants.PROFILE_BTN_TYPES[1])) {
            btn = (Button) findViewById(R.id.btn_cancel_request);
            
            //cancel companionship
        } else if (type.equals(Constants.PROFILE_BTN_TYPES[2])){
            btn = (Button) findViewById(R.id.btn_cancel_companionship);
            
            //accept companionship
        } else {
            btn = (Button) findViewById(R.id.btn_accept_companionship);
        }

        if (btnAction != null) {
            btnAction.setVisibility(View.GONE);
        }
        
        btnAction = btn;
        btnAction.setVisibility(View.VISIBLE);
        btnAction.setOnClickListener(new ProfileButtonActionListener(type, confirmer));
    }

}
