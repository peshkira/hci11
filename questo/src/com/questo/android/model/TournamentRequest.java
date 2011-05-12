package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TournamentRequest {
	
	public static final String UUID = "UUID";
	public static final String USER_UUID = "USER_UUID";
	public static final String REQUESTOR_UUID = "REQUESTOR_UUID";
	public static final String TOURNAMENT_UUID = "TOURNAMENT_UUID";

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(columnName=TOURNAMENT_UUID)
	private String tournamentUUID;
	@DatabaseField(columnName = USER_UUID)
	private String userUUID;
	@DatabaseField(columnName = REQUESTOR_UUID)
	private String requestorUUID;
	@DatabaseField
	private Date invitedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TournamentRequest() {
	}

	public TournamentRequest(String uuid, String userUUID, String requestorUUID, String tournamentUUID, Date invitedAt) {
		super();
		this.uuid = uuid;
		this.userUUID = userUUID;
		this.requestorUUID = requestorUUID;
		this.tournamentUUID = tournamentUUID;
		this.invitedAt = invitedAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}

	public String getRequestorUUID() {
		return requestorUUID;
	}

	public void setRequestorUUID(String requestorUUID) {
		this.requestorUUID = requestorUUID;
	}

	public String getUserUUID() {
		return userUUID;
	}

	public String getTournamentUUID() {
		return tournamentUUID;
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