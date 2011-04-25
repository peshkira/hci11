package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.questo.android.model.json.JSONKeyValueStore;
import com.questo.android.model.json.JSONizer;

@DatabaseTable
public class Notification {

	enum Type {
		USER_CREATED_TOURNAMENT, TOURNAMENT_STARTED, USER_COMPLETED_TOURNAMENT, USER_COMPLETED_QUEST,
		USER_TOURNAMENT_RANKING, TOURNAMENT_STARTS_SOON, TOURNAMENT_ENDS_SOON, COMPANIONSHIP_REQUEST,
		TOURNAMENT_INVITATION, SYSTEM_EVENT;
	}

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField
	private Type type;
	@DatabaseField(foreign = true)
	private User user;
	@DatabaseField
	private String jsonifiedContent;
	@DatabaseField
	private Date createdAt;
	
	private Content content;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public Notification() {
	}

	public Notification(String uuid, Type type, User user, Content content, Date createdAt) {
		super();
		this.uuid = uuid;
		this.type = type;
		this.content = content;
		this.createdAt = createdAt;
		this.user = user;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	
	public User getUser() {
		return user;
	}

	public Notification.Content getContent() {
		if (content == null) {
			if (jsonifiedContent == null)
				content = this.new Content();
			else
				content = JSONizer.toObject(jsonifiedContent, Notification.Content.class);
		}
		return content;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		Notification other = (Notification) o;
		return other.id.equals(this.id);
	}

	public class Content extends JSONKeyValueStore {

		public static final String TOURNAMENT_UUID = "TOURNAMENT_UUID";
		//TODO Add your own values!

		@Override
		public void updateJSON() {
			Notification.this.jsonifiedContent = JSONizer.toJSON(this);
		}

	}

}