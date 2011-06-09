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
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.helper.QuestoFieldFocusListener;
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
        App app = (App) this.getApplicationContext();
        ModelManager mngr = app.getModelManager();

        TopBar topbar = (TopBar) findViewById(R.id.topbar);
        Button btnInvite = topbar.addImageButtonLeftMost(this, R.drawable.img_plus);
        btnInvite.setOnClickListener(new InviteListener());

        // TODO query non companions...
        List<User> noncompanions = mngr.getNonCompanionsForUser(app.getLoggedinUser());
        NonCompanionsListAdapter adapter = new NonCompanionsListAdapter(noncompanions);

        TextView empty = (TextView) findViewById(R.id.empty_users_text);
        empty.setOnTouchListener(new QuestoFieldFocusListener((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)));
        
        ListView results = (ListView) findViewById(R.id.list_users);
        results.setEmptyView(empty);
        results.setAdapter(adapter);
        results.setOnTouchListener(new QuestoFieldFocusListener((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)));

        EditText searchBox = (EditText) findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new SearchBoxTextWatcher(adapter));

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_change_out, R.anim.push_down_in);
    }

    private class InviteListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(AddCompanionView.this, InviteView.class));
        }

    }
    
    private class AddUserItemListener implements OnClickListener {

        private User user;

        public AddUserItemListener(User user) {
            this.user = user;
        }
        
        @Override
        public void onClick(View arg0) {
            Intent showCompanion = new Intent(AddCompanionView.this, ProfileView.class).putExtra(
                    Constants.TRANSITION_OBJECT_UUID, user.getUuid()).putExtra(Constants.PROFILE_BTN_TYPE, Constants.PROFILE_BTN_TYPES[0]);
            startActivity(showCompanion);
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
            this.noncompanions = users;
            this.data = new ArrayList<User>();
            this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void filter(CharSequence s) {
            this.data.clear();

            if (!s.toString().equals("")) {
                Iterator<User> iter = this.noncompanions.iterator();

                while (iter.hasNext()) {
                    User next = iter.next();
                    if (next.getName().toLowerCase().contains(s.toString().toLowerCase())
                            || next.getEmail().toLowerCase().equals(s.toString().toLowerCase())) {
                        this.data.add(next);
                    }
                }

                if (data.isEmpty()) {
                    ((TextView) findViewById(R.id.empty_users_text))
                            .setText("No user by that name! Click on the '+' sign to send out an invite");
                }
            } else {
                ((TextView) findViewById(R.id.empty_users_text))
                        .setText("You can search for new companions by typing in the box above.");
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
            view.setOnClickListener(new AddUserItemListener(user));

            ImageView image = (ImageView) view.findViewById(R.id.companion_img);
            // image.setImageResource(trophy.getImgUrl);

            TextView name = (TextView) view.findViewById(R.id.companion_name);
            name.setText(user.getName());
            // view.setOnClickListener(new UserClickListener(user));
            return view;
        }

    }

}
