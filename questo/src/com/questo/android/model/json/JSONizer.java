package com.questo.android.model.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.Question;

public class JSONizer {
	private static Gson gson;
	
	private static Gson gson() {
		if(gson == null) {
			gson = new GsonBuilder()
			.registerTypeAdapter(Question.PossibleAnswers.class, new PossibleAnswersCreator())
			.registerTypeAdapter(Question.CorrectAnswer.class, new CorrectAnswerCreator())
			.registerTypeAdapter(PossibleAnswer.class, new PossibleAnswerCreator())
			.create();
		}
		return gson;
	}
	
	public static String toJSON(Object o) {
		return gson().toJson(o);
	}
	
	public static <T> T toObject(String json, Class<T> clazz) {
		return gson().fromJson(json, clazz);
	}
}
