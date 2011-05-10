package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TrophyForUser {

	public static final String UUID = "UUID";
	
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(foreign = true)
	private User user;
	@DatabaseField(foreign = true)
	private Trophy trophy;
	@DatabaseField
	private Date receivedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TrophyForUser() {
	}

	public TrophyForUser(String uuid, User user, Trophy trophy, Date receivedAt) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.trophy = trophy;
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

	public Trophy getTrophy() {
		return trophy;
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