package com.questo.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.questo.android.error.LoginError;
import com.questo.android.helper.Security;
import com.questo.android.model.User;

public class App extends Application {

	private ModelManager modelManager;
	private Object dbHelperLock = new Object();
	private DBHelper dbHelper;
	private Settings settings = new Settings();
	private User loggedinUser;

	private static final String TAG = "APP";

	@Override
	public void onCreate() {
		deleteDatabase();
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DBHelper(context);
			}
		});
		settings.initializeFromFilestore();
		//setCurrentUserForDebugging();
		super.onCreate();
	}

	public DBHelper getDB() {
		synchronized (dbHelperLock) {
			if (dbHelper == null)
				dbHelper = (DBHelper) OpenHelperManager.getHelper(getApplicationContext());
			return dbHelper;
		}
	}

	public ModelManager getModelManager() {
		if (modelManager == null)
			modelManager = new ModelManager(this);
		return modelManager;
	}

	// TODO: Remove if needed - deletes database on every app start (not
	// every main activity start!):
	private void deleteDatabase() {
//		File dbFile = new File("/data/data/com.questo.android/databases/questo.db");
//		if (dbFile.exists() && dbFile.canWrite())
//			dbFile.delete();
	}

	// TODO: delete this later - only for initial debugging & testing:
	private void setCurrentUserForDebugging() {
		settings.setEmail("questo@questo.me");
		settings.setPassword("questo");
	}

	public boolean tryAutomaticLogin() {
		if (settings.getEmail() != null && settings.getPassword() != null) {
			if (login(settings.getEmail(), settings.getPassword()) == null) {
				Log.i(TAG, "Automatic login successful!");
				return true;
			}
		}
		return false;
	}

	public User getLoggedinUser() {
		return loggedinUser; 
	}
	

	public LoginError login(String email, String password) {
		User user = getModelManager().getUserByEmail(email);
		if (user == null)
			return new LoginError(true, null);
		else {
			if (!Security.validatePasswordCorrect(password, user))
				return new LoginError(false, true);
			loggedinUser = user;
			settings.setEmail(email);
			settings.setPassword(password);
			return null;
		}
	}
	

	private class Settings extends Properties {
		private static final long serialVersionUID = -7551836747366493766L;

		public static final String EMAIL = "EMAIL";
		public static final String PASSWORD = "PASSWORD";

		@Override
		public synchronized Object put(Object key, Object value) {
			Object result = super.put(key, value);
			writeToFile();
			return result;
		}

		public void reset() {
			clear();
			writeToFile();
		}

		private void writeToFile() {
			try {
				FileOutputStream fos = openFileOutput(App.this.getString(R.string.settingsfile), Context.MODE_PRIVATE);
				settings.store(fos, "Questo Local Settings");
				fos.close();
			} catch (Exception e) {
				Log.e(TAG, "Serious error: Could not save to settings file!");
				e.printStackTrace();
			}
		}

		public void initializeFromFilestore() {
			try {
				FileInputStream is = openFileInput(getString(R.string.settingsfile));
				load(is);
			} catch (FileNotFoundException e1) {
				reset();
			} catch (IOException e) {
				e.printStackTrace();
				reset();
			}
		}

		public void setEmail(String email) {
			this.put(EMAIL, email);
		}

		public String getEmail() {
			return (String) this.get(EMAIL);
		}

		public void setPassword(String password) {
			this.put(PASSWORD, password);
		}

		public String getPassword() {
			return (String) this.get(PASSWORD);
		}

	}

}
