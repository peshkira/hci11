package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TrophyForUser {

	public static final String UUID = "UUID";

    public static final String USER = "USER";

    public static final String TROPHY_UUID = "TROPHY_UUID";
	
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(columnName=USER, foreign = true)
	private User user;
	@DatabaseField(columnName=TROPHY_UUID)
	private String trophyUuid;
	@DatabaseField
	private Date receivedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TrophyForUser() {
	}

	public TrophyForUser(String uuid, User user, String trophy, Date receivedAt) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.trophyUuid = trophy;
		this.receivedAt = receivedAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}

	public String getTrophy() {
		return trophyUuid;
	}

	public Date getReceivedAt() {
		return receivedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		TrophyForUser other = (TrophyForUser) o;
		return other.id.equals(this.id);
	}

}