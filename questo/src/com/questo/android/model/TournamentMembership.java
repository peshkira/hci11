package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TournamentMembership {

	public static final String USER = "USER";
	public static final String TOURNAMENT = "TOURNAMENT";
	
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField(foreign = true, columnName=TOURNAMENT)
	private Tournament tournament;
	@DatabaseField(foreign = true, columnName=USER)
	private User user;
	@DatabaseField
	private Date joinedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TournamentMembership() {
	}

	public TournamentMembership(String uuid, User user, Tournament tournament, Date joinedAt) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.tournament = tournament;
		this.joinedAt = joinedAt;
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

	public Tournament getTournament() {
		return tournament;
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