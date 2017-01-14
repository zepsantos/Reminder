package com.josepgrs.reminder.Init;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josepgrs.reminder.Calendar.CalendarFragmentView;
import com.josepgrs.reminder.GetUserInformation;
import com.josepgrs.reminder.R;
import com.josepgrs.reminder.RecentContent.NewsView;
import com.josepgrs.reminder.School.SchoolView;
import com.josepgrs.reminder.Settings.Settings;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainView extends Activity {
    public static String uid;
    FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth mAuth;
    private BottomBar mBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        mAuth = FirebaseAuth.getInstance();
        BottomBarFunctions();
        AuthState();
    }

    @Override
    protected void onResume() {
        GetUserInformation.getInstance();
        super.onResume();
    }

    private void AuthState() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), InitApp.class));
                }
            }
        };
        mAuth.addAuthStateListener(authListener);
    }

    private void BottomBarFunctions() {

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment selectedFragment = null;
                switch (tabId) {
                    case R.id.News:
                        selectedFragment = new NewsView();
                        break;
                    case R.id.Calendar:
                        selectedFragment = new CalendarFragmentView();
                        break;
                    case R.id.School:
                        selectedFragment = new SchoolView();
                        break;
                    case R.id.Settings:
                        selectedFragment = new Settings();
                        break;

                }
                if (selectedFragment != null) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.mainContent, selectedFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.Settings:
                        getFragmentManager().beginTransaction()
                                .replace(R.id.mainContent, new Settings())
                                .addToBackStack(null)
                                .commit();
                        break;
                }
            }
        });
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
