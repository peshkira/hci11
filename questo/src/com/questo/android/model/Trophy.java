package com.questo.android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.questo.android.model.json.JSONKeyValueStore;
import com.questo.android.model.json.JSONizer;

@DatabaseTable
public class Trophy {

	public enum Type {
		GLOBAL, FOR_PLACE, FOR_QUEST;
	}

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField
	private String name;
	@DatabaseField
	private Type type;
	@DatabaseField
	private String jsonifiedExtras;

	private Extras extras;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public Trophy() {
	}

	public Trophy(String uuid, String name, Type type) {
		super();
		this.uuid = uuid;
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

	public Trophy.Extras getExtras() {
		if (extras == null) {
			if (jsonifiedExtras == null)
				extras = this.new Extras();
			else
				extras = JSONizer.toObject(jsonifiedExtras, Trophy.Extras.class);
		}
		return extras;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		Trophy other = (Trophy) o;
		return other.id.equals(this.id);
	}

	public class Extras extends JSONKeyValueStore {

		public static final String QUEST_UUID = "PLACE_UUID";
		//TODO Add your own values!

		@Override
		public void updateJSON() {
			Trophy.this.jsonifiedExtras = JSONizer.toJSON(this);
		}

	}

}