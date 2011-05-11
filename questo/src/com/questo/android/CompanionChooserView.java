package com.questo.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.questo.android.model.User;

public class CompanionChooserView extends Activity {

	public static final String EXTRA_COMPANION_UUID_ARRAY = "EXTRA_COMPANION_UUID_ARRAY";

	CompanionsListAdapter adapter;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.companion_chooser);

		App app = (App) this.getApplicationContext();
		ModelManager mngr = app.getModelManager();
		List<User> companions = mngr.getCompanionsForUser(app.getLoggedinUser());
		String[] preselectedUUIDs = getIntent().getExtras().getStringArray(EXTRA_COMPANION_UUID_ARRAY);
		List<User> preselectedUsers = new ArrayList<User>();
		if (preselectedUUIDs != null) {
			for (String userUUID : preselectedUUIDs) {
				preselectedUsers.add(app.getModelManager().getGenericObjectByUuid(userUUID, User.class));
			}
		}

		adapter = new CompanionsListAdapter(companions, preselectedUsers);

		ListView companionsList = (ListView) findViewById(R.id.list_companions);
		companionsList.setEmptyView(findViewById(R.id.empty_companions_text));
		companionsList.setAdapter(adapter);
		companionsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				User user = (User) adapter.getItem(position);
				CheckBox checkbox = (CheckBox) v.findViewById(R.id.companion_cb);
				if (checkbox.isChecked()) {
					adapter.removeSelection(user);
					checkbox.setChecked(false);
				} else {
					adapter.addSelection(user);
					checkbox.setChecked(true);
				}
			}
		});

		Button selectBtn = (Button) findViewById(R.id.select);
		selectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				writeParticipantsForResult();
				finish();
			}
		});
	}

	private void writeParticipantsForResult() {
		List<String> participants = new ArrayList<String>();
		for (User participant : adapter.getSelected())
			participants.add(participant.getUuid());

		Intent i = new Intent();
		i.putExtra(EXTRA_COMPANION_UUID_ARRAY, participants.toArray(new String[0]));
		setResult(RESULT_OK, i);
	}

	private class CompanionsListAdapter extends BaseAdapter {

		private List<User> selected;
		private List<User> data;
		private LayoutInflater inflater;

		public CompanionsListAdapter(List<User> users, List<User> selectedUsers) {
			this.data = users;
			this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.selected = selectedUsers;
			if (this.selected == null)
				this.selected = new ArrayList<User>();
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

		public void addSelection(User user) {
			selected.add(user);
		}

		public void removeSelection(User user) {
			selected.remove(user);
		}

		public List<User> getSelected() {
			return selected;
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

			CheckBox box = (CheckBox) view.findViewById(R.id.companion_cb);
			box.setVisibility(CheckBox.VISIBLE);
			box.setChecked(selected.contains(user));
			box.setClickable(false);
			return view;
		}

	}
}
