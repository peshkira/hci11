package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TournamentMembership {
	
	public static final String UUID = "UUID";
	public static final String USER_UUID = "USER_UUID";
	public static final String TOURNAMENT_UUID = "TOURNAMENT_UUID";
	
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(columnName=TOURNAMENT_UUID)
	private String tournamentUUID;
	@DatabaseField(columnName=USER_UUID)
	private String userUUID;
	@DatabaseField
	private Date joinedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TournamentMembership() {
	}

	public TournamentMembership(String uuid, String userUUID, String tournamentUUID, Date joinedAt) {
		super();
		this.uuid = uuid;
		this.userUUID = userUUID;
		this.tournamentUUID = tournamentUUID;
		this.joinedAt = joinedAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}

	public String getUserUUID() {
		return userUUID;
	}

	public String getTournamentUUID() {
		return tournamentUUID;
	}

	public Date getJoinedAt() {
		return joinedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		TournamentMembership other = (TournamentMembership) o;
		return other.id.equals(this.id);
	}

}