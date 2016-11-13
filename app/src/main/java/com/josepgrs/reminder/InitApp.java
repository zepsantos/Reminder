package com.josepgrs.reminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class InitApp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init_app);

		FirebaseAuth auth = FirebaseAuth.getInstance();
		if (auth.getCurrentUser() != null) {
			// already signed in
		} else { // not signed in
			startActivityForResult(
					AuthUI.getInstance()
							.createSignInIntentBuilder()
							.setTheme(R.style.LoginTheme)
							.setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
									new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
									new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
							.build(),
					RC_SIGN_IN);


		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Log.d("RESULT_OK","RESULT_OK");
			return;
		}

		// Sign in canceled
		if (resultCode == RESULT_CANCELED) {
			Log.d("RESULT_OK","RESULT_CANCELED");
			return;
		}

		// No network
		if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
			Log.d("RESULT_OK","RESULT_NO_NETWORK");
			return;
		}

		// User is not signed in. Maybe just wait for the user to press
		// "sign in" again, or show a message.
	}

	@Override
	protected void onStop() {
		super.onStop();
		AuthUI.getInstance()
				.signOut(this)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					public void onComplete(@NonNull Task<Void> task) {
						// user is now signed out

					}
				});
	}
}
