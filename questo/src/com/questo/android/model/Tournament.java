package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.questo.android.model.json.JSONKeyValueStore;
import com.questo.android.model.json.JSONizer;

@DatabaseTable
public class Tournament {

	enum Type {
		COOP, DEATHMATCH, ONE_ON_ONE, KING_OF_THE_HILL;
	}
	
	enum Visibility {
		PUBLIC, PRIVATE;
	}

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField
	private String name;
	@DatabaseField
	private Date createdAt;
	@DatabaseField
	private boolean started;
	@DatabaseField
	private Date startedAt;
	@DatabaseField
	private Type type;
	@ForeignCollectionField(eager = false)
	ForeignCollection<TournamentTask> tasks;
	@DatabaseField
	private String jsonifiedExtras;

	private Extras extras;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public Tournament() {
	}

	public Tournament(String uuid, Date createdAt, String name, Type type) {
		super();
		this.uuid = uuid;
		this.createdAt = createdAt;
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
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

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets all connected TournamentTasks. This should not be used to add new
	 * tasks - rather change the TournamentTask and set this Tournament on it.
	 * @return
	 */
	public ForeignCollection<TournamentTask> getTasks() {
		return tasks;
	}

	public Tournament.Extras getExtras() {
		if (extras == null) {
			if (jsonifiedExtras == null)
				extras = this.new Extras();
			else
				extras = JSONizer.toObject(jsonifiedExtras, Tournament.Extras.class);
		}
		return extras;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		Tournament other = (Tournament) o;
		return other.id.equals(this.id);
	}

	public class Extras extends JSONKeyValueStore {

		public static final String ONLY_FRIENDS_ALLOWED = "ONLY_FRIENDS_ALLOWED";
		//TODO Add your own values!

		@Override
		public void updateJSON() {
			Tournament.this.jsonifiedExtras = JSONizer.toJSON(this);
		}

	}

}