package com.questo.android;

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

import com.questo.android.model.Companionship;
import com.questo.android.model.User;
import com.questo.android.view.TopBar;

public class CompanionRequestsView extends Activity {

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companion_requests);

        this.init();
    }

    private void init() {
        this.app = (App) getApplicationContext();
        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        Button requestButton = topbar.addToggleButtonLeftMost(this, "Requests", true);
        requestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompanionRequestsView.this, CompanionsView.class));
            }
        });

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(Html
                .fromHtml("Your herald has delivered to you the following <b>requests for companionship</b>:"));

        ListView list = (ListView) findViewById(R.id.requestlist);
        list.setEmptyView(findViewById(R.id.empty_companionrequestlist_text));
        CompanionRequestListAdapter adapter = new CompanionRequestListAdapter(this, R.layout.requests_list_item);
        list.setAdapter(adapter);
        this.loadRequests(adapter);
    }

    private void loadRequests(CompanionRequestListAdapter adapter) {
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

            User requestor = app.getModelManager().getGenericObjectByUuid(current.getInitiator(), User.class);
            String requestorName = requestor.getName() == null ? "Someone" : requestor.getName();

            reqDescription.setText(Html.fromHtml("<b>" + requestorName + "</b> wants to be your companion."));

            Button acceptBtn = (Button) v.findViewById(R.id.accept);
            acceptBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // app.getModelManager().acceptTournamentRequest(current);
                    // loadRequests();
                }
            });

            Button rejectBtn = (Button) v.findViewById(R.id.reject);
            rejectBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // app.getModelManager().delete(current,
                    // TournamentRequest.class);
                    // loadRequests();
                }
            });

            Button detailsBtn = (Button) v.findViewById(R.id.details);
            detailsBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(CompanionRequestsView.this,
                    // ProfileView.class);
                    // intent.putExtra(Constants.TRANSITION_OBJECT_UUID,
                    // tournament.getUuid());
                    // startActivity(intent);
                }
            });

            return v;
        }

    }
}
