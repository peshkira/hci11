package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TournamentTaskDone {
	
	public static final String UUID = "UUID";
	public static final String USER = "USER";
	public static final String TOURNAMENT_TASK_UUID = "TOURNAMENT_TASK_UUID";

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(columnName=TOURNAMENT_TASK_UUID)
	private String tournamentTaskUUID;
	@DatabaseField(foreign = true, columnName=USER)
	private User user;
	@DatabaseField
	private Date completedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TournamentTaskDone() {
	}

	public TournamentTaskDone(String uuid, User user, String tournamentTaskUUID, Date completedAt) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.tournamentTaskUUID = tournamentTaskUUID;
		this.completedAt = completedAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}

	public String getTournamentTaskUUID() {
		return tournamentTaskUUID;
	}

	public User getUser() {
		return user;
	}

	public Date getCompletedAt() {
		return completedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		TournamentTaskDone other = (TournamentTaskDone) o;
		return other.id.equals(this.id);
	}

}