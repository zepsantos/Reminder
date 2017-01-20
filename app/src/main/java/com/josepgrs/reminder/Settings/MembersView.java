package com.josepgrs.reminder.Settings;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.josepgrs.reminder.GetUserInformation;
import com.josepgrs.reminder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersView extends android.app.Fragment {
    ListView membersListView;
    EditText invitedusername;
    TextView submitInvite;
    DatabaseReference mDatabase;
    DatabaseReference groupMembers;
    FirebaseListAdapter mAdapter;
    GetUserInformation userInfo;

    public MembersView() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_members_view, container, false);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        membersListView = (ListView) view.findViewById(R.id.memberslv);
        userInfo = GetUserInformation.getInstance();
        invitedusername = (EditText) view.findViewById(R.id.invitemembername);
        submitInvite = (TextView) view.findViewById(R.id.addmembertv);
        initList();
        inviteUser();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initList() {
        groupMembers = mDatabase.child("groups").child(userInfo.getUserGroup()).child("members");
        mAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, android.R.layout.simple_list_item_1, groupMembers) {
            @Override
            protected void populateView(View v, String model, int position) {
                //TextView tv = (TextView) v.findViewById(android.R.id.text1);
                //tv.setText(model);
            }
        };
        membersListView.setAdapter(mAdapter);
    }

    private void inviteUser() {
        submitInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Invite(invitedusername.getText().toString());
            }
        });
    }

    private void Invite(final String username) {
        DatabaseReference useruid = mDatabase.child("username").child(username);
        useruid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final String uid = dataSnapshot.getValue(String.class);

                    DatabaseReference membersInvited = mDatabase.child("/groups/" + userInfo.getUserGroup() + "/members-invited/" + uid);
                    membersInvited.setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DatabaseReference groupInvites = mDatabase.child("/group-invites/" + uid + "/" + userInfo.getUserName());
                                    groupInvites.setValue(userInfo.getUserGroup()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                }
                            });

                        }
                    });

                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Username doesn't exist", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }  // FALTA AÇAO PARA QUANDO O INVITE É BEM SUCEDIDO
}
