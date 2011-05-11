package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Quest {
	
	public static final String UUID = "UUID";
	
	public enum Completion {
		INITIALIZED, COMPLETED
	}

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(foreign = true)
	private Place place;
	@DatabaseField
	private Integer answeredCorrectlyCount;
	@DatabaseField
	private Completion completionState;
	@DatabaseField
	private Date completedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public Quest() {
	}

	public Quest(String uuid, Place place) {
		super();
		this.uuid = uuid;
		this.place = place;
	}

	public Integer getAnsweredCorrectlyCount() {
		return answeredCorrectlyCount;
	}

	public void setAnsweredCorrectlyCount(Integer answeredCorrectlyCount) {
		this.answeredCorrectlyCount = answeredCorrectlyCount;
	}

	public Completion getCompletionState() {
		return completionState;
	}

	public void setCompletionState(Completion completed) {
		this.completionState = completed;
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
	
	public Place getPlace() {
		return place;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		Quest other = (Quest) o;
		return other.id.equals(this.id);
	}

}