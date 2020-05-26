package ru.iamserj.instagramclone;

import android.app.Application;
import android.util.Log;
import com.parse.Parse;

/**
 * @author iamserj
 * 21.05.2020 2:15
 */

public class App extends Application {
	
	private static final String TAG = "MY_LOG_TAG";
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate: App");
		super.onCreate();
		
		Parse.initialize(new Parse.Configuration.Builder(this)
				.applicationId(getString(R.string.back4app_app_id))
				// if defined
				.clientKey(getString(R.string.back4app_client_key))
				.server(getString(R.string.back4app_server_url))
				.build()
		);
		
	}
}
