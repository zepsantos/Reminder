package com.josepgrs.reminder.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josepgrs.reminder.Init.InitApp;
import com.josepgrs.reminder.R;

/**
 * Created by josep on 14/11/2016.
 */

public class Settings extends PreferenceFragment {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Preference Name;
    private Preference Email;
    private Preference Group;
    private Preference LogOut;
    private Preference Changepassword;
    private Preference UserName;
    private String mName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Name = getPreferenceManager().findPreference("Name");
        Email = getPreferenceManager().findPreference("Email");
        Group = getPreferenceManager().findPreference("Group");
        LogOut = getPreferenceManager().findPreference("LogOut");
        UserName = getPreferenceManager().findPreference("UserName");
        Changepassword = getPreferenceManager().findPreference("ChangePassword");
        onClickListeners();
        PasswordDialog();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void onClickListeners() {
        LogOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LogOutFunction();
                return false;
            }
        });
        Group.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });
    }

    private void PasswordDialog() {
        Changepassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
                builder.setTitle("Change Password:");
                final EditText input = new EditText(getActivity().getApplicationContext());
                input.layout(50, 10, 50, 10);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
                        builder.setTitle(R.string.ChangePassMessage);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ChangePasswordFunction(input);

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();


                    }
                });


                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return false;
            }
        });
    }

    private void ChangePasswordFunction(EditText pass) {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updatePassword(pass.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                LogOutFunction();
                            } else {

                                if (getView() != null) {
                                    Toast t = Toast.makeText(getActivity().getApplicationContext(), "Password Unsucessufuly changed", Toast.LENGTH_LONG);
                                    t.show();
                                }

                            }
                        }
                    });

        }
    }

    private void LogOutFunction() {

        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(getActivity(), InitApp.class));
                        getActivity().finish();
                    }
                });


    }


}
