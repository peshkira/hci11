package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Companionship {

	public static final String UUID = "UUID";
	
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(foreign = true)
	private User initiator;
	@DatabaseField(foreign = true)
	private User confirmer;
	@DatabaseField
	private boolean confirmed;
	@DatabaseField
	private Date requestedAt;
	@DatabaseField
	private Date confirmedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public Companionship() {
	}

	public Companionship(String uuid, User initiator, User confirmer, Date requestedAt) {
		super();
		this.uuid = uuid;
		this.initiator = initiator;
		this.confirmer = confirmer;
		this.requestedAt = requestedAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Date getConfirmedAt() {
		return confirmedAt;
	}

	public void setConfirmedAt(Date confirmedAt) {
		this.confirmedAt = confirmedAt;
	}

	public User getInitiator() {
		return initiator;
	}

	public User getConfirmer() {
		return confirmer;
	}

	public Date getRequestedAt() {
		return requestedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		Companionship other = (Companionship) o;
		return other.id.equals(this.id);
	}

}