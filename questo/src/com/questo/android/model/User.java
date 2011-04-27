package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.questo.android.model.json.JSONKeyValueStore;
import com.questo.android.model.json.JSONizer;

@DatabaseTable
public class User {

	//private static final String TAG = "ENTITY USER"; // tag for log

	public enum Gender {
		NONE, FEMALE, MALE
	}

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField
	private String email;
	@DatabaseField
	private String name;
	@DatabaseField
	private Gender gender;
	@ForeignCollectionField(eager = false)
	ForeignCollection<Notification> notifications;
	@ForeignCollectionField(eager = false)
	ForeignCollection<PlaceVisitation> placeVisitations;
	@DatabaseField
	private Date signedUpAt;
	@DatabaseField
	private String jsonifiedProfile;
	@DatabaseField
	private String jsonifiedSettings;

	private User.Profile profile;
	private User.Settings settings;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public User() {
	}

	public User(String uuid, Date signedUpAt) {
		super();
		this.uuid = uuid;
		this.signedUpAt = signedUpAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getSignedUpAt() {
		return signedUpAt;
	}

	public CloseableIterator<Notification> getNotifications() {
		return notifications.iterator();
	}
	
	public CloseableIterator<PlaceVisitation> getPlaceVisitations() {
		return placeVisitations.iterator();
	}
	
	public User.Profile getProfile() {
		if (profile == null) {
			if (jsonifiedProfile == null)
				profile = this.new Profile();
			else
				profile = JSONizer.toObject(jsonifiedProfile, User.Profile.class);
		}
		return profile;
	}
	
	public User.Settings getSettings() {
		if (settings == null) {
			if (jsonifiedSettings == null)
				settings = this.new Settings();
			else
				settings = JSONizer.toObject(jsonifiedSettings, User.Settings.class);
		}
		return settings;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		User other = (User) o;
		return other.id.equals(this.id);
	}

	public class Profile extends JSONKeyValueStore {
		
		public static final String PASSWORD_HASH = "PASSWORD_HASH";
		public static final String PROFILE_PIC = "PROFILE_PIC";
		//TODO Add your own values!

		@Override
		public void updateJSON() {
			User.this.jsonifiedProfile = JSONizer.toJSON(this);
		}
	}
	
	public class Settings extends JSONKeyValueStore {
		
		public static final String GPS_ENABLED = "GPS_ENABLED";
		//TODO Add your own values!

		@Override
		public void updateJSON() {
			User.this.jsonifiedSettings = JSONizer.toJSON(this);
		}
	}

}