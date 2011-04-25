package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TournamentTaskDone {

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField(foreign = true)
	private TournamentTask tournamentTask;
	@DatabaseField(foreign = true)
	private User user;
	@DatabaseField
	private Date completedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TournamentTaskDone() {
	}

	public TournamentTaskDone(String uuid, User user, TournamentTask tournamentTask, Date completedAt) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.tournamentTask = tournamentTask;
		this.completedAt = completedAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}

	public TournamentTask getTournamentTask() {
		return tournamentTask;
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