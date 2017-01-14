package com.josepgrs.reminder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class GetUserInformation {
    static String name;
    static String userGroup;
    static String email;
    static String userName;
    static String userUid;
    private static GetUserInformation instance = new GetUserInformation();
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ValueEventListener dbname;
    ValueEventListener dbuserName;
    ValueEventListener dbuserGroup;

    // Getter-Setters
    public static GetUserInformation getInstance() {
        return instance;
    }

    public void Init() {

        mAuth = FirebaseAuth.getInstance();
        userUid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        email = mAuth.getCurrentUser().getEmail();
        dbValueListener();
        getAccountDetails();
    }

    private void getAccountDetails() {
        DatabaseReference NameChild;
        NameChild = mDatabase.child("users").child(userUid).child("name");
        NameChild.addValueEventListener(dbname);
        DatabaseReference userNameChild;
        userNameChild = mDatabase.child("users").child(userUid).child("username");
        userNameChild.addValueEventListener(dbuserName);
        DatabaseReference userGroupChild;
        userGroupChild = mDatabase.child("users").child(userUid).child("group");
        userGroupChild.addValueEventListener(dbuserGroup);


    }

    private void dbValueListener() {
        dbname = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbuserGroup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userGroup = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbuserName = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    public String getName() {
        if (name != null) {
            return name;
        }
        return null;
    }

    public String getUserGroup() {
        if (userGroup != null) {
            return userGroup;
        }
        return null;
    }

    public String getEmai() {
        if (email != null) {
            return email;
        }
        return null;
    }

    public String getUserName() {
        if (userName != null) {
            return userName;
        }
        return null;
    }

    public String getUserUid() {
        if (userUid != null) {
            return userUid;
        }
        return null;
    }


}
