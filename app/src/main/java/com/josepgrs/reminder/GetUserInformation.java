package com.josepgrs.reminder;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by josep on 21/11/2016.
 */

public class GetUserInformation extends Application {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String Name;
    String UserGroup;
    String Email;
    String UserName;
    String UserUid;

    public void Init() {
        mAuth = FirebaseAuth.getInstance();
        UserUid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getAccountDetails();
    }

    private void getAccountDetails() {
        DatabaseReference NameChild;
        NameChild = mDatabase.child("users").child(UserUid).child("name");
        Name = getValue(NameChild);

    }

    public String getName() {
        if (Name != null) {
            return Name;
        }
        return null;
    }

    public String getUserGroup() {
        if (UserGroup != null) {
            return UserGroup;
        }
        return null;
    }

    public String getEmail() {
        if (Email != null) {
            return Email;
        }
        return null;
    }

    public String getUserName() {
        if (UserName != null) {
            return UserName;
        }
        return null;
    }

    public String getValue(DatabaseReference databaseReference) {
        final String[] value = {null};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value[0] = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return value[0];
    }
}
