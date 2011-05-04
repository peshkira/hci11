package com.questo.android;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Notification;
import com.questo.android.model.Notification.Type;
import com.questo.android.view.HomeIcon;

public class HomeView extends Activity {

	private GridView iconsGrid;

	// Set displayed homescreen icons and their target activity class here:
	private static final Wrap[] icons = {
			new Wrap(R.drawable.imgstate_quests, R.string.homeicon_quests, QuestMapView.class),
			new Wrap(R.drawable.imgstate_tournaments, R.string.homeicon_tournaments, TournamentView.class),
			new Wrap(R.drawable.imgstate_profiles, R.string.homeicon_profile, ProfileView.class),
			new Wrap(R.drawable.imgstate_companions, R.string.homeicon_companions, null),
			new Wrap(R.drawable.imgstate_trophies, R.string.homeicon_trophies, null),
			new Wrap(R.drawable.imgstate_settings, R.string.homeicon_settings, null) };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		this.initViews();
	}

	private void initViews() {
		iconsGrid = (GridView) findViewById(R.id.homeGrid);
		iconsGrid.setAdapter(new ImageAdapter());

		ListView watchtower = (ListView) findViewById(R.id.watchtower);

		QuestoListAdapter adapt = new QuestoListAdapter();
		adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST,
				"<b>Cato</b> has completed quest <b>Chillhouse of Terror</b>.", null, null, new Date()));
		adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "<b>Nuno</b> completed a quest.",
				null, null, new Date()));
		adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST,
				"<b>Lolcat</b> completed a quest.", null, null, new Date()));
		adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST,
				"<b>Aragorn</b> completed a quest.", null, null, new Date()));
		adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST, "<b>Cato</b> completed a quest.",
				null, null, new Date()));
		adapt.addItem(new Notification(UUIDgen.getUUID(), Type.USER_COMPLETED_QUEST,
				"<b>Gandalf</b> completed a quest.", null, null, new Date()));
		watchtower.setAdapter(adapt);

	}

	private void navigate(Wrap to) {
		System.out.println("NAVIGATE!!! " + to);
		Class<? extends Activity> targetActivityClass = to.getTargetActivity();
		if (targetActivityClass != null) {
			startActivity(new Intent(this, targetActivityClass));
		}
	}

	@Override
	public void onBackPressed() {
		// Similar result as pressing home button, but app lives on:
		moveTaskToBack(true);
	}

	private class MenuOnTouchListener implements OnClickListener {

		private Wrap iconWrapper;

		public MenuOnTouchListener(Wrap iconWrapper) {
			this.iconWrapper = iconWrapper;
		}

		@Override
		public void onClick(View v) {
			HomeView.this.navigate(iconWrapper);
		}
	}

	private class QuestoListAdapter extends BaseAdapter {

		private ArrayList<Notification> mData = new ArrayList<Notification>();
		private LayoutInflater mInflater;

		public QuestoListAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void addItem(final Notification item) {
			mData.add(item);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public String getItem(int position) {
			return mData.get(position).getText();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.home_listitem, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView.findViewById(R.id.item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textView.setText(Html.fromHtml(this.getItem(position)));
			return convertView;
		}

	}

	public static class ViewHolder {
		public TextView textView;
	}

	private static class Wrap {
		Integer iconResource;
		Integer textResource;
		Class<? extends Activity> targetActivity;

		public Wrap(Integer iconResource, Integer textResource, Class<? extends Activity> targetActivity) {
			super();
			this.iconResource = iconResource;
			this.textResource = textResource;
			this.targetActivity = targetActivity;
		}

		public Integer getIconResource() {
			return iconResource;
		}

		public Integer getTextResource() {
			return textResource;
		}

		public Class<? extends Activity> getTargetActivity() {
			return targetActivity;
		}
	}

	private class ImageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return icons.length;
		}

		@Override
		public Object getItem(int pos) {
			return null;
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;

			if (convertView == null) {
				final LayoutInflater infl = (LayoutInflater) HomeView.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				RelativeLayout layout = (RelativeLayout) infl.inflate(R.layout.home_icon, null);
				HomeIcon icon = (HomeIcon) layout.findViewById(R.id.home_icon_image);
				icon.setBackgroundResource(icons[position].getIconResource());
				icon.setOnClickListener(new MenuOnTouchListener(icons[position]));
				TextView tv = (TextView) layout.findViewById(R.id.home_icon_text);
				tv.setText(getString(icons[position].getTextResource()));
				v = layout;
			} else {
				v = convertView;
			}

			return v;
		}
	}

}