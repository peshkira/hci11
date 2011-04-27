package com.questo.android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ImageResource {

	enum LocationType {
		HTTP, FTP, SDCARD, ASSET;
	}

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField
	private String uri;
	@DatabaseField
	private LocationType locationType;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public ImageResource() {
	}

	public ImageResource(String uuid, LocationType locationType, String uri) {
		super();
		this.uuid = uuid;
		this.locationType = locationType;
		this.uri = uri;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}
	
	public String getUri() {
		return uri;
	}
	
	public LocationType getLocationType() {
		return locationType;
	}


	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		ImageResource other = (ImageResource) o;
		return other.id.equals(this.id);
	}

}