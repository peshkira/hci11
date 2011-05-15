package com.questo.android;

import java.util.ArrayList;
import java.util.Collections;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.questo.android.helper.NotificationComparator;
import com.questo.android.model.Notification;
import com.questo.android.view.FlexibleImageView;
import com.questo.android.view.TopBar;

public class HomeView extends Activity {

	private GridView iconsGrid;

	// Set displayed homescreen icons and their target activity class here:
	private static final Wrap[] icons = {
			new Wrap(R.drawable.imgstate_quests, R.string.homeicon_quests, QuestMapView.class),
			new Wrap(R.drawable.imgstate_tournaments, R.string.homeicon_tournaments, TournamentsView.class),
			new Wrap(R.drawable.imgstate_profiles, R.string.homeicon_profile, ProfileView.class),
			new Wrap(R.drawable.imgstate_companions, R.string.homeicon_companions, CompanionsView.class),
			new Wrap(R.drawable.imgstate_trophies, R.string.homeicon_trophies, TrophyRoomView.class),
			new Wrap(R.drawable.imgstate_settings, R.string.homeicon_settings, SettingsView.class) };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		this.initViews();
	}

	private void initViews() {
	    App app = (App) this.getApplicationContext();
	    
		iconsGrid = (GridView) findViewById(R.id.homeGrid);
		iconsGrid.setAdapter(new ImageAdapter());

		TopBar topbar = (TopBar) findViewById(R.id.topbar);
		topbar.setSpecialFont(true, this);
		
		ListView watchtower = (ListView) findViewById(R.id.watchtower);
		TextView emptyWatchtower = (TextView) findViewById(R.id.txt_empty_watchtower);
		watchtower.setEmptyView(emptyWatchtower);

		List<Notification> notifications = app.getModelManager().getNotificationsForUser(app.getLoggedinUser());
		NotificationListAdapter adapt = new NotificationListAdapter(notifications);

		watchtower.setAdapter(adapt);
		watchtower.setOnItemClickListener(new NotificationItemClickListener(notifications));

	}

	private void navigate(Wrap to) {
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
	
	private class NotificationItemClickListener implements OnItemClickListener {

	    private List<Notification> notifications;

        public NotificationItemClickListener(List<Notification> notifications) {
	        this.notifications = notifications;
	    }
	    
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            this.showNotificationResult(this.notifications.get(pos));
        }
        
        private void showNotificationResult(Notification notification) {
            switch (notification.getType()) {
            case COMPANIONSHIP_REQUEST:
                startActivity(new Intent(HomeView.this, CompanionRequestsView.class));
                break;
            case COMPANIONSHIP_ACCEPTED:
                startActivity(new Intent(HomeView.this, CompanionsView.class));
                break;
                default: //do nothing
            }
        }
	    
	}

	private class NotificationListAdapter extends BaseAdapter {

		private List<Notification> notifications;
		private LayoutInflater mInflater;
		private NotificationComparator comparator;

		public NotificationListAdapter() {
		    super();
		    this.notifications = new ArrayList<Notification>();
			this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.comparator = new NotificationComparator();
		}
		
		public NotificationListAdapter(List<Notification> notifications) {
		    this();
		    this.notifications = notifications;
		}

		public void addItem(final Notification item) {
			this.notifications.add(item);
			Collections.sort(this.notifications, this.comparator);
			
			while (this.notifications.size() > 10) {
			    this.notifications.remove(this.notifications.size() - 1);
			}
			
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return notifications.size();
		}

		@Override
		public String getItem(int position) {
			return notifications.get(position).getText();
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
				FlexibleImageView icon = (FlexibleImageView) layout.findViewById(R.id.home_icon_image);
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