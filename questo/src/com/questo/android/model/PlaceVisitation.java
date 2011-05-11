package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class PlaceVisitation {
	
	public static final String UUID = "UUID";

    public static final String PLACE_UUID = "PLACE_UUID";

    public static final String USER = "USER";

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField(columnName=USER, foreign = true)
	private User user;
	@DatabaseField(columnName=PLACE_UUID)
	private String place;
	@DatabaseField
	private Date visitedAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public PlaceVisitation() {
	}

	public PlaceVisitation(String uuid, User user, String place, Date visitedAt) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.place = place;
		this.visitedAt = visitedAt;
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

	public String getPlace() {
		return place;
	}

	public Date getVisitedAt() {
		return visitedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		PlaceVisitation other = (PlaceVisitation) o;
		return other.id.equals(this.id);
	}

}