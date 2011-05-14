package com.questo.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.model.User;
import com.questo.android.view.TopBar;

public class AddCompanionView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_companion);
        
        this.init();
    }

    private void init() {
        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        Button btnRecruit = topbar.addButtonLeftMost(this, "Recruit");
        btnRecruit.setOnClickListener(new RecruitListener());
        
        //TODO query non companions...
        
        NonCompanionsListAdapter adapter = new NonCompanionsListAdapter(new ArrayList<User>());
        
        ListView results = (ListView) findViewById(R.id.list_users);
        results.setEmptyView(findViewById(R.id.empty_users_text));
        results.setAdapter(adapter);
        
        EditText searchBox = (EditText) findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new SearchBoxTextWatcher(adapter));
        
        //No user by that name! Click on recruit to send out an invite
        
        
    }
    
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_change_out, R.anim.push_down_in);
    }
    
    private class RecruitListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // start activity
        }
        
    }
    
    private class SearchBoxTextWatcher implements TextWatcher {

        private NonCompanionsListAdapter adapter;

        public SearchBoxTextWatcher(NonCompanionsListAdapter adapter) {
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
    
    private class NonCompanionsListAdapter extends BaseAdapter {

        private List<User> noncompanions;
        private List<User> data;
        private LayoutInflater inflater;
        
        public NonCompanionsListAdapter(List<User> users) {
            this.noncompanions = new ArrayList<User>();
            this.data = users;
            this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        public void filter(CharSequence s) {
            
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
