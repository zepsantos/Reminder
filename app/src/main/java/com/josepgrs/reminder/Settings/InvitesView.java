package com.josepgrs.reminder.Settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josepgrs.reminder.GetUserInformation;
import com.josepgrs.reminder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvitesView extends android.app.Fragment {
    DatabaseReference mDatabase;
    FirebaseListAdapter mAdapter;
    GetUserInformation userInfo;
    DatabaseReference inviteUid;
    ListView invitesAvailableList;

    public InvitesView() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_invites_view, container, false);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userInfo = GetUserInformation.getInstance();
        invitesAvailableList = (ListView) view.findViewById(R.id.invitesAvailableList);
        invitesAvailable();
        super.onViewCreated(view, savedInstanceState);
    }

    private void invitesAvailable() {
        inviteUid = mDatabase.child("group-invites").child(userInfo.getUserUid());
        mAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.inviteslayout, inviteUid) {
            @Override
            protected void populateView(View v, final String model, int position) {
                TextView tv = (TextView) v.findViewById(R.id.invitedgroup);
                tv.setText(model);
                final ImageView accept = (ImageView) v.findViewById(R.id.acceptinvite);
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        acceptInvite(model);
                    }
                });
            }
        };
        invitesAvailableList.setAdapter(mAdapter);

    }

    private void acceptInvite(final String groupname) {
        DatabaseReference addmemberGroup = mDatabase.child("/groups/" + groupname + "/" + "members/" + userInfo.getUserUid());
        addmemberGroup.setValue(userInfo.getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DatabaseReference addmemberGroup = mDatabase.child("/users/" + userInfo.getUserUid() + "/group/");
                addmemberGroup.setValue(groupname).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });


    } //AÇAO PARA QUANDO O CONVITE É ACEITE E O UTILIZADOR JA FAZ PARTE DO GRUPO
}
