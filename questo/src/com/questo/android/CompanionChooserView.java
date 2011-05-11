package com.questo.android;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.model.User;

public class CompanionChooserView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companion_chooser);

        App app = (App) this.getApplicationContext();
        ModelManager mngr = app.getModelManager();
        List<User> companions = mngr.getCompanionsForUser(app.getLoggedinUser());

        CompanionsListAdapter adapter = new CompanionsListAdapter(companions);

        ListView companionsList = (ListView) findViewById(R.id.list_companions);
        companionsList.setEmptyView(findViewById(R.id.empty_companions_text));
        companionsList.setAdapter(adapter);

    }

    private class CompanionsListAdapter extends BaseAdapter {

        private List<User> companions;
        private List<User> data;
        private LayoutInflater inflater;

        public CompanionsListAdapter(List<User> users) {
            this.companions = users;
            this.data = users;
            this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            
            CheckBox box = (CheckBox)view.findViewById(R.id.companion_cb);
            box.setVisibility(CheckBox.VISIBLE);
            
            return view;
        }

    }
}
