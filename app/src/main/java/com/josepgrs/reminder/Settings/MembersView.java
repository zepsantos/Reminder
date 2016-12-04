package com.josepgrs.reminder.Settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josepgrs.reminder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersView extends android.app.Fragment {


	public MembersView() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_members_view, container, false);
	}

}
