package com.questo.android;

import java.sql.SQLException;

import android.app.Application;
import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.questo.android.model.test.TestData;

public class App extends Application {

	private ModelManager modelManager;
	private Object dbHelperLock = new Object();
	private DBHelper dbHelper;

	public DBHelper db() {
		synchronized (dbHelperLock) {
			if (dbHelper == null)
				dbHelper = (DBHelper) OpenHelperManager.getHelper(getApplicationContext());
			return dbHelper;
		}
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

		TestData.generateTestData(modelManager());
		
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
