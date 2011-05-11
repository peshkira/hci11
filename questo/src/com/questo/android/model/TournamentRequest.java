package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TournamentRequest {
	
	public static final String UUID = "UUID";
	public static final String USER = "USER";

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(foreign = true)
	private Tournament tournament;
	@DatabaseField(foreign = true, columnName = USER)
	private User user;
	@DatabaseField(foreign = true)
	private User requestor;
	@DatabaseField
	private Date invitedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TournamentRequest() {
	}

	public TournamentRequest(String uuid, User user, User requestor, Tournament tournament, Date invitedAt) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.requestor = requestor;
		this.tournament = tournament;
		this.invitedAt = invitedAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}

	public User getRequestor() {
		return requestor;
	}

	public void setRequestor(User requestor) {
		this.requestor = requestor;
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
		TournamentRequest other = (TournamentRequest) o;
		return other.id.equals(this.id);
	}

}