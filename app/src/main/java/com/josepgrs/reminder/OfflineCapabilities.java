package com.josepgrs.reminder;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineCapabilities extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
	/* Enable disk persistence  */
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

	}
}