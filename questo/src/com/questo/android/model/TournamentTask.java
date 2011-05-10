package com.questo.android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TournamentTask {
	
	public final static String ID = "ID";
	public final static String UUID = "UUID";
	public final static String TOURNAMENT = "TOURNAMENT";

	@DatabaseField(generatedId = true, columnName=ID)
	private Integer id;
	@DatabaseField(columnName = UUID)
	private String uuid;
	@DatabaseField(foreign = true, columnName=TOURNAMENT)
	private Tournament tournament;
	@DatabaseField(foreign = true)
	private Place place;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public TournamentTask() {
	}

	public TournamentTask(String uuid, Tournament tournament, Place place) {
		super();
		this.uuid = uuid;
		this.tournament = tournament;
		this.place = place;
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
	
	public Tournament getTournament() {
		return tournament;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		TournamentTask other = (TournamentTask) o;
		return other.id.equals(this.id);
	}

}