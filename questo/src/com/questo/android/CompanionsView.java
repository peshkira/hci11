package com.questo.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.model.User;
import com.questo.android.view.TopBar;

public class CompanionsView extends Activity {

    private App app;
    private ModelManager mngr;
    private CompanionsListAdapter adapter;
    private CompanionItemListener itemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companions);

        app = (App) this.getApplicationContext();
        mngr = app.getModelManager();
        List<User> companions = mngr.getCompanionsForUser(app.getLoggedinUser());
        System.out.println("SIZE: " + companions.size());
        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        Button requestButton = topbar.addImageToggleButtonLeftMost(this, R.drawable.img_request, false);
        requestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tournamentRequest = new Intent(CompanionsView.this, CompanionRequestsView.class);
                startActivity(tournamentRequest);
            }
        });

        adapter = new CompanionsListAdapter(companions);
        itemListener = new CompanionItemListener(companions);

        ListView companionsList = (ListView) findViewById(R.id.list_companion);
        companionsList.setEmptyView(findViewById(R.id.empty_companions_text));
        companionsList.setAdapter(adapter);
        companionsList.setOnItemClickListener(itemListener);

        EditText searchbox = (EditText) findViewById(R.id.search_box);
        searchbox.addTextChangedListener(new SearchBoxTextWatcher(adapter));

        Button btnAdd = (Button) findViewById(R.id.btn_add_companion);
        btnAdd.setOnClickListener(new AddCompanionListener());

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<User> list = mngr.getCompanionsForUser(app.getLoggedinUser());
        adapter.setData(list);
        itemListener.setCompanions(list);
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeView.class));
    }
    

    private class AddCompanionListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(CompanionsView.this, AddCompanionView.class));
            overridePendingTransition(R.anim.push_up_in, R.anim.no_change_out);
        }

    }

    private class CompanionItemListener implements OnItemClickListener {

        private List<User> companions;

        public CompanionItemListener(List<User> companions) {
            this.companions = companions;
        }
        
        public void setCompanions(List<User> companions) {
            this.companions = companions;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent showCompanion = new Intent(CompanionsView.this, ProfileView.class).putExtra(
                    Constants.TRANSITION_OBJECT_UUID, this.companions.get(position).getUuid()).putExtra(
                    Constants.PROFILE_BTN_TYPE, Constants.PROFILE_BTN_TYPES[2]);
            startActivityForResult(showCompanion, RESULT_CANCELED);

        }
    }

    private class SearchBoxTextWatcher implements TextWatcher {

        private CompanionsListAdapter adapter;

        public SearchBoxTextWatcher(CompanionsListAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.filter(s);
        }

    }

    private class CompanionsListAdapter extends BaseAdapter {

        private List<User> companions;
        private List<User> data;
        private LayoutInflater inflater;

        public CompanionsListAdapter(List<User> users) {
            this.companions = new ArrayList<User>();
            this.data = users;
            this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        public void setData(List<User> users) {
            this.data = users;
            this.companions.clear();
            notifyDataSetChanged();
        }

        public void filter(CharSequence s) {
            this.data.addAll(this.companions);
            this.companions.clear();
            Iterator<User> iter = this.data.iterator();

            while (iter.hasNext()) {
                User next = iter.next();
                if (!next.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                    this.companions.add(next);
                    iter.remove();
                }
            }

            if (data.isEmpty()) {
                TextView tv = (TextView) findViewById(R.id.empty_companions_text);
                tv.setText("You have no companion with that name!");
            }

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.data.size();
        }

        @Override
        public Object getItem(int pos) {
            return this.data.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            User user = this.data.get(position);
            LinearLayout view = (LinearLayout) this.inflater.inflate(R.layout.companion_listitem, null, false);

            ImageView image = (ImageView) view.findViewById(R.id.companion_img);
            // image.setImageResource(trophy.getImgUrl);

            TextView name = (TextView) view.findViewById(R.id.companion_name);
            name.setText(user.getName());
            // view.setOnClickListener(new UserClickListener(user));
            return view;
        }

    }
}
