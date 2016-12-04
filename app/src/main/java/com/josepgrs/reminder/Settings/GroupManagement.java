package com.josepgrs.reminder.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josepgrs.reminder.GetUserInformation;
import com.josepgrs.reminder.R;

import java.util.HashMap;
import java.util.Map;

import static com.josepgrs.reminder.R.string.LeaveGroup;

/**
 * Created by josep on 24/11/2016.
 */

public class GroupManagement extends PreferenceFragment {
    GetUserInformation userInformation;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Preference group;
    private Preference invmemb; //ALSO IT IS A BUTTON FOR MEMBERS LIST
    private Preference leaveGroup;
    private PreferenceScreen preferenceScreen;
    private String userGroup;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.groupsettings);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = view.getContext();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userInformation = new GetUserInformation().getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preferenceScreen = getPreferenceScreen();
        mAuth = FirebaseAuth.getInstance();
        group = getPreferenceManager().findPreference("group");
        leaveGroup = getPreferenceManager().findPreference("leavegroup");
        invmemb = getPreferenceManager().findPreference("invmemb");
        checkIfHasGroup();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void checkIfHasGroup() {

        if (userInformation.getUserGroup() != null) {
            userGroup = userInformation.getUserGroup();
        }
        if (userGroup != null) {
            group.setSummary(userGroup);
            invmemb.setTitle(R.string.memberslist);
            LeaveGroupFunction();
            MembersListFragment();
        } else {
            invmemb.setTitle(R.string.InviteGroup);
            CreateGroup();// CRIAR GRUPO CASO NAO TENHA
            InvitesFragment();

        }

    }

    private void CreateGroup() {
        preferenceScreen.removePreference(leaveGroup); //EM CASO DE NAO TER GRUPO NAO DAR PARA DAR LEAVE
        group.setSummary(R.string.ClicktoCreateGroup); //SUMARY PARA STRING CRIAR NOVO GRUPO
        group.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Group Name:");


                final EditText input = new EditText(mContext);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input.layout(50, 10, 50, 10);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String mText = input.getText().toString();
                        final String upperString = mText.substring(0, 1).toUpperCase() + mText.substring(1);
                        Map<String, Object> childUpdates = new HashMap<>();
                        final String useruid = mAuth.getCurrentUser().getUid();
                        childUpdates.put("/groups/" + upperString + "/owner/", useruid);
                        childUpdates.put("/group-names/" + upperString, useruid);
                        childUpdates.put("/groups/" + upperString + "/members/" + useruid, userInformation.getName());
                        childUpdates.put("/groups/" + upperString + "/name/", upperString);


                        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        addUserToGroup(useruid, upperString);

                                    }
                                });
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        });
                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    private void groupRefresh() {
        userGroup = userInformation.getUserGroup();
        checkIfHasGroup();
    }


    private void addUserToGroup(String useruid, String upperString) {
        Map<String, Object> addusertogroup = new HashMap<>();
        addusertogroup.put("/users/" + useruid + "/group/", upperString);
        mDatabase.updateChildren(addusertogroup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                task.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        group.setOnPreferenceClickListener(null);
                        groupRefresh();
                    }
                });
                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }


    private void InvitesFragment() {
        invmemb.setOnPreferenceClickListener(null);
        invmemb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.mainContent, new InvitesView())
                        .addToBackStack(null)
                        .commit();
                return false;
            }
        });



    }

    private void LeaveGroupFunction() {

        preferenceScreen.addPreference(leaveGroup);
        leaveGroup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(LeaveGroup);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mAuth.getCurrentUser() != null) {
                            DatabaseReference userGroup = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("group");

                            userGroup.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    preferenceScreen.removePreference(leaveGroup);
                                    CreateGroup();
                                    groupRefresh();
                                }
                            });
                        }
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


    private void MembersListFragment() {
        invmemb.setOnPreferenceClickListener(null);
        invmemb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.mainContent, new MembersView())
                        .addToBackStack(null)
                        .commit();
                return false;
            }
        });
    }


}
