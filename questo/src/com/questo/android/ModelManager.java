package com.questo.android;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.questo.android.model.User;

public class ModelManager {

	private App app;
	private static final String TAG = "ModelManager";

	public ModelManager(Context ctx) {
		this.app = (App) ctx;
	}

	private DBHelper db() {
		return app.getDB();
	}

	private void handleException(SQLException e) {
		Log.e(TAG, "Problem with the database!");
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

	public User getUserByEmail(String email) {
		try {
			QueryBuilder<User, Integer> userQBuilder = db().getCachedDao(User.class).queryBuilder();
			userQBuilder.where().eq(User.EMAIL, email);
			PreparedQuery<User> preparedQuery = userQBuilder.prepare();
			List<User> users = db().getCachedDao(User.class).query(preparedQuery);
			if (users.size() > 0)
				return users.get(0);
		} catch (SQLException e) {
			handleException(e);
		}
		return null;
	}

}
