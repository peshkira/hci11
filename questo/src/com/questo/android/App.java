package com.questo.android;

import java.sql.SQLException;
import java.util.List;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.questo.android.model.Quest;

public class App extends Application {

	private ModelManager modelManager;

	// DEBUG ONLY, DELETE:
	PendingIntent pi;

	public DBHelper db() {
		return (DBHelper) OpenHelperManager.getHelper(getApplicationContext());
	}

	public ModelManager modelManager() {
		if (modelManager == null)
			modelManager = new ModelManager(this);
		return modelManager;
	}

	@Override
	public void onCreate() {
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DBHelper(context);
			}
		});
		
		try {
			List<Quest> all = db().daoQuest().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.onCreate();
		modelManager();
		// modelManager.createTestData();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
