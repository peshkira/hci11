package com.questo.android;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.questo.android.common.Constants;
import com.questo.android.model.Place;
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

	public <T> T refresh(T o, Class<T> clazz) {
		try {
			int result = db().getCachedDao(clazz).refresh(o);
			if (result == 1)
				return o;
		} catch (SQLException e) {
			handleException(e);
		}
		return null;
	}

	public User getUserByEmail(String email) {
		try {
			QueryBuilder<User, Integer> userQBuilder = db().getCachedDao(
					User.class).queryBuilder();
			userQBuilder.where().eq(User.EMAIL, email);
			PreparedQuery<User> preparedQuery = userQBuilder.prepare();
			List<User> users = db().getCachedDao(User.class).query(
					preparedQuery);
			if (users.size() > 0)
				return users.get(0);
		} catch (SQLException e) {
			handleException(e);
		}
		return null;
	}

	public List<Place> getPlacesNearby(Double latitude, Double longitude) {
		double lowLatitude = latitude - Constants.MAP_PLACES_NEARBY;
		double highLatitude = latitude + Constants.MAP_PLACES_NEARBY;
		double lowLongitude = longitude - Constants.MAP_PLACES_NEARBY;
		double highLongitude = longitude + Constants.MAP_PLACES_NEARBY;

		try {
			QueryBuilder<Place, Integer> placeQBuilder = db().getCachedDao(
					Place.class).queryBuilder();
			placeQBuilder.where()
					.between("latitude", lowLatitude, highLatitude).and()
					.between("longitude", lowLongitude, highLongitude);
			PreparedQuery<Place> preparedQuery = placeQBuilder.prepare();
			List<Place> places = db().getCachedDao(Place.class).query(preparedQuery);
			return places;
		} catch (SQLException e) {
			handleException(e);
		}
		return null;
	}

	public Object getGenericObjectById(Integer id, Class clazz) {
		try {
			QueryBuilder<?, Integer> genericQBuilder = db().getCachedDao(clazz)
					.queryBuilder();
			genericQBuilder.where().eq("id", id);
			PreparedQuery<?> preparedQuery = genericQBuilder.prepare();
			List<?> objects = db().getCachedDao(clazz).query(preparedQuery);
			if (objects.size() > 0) {
				return objects.get(0);
			}
		} catch (SQLException e) {
			handleException(e);
		}

		return null;
	}

	public Object getGenericObjectByUuid(String uuid, Class clazz) {
		try {
			QueryBuilder<?, String> genericQBuilder = db().getCachedDao(clazz)
					.queryBuilder();
			genericQBuilder.where().eq("uuid", uuid);
			PreparedQuery<?> preparedQuery = genericQBuilder.prepare();
			List<?> objects = db().getCachedDao(clazz).query(preparedQuery);
			if (objects.size() > 0) {
				return objects.get(0);
			}
		} catch (SQLException e) {
			handleException(e);
		}

		return null;
	}

}
