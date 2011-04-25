package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TournamentInvitation {

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField(foreign = true)
	private Tournament tournament;
	@DatabaseField(foreign = true)
	private User user;
	@DatabaseField
	private Date invitedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TournamentInvitation() {
	}

	public TournamentInvitation(String uuid, User user, Tournament tournament, Date invitedAt) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.tournament = tournament;
		this.invitedAt = invitedAt;
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

	public Date getInvitedAt() {
		return invitedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		TournamentInvitation other = (TournamentInvitation) o;
		return other.id.equals(this.id);
	}

}