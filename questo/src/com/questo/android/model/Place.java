package com.questo.android.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.LazyForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Place {

	public static final String UUID = "UUID";
	
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField
	private Double latitude;
	@DatabaseField
	private Double longitude;
	@DatabaseField
	private String name;
	@ForeignCollectionField(eager = false)
	ForeignCollection<Question> questions;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public Place() {
	}

	public Place(String uuid, String name) {
		super();
		this.uuid = uuid;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets all connected Questions. This should not be used to add new
	 * questions - rather change the Question and set this Place on it.
	 * @return
	 */
	public ForeignCollection<Question> getQuestions() {
		return questions;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		Place other = (Place) o;
		return other.id.equals(this.id);
	}

}