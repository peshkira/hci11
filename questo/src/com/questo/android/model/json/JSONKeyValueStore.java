package com.questo.android.model.json;

import java.util.HashMap;
import java.util.Map;

public abstract class JSONKeyValueStore {
	private Map<String, Object> store = new HashMap<String, Object>();
	
	abstract public void updateJSON();
	
	public JSONKeyValueStore() {
	}
	
	public JSONKeyValueStore(HashMap<String, Object> keyValuePairs) {
		this.store = keyValuePairs;
	}
	
	public Object get(String key) {
		return store.get(key);
	}
	
	public void put(String key, Object value) {
		store.put(key, value);
		updateJSON();
	}
	
	public void remove(String key) {
		store.remove(key);
		updateJSON();
	}
}
