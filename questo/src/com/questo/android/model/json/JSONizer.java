package com.questo.android.model.json;

import com.google.gson.Gson;

public class JSONizer {
	private static Gson gson;
	
	private static Gson gson() {
		if(gson == null)
			gson = new Gson();
		return gson;
	}
	
	public static String toJSON(Object o) {
		return gson().toJson(o);
	}
	
	public static <T> T toObject(String json, Class<T> clazz) {
		return gson().fromJson(json, clazz);
	}
}
