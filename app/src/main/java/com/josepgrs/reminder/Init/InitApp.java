package com.josepgrs.reminder.Init;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josepgrs.reminder.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



public class InitApp extends Activity {
	private static final int RC_SIGN_IN = 567;
	private DatabaseReference mDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init_app);
		mDatabase = FirebaseDatabase.getInstance().getReference();
		FirebaseAuth auth = FirebaseAuth.getInstance();
		if (auth.getCurrentUser() != null) {
			AlreadySignedIn();
			// already signed in
		} else { // not signed in
			startActivityForResult(
					AuthUI.getInstance()
							.createSignInIntentBuilder()
							.setTheme(R.style.LoginTheme)
                            .setLogo(R.drawable.com_facebook_button_login_logo)
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
									new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
									new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
							.build(),
					RC_SIGN_IN);


		}


	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			FirebaseAuth auth = FirebaseAuth.getInstance();
			Map<String, Object> userinfo = new HashMap<>();
			userinfo.put("/users/" + auth.getCurrentUser().getUid() + "/Name/", auth.getCurrentUser().getDisplayName());
			userinfo.put("/users/" + auth.getCurrentUser().getUid() + "/email/", auth.getCurrentUser().getEmail());
			mDatabase.updateChildren(userinfo);
			AlreadySignedIn();
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

	private void AlreadySignedIn() {
		startActivity(new Intent(this, MainView.class));
		finish();
	}


}
