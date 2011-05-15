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

	public static final String EMAIL = "EMAIL";
	public static final String UUID = "UUID";
	//private static final String TAG = "ENTITY USER"; // tag for log

	public enum Gender {
		NONE, FEMALE, MALE
	}

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(columnName=EMAIL)
	private String email;
	@DatabaseField
	private String name;
	@DatabaseField
	private String passwordHash;
	@DatabaseField
	private String passwordSalt;
	@DatabaseField
	private Gender gender;
	@ForeignCollectionField(eager = false)
	ForeignCollection<Notification> notifications;
	@ForeignCollectionField(eager = false)
	ForeignCollection<PlaceVisitation> placeVisitations;
	@DatabaseField
	private Integer points;
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
		this.points = 0;
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
	
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
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

	public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getPoints() {
        return points;
    }
    
    public void incrementPointsBy(int incr) {
        this.points += incr;
    }

    @Override
    public boolean equals(Object o) {
        if (!this.getClass().equals(o.getClass()))
            return false;
        User other = (User) o;
        return other.id.equals(this.id);
    }
    
    public class Profile extends JSONKeyValueStore {
		
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