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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.model.User;
import com.questo.android.view.TopBar;

public class CompanionsView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companions);

        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        topbar.addButtonLeftMost(this, "Requests");

        CompanionsListAdapter adapter = new CompanionsListAdapter(new ArrayList<User>());
        
        ListView companionsList = (ListView) findViewById(R.id.list_companion);
        companionsList.setEmptyView(findViewById(R.id.empty_companions_text));
        companionsList.setAdapter(adapter);
        
        EditText searchbox = (EditText) findViewById(R.id.search_box);
        searchbox.addTextChangedListener(new SearchBoxTextWatcher(adapter));
        
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
            //TODO
        }
        
    }

    private class CompanionsListAdapter extends BaseAdapter {

        private List<User> companions;
        private LayoutInflater inflater;

        public CompanionsListAdapter(List<User> users) {
            this.companions = users;
            this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        public void filterItems(List<User> users) {
            this.companions.clear();
            this.companions.addAll(users);
        }

        @Override
        public int getCount() {
            return this.companions.size();
        }

        @Override
        public Object getItem(int pos) {
            return this.companions.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            User user = this.companions.get(position);
            LinearLayout view = (LinearLayout) this.inflater.inflate(R.layout.companion_listitem, null, false);

            ImageView image = (ImageView) view.findViewById(R.id.companion_img);
            // image.setImageResource(trophy.getImgUrl);

            TextView name = (TextView) view.findViewById(R.id.companion_name);
            name.setText(user.getName());
            //view.setOnClickListener(new UserClickListener(user));
            return view;
        }

    }
}
