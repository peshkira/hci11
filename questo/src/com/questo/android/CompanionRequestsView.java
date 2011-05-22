package com.questo.android;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Companionship;
import com.questo.android.model.Notification;
import com.questo.android.model.User;
import com.questo.android.view.TopBar;

public class CompanionRequestsView extends Activity {

    private App app;
    private CompanionRequestListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companion_requests);

        this.init();
    }

    private void init() {
        this.app = (App) getApplicationContext();
        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        Button requestButton = topbar.addImageToggleButtonLeftMost(this, R.drawable.img_request, true);
        requestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompanionRequestsView.this, CompanionsView.class));
            }
        });

        ListView list = (ListView) findViewById(R.id.requestlist);
        list.setEmptyView(findViewById(R.id.empty_companionrequestlist_text));
        adapter = new CompanionRequestListAdapter(this, R.layout.requests_list_item);
        list.setAdapter(adapter);
        loadRequests();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadRequests();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadRequests() {
        adapter.clear();
        List<Companionship> requests = app.getModelManager().getCompanionshipRequestsForUser(app.getLoggedinUser());
        for (Companionship cs : requests) {
            adapter.add(cs);
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, CompanionsView.class));
    }

    private class CompanionRequestListAdapter extends ArrayAdapter<Companionship> {

        public CompanionRequestListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) CompanionRequestsView.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.requests_list_item, null);
            }
            final Companionship current = this.getItem(position);
            TextView reqDescription = (TextView) v.findViewById(R.id.request_description);

            final User requestor = app.getModelManager().getGenericObjectByUuid(current.getInitiator(), User.class);
            String requestorName = requestor.getName() == null ? "Someone" : requestor.getName();

            reqDescription.setText(Html.fromHtml("<b>" + requestorName + "</b> wants to be your companion."));

            Button acceptBtn = (Button) v.findViewById(R.id.accept);
            acceptBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    current.setConfirmed(true);
                    current.setConfirmedAt(new Date());
                    app.getModelManager().update(current, Companionship.class);
                    String text = "<b>" + app.getLoggedinUser().getName() + "</b> is your companion now.";
                    Notification notification = new Notification(UUIDgen.getUUID(), Notification.Type.COMPANIONSHIP_ACCEPTED, text, requestor, null, new Date());
                    app.getModelManager().create(notification, Notification.class);
                    loadRequests();
                }
            });

            Button rejectBtn = (Button) v.findViewById(R.id.reject);
            rejectBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.getModelManager().delete(current, Companionship.class);
                    loadRequests();
                }
            });

            Button detailsBtn = (Button) v.findViewById(R.id.details);
            detailsBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CompanionRequestsView.this, ProfileView.class);
                    intent.putExtra(Constants.TRANSITION_OBJECT_UUID, requestor.getUuid()).putExtra(
                            Constants.PROFILE_BTN_TYPE, Constants.PROFILE_BTN_TYPES[3]);
                    startActivityForResult(intent, RESULT_CANCELED);
                }
            });

            return v;
        }

    }
}
