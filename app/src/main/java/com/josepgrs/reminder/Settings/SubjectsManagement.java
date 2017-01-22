package com.josepgrs.reminder.Settings;


import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josepgrs.reminder.GetUserInformation;
import com.josepgrs.reminder.Model.Subject;
import com.josepgrs.reminder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectsManagement extends android.app.Fragment {
	DatabaseReference mDatabase;
	DatabaseReference userSubject;
	GetUserInformation userInfo;
	ListView subjectlv;
	FirebaseListAdapter mAdapter;

	public SubjectsManagement() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_subjects_management, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		subjectlv = (ListView) view.findViewById(R.id.subjectlv);
		userInfo = new GetUserInformation().getInstance();
		mDatabase = FirebaseDatabase.getInstance().getReference();
		initList();
		super.onViewCreated(view, savedInstanceState);
	}

	private void initList() {
		userSubject = mDatabase.child("userSubjects").child(userInfo.getUserUid());
		mAdapter = new FirebaseListAdapter<Subject>(getActivity(), Subject.class, R.layout.subjectlist, userSubject) {


			@Override
			protected void populateView(View v, Subject model, int position) {
				TextView tv = (TextView) v.findViewById(R.id.subjectsname);
				tv.setText(model.Name);
				ImageView subjcolor = (ImageView) v.findViewById(R.id.subjectscolor);
				ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
				if (model.Color != null) {
					drawable.getPaint().setColor(Color.parseColor(model.Color));
					subjcolor.setBackground(drawable);
				}

			}


		};
		subjectlv.setAdapter(mAdapter);
	}


}

