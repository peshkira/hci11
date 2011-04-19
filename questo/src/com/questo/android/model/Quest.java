package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Quest {
	enum Completion {
		INITIALIZED, COMPLETED
	}

	public static final String ACTIVE_FIELD_NAME = "active";

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField
	private Integer answeredCorrectlyCount;
	@DatabaseField
	private Completion completed;
	@DatabaseField
	private Date completedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public Quest() {
	}

	public Quest(String uuid) {
		super();
		this.uuid = uuid;
	}

	public Integer getAnsweredCorrectlyCount() {
		return answeredCorrectlyCount;
	}

	public void setAnsweredCorrectlyCount(Integer answeredCorrectlyCount) {
		this.answeredCorrectlyCount = answeredCorrectlyCount;
	}

	public Completion getCompleted() {
		return completed;
	}

	public void setCompleted(Completion completed) {
		this.completed = completed;
	}

	public Date getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		Quest other = (Quest) o;
		return other.id.equals(this.id);
	}

}