package com.questo.android;

import java.sql.SQLException;

import android.content.Context;

import com.questo.android.model.Place;
import com.questo.android.model.Question;

public class ModelManager {

	private App app;
	private static final String TAG = "ModelManager";

	public ModelManager(Context ctx) {
		this.app = (App) ctx;
	}

	private DBHelper db() {
		return app.db();
	}

	private void handleException(Exception e) {
		e.printStackTrace();
	}

	public <T> T create(T o, Class<T> clazz) {
		try {
			int result = db().getCachedDao(clazz).create(o);
			if (result == 1)
				return o;
		} catch (SQLException e) {
			handleException(e);
		}
		return null;
	}

	public <T> T update(T o, Class<T> clazz) {
		try {
			int result = db().getCachedDao(clazz).update(o);
			if (result == 1)
				return o;
		} catch (SQLException e) {
			handleException(e);
		}
		return null;
	}

}
