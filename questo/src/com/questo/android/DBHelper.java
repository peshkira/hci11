package com.questo.android;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.Callable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.questo.android.model.Companionship;
import com.questo.android.model.ImageResource;
import com.questo.android.model.Notification;
import com.questo.android.model.Place;
import com.questo.android.model.PlaceVisitation;
import com.questo.android.model.Quest;
import com.questo.android.model.QuestHasQuestion;
import com.questo.android.model.Question;
import com.questo.android.model.QuestionAnswered;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentRequest;
import com.questo.android.model.TournamentMembership;
import com.questo.android.model.TournamentTask;
import com.questo.android.model.TournamentTaskDone;
import com.questo.android.model.Trophy;
import com.questo.android.model.TrophyForUser;
import com.questo.android.model.User;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "questo.db";
	private static final int DATABASE_VERSION = 1;
	
	private HashMap<String, Dao<Object, Integer>> daos = new HashMap<String, Dao<Object,Integer>>();

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DBHelper.class.getName(), "onCreate");
			createTables(connectionSource);
		} catch (SQLException e) {
			Log.e(DBHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}
	
	private void createTables(ConnectionSource cs) throws SQLException {
		TableUtils.createTable(cs, Companionship.class);
		TableUtils.createTable(cs, ImageResource.class);
		TableUtils.createTable(cs, Notification.class);
		TableUtils.createTable(cs, Place.class);
		TableUtils.createTable(cs, PlaceVisitation.class);
		TableUtils.createTable(cs, QuestHasQuestion.class);
		TableUtils.createTable(cs, Question.class);
		TableUtils.createTable(cs, QuestionAnswered.class);
		TableUtils.createTable(cs, Tournament.class);
		TableUtils.createTable(cs, TournamentRequest.class);
		TableUtils.createTable(cs, TournamentMembership.class);
		TableUtils.createTable(cs, TournamentTask.class);
		TableUtils.createTable(cs, TournamentTaskDone.class);
		TableUtils.createTable(cs, Trophy.class);
		TableUtils.createTable(cs, TrophyForUser.class);
		TableUtils.createTable(cs, User.class);
		TableUtils.createTable(cs, Quest.class);	
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DBHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Quest.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DBHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Dao<T, Integer> getCachedDao(Class<T> clazz) throws SQLException {
		Dao<T, Integer> dao;
		if ((dao = (Dao<T, Integer>) daos.get(clazz.getSimpleName())) == null) {
			dao = BaseDaoImpl.createDao(getConnectionSource(), clazz);
			daos.put(clazz.getSimpleName(), (Dao<Object, Integer>) dao);
		}
		return dao;
	}
	
	public void executeInTransaction(Callable<Void> callable) throws SQLException {
		TransactionManager.callInTransaction(getConnectionSource(),callable);
	}
	
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		daos = null;
	}
}
