package com.josepgrs.reminder.Settings;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.colorpicker.ColorPickerDialog;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josepgrs.reminder.GetUserInformation;
import com.josepgrs.reminder.Model.Subject;
import com.josepgrs.reminder.R;

import static android.R.attr.numColumns;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectsManagement extends android.app.Fragment {
	DatabaseReference mDatabase;
	DatabaseReference userSubject;
	GetUserInformation userInfo;
	ListView subjectlv;
	FirebaseListAdapter mAdapter;
    Context mContext;
    int[] colors = {R.color.red, R.color.pink, R.color.purple, R.color.deep_purple, R.color.indigo, R.color.blue, R.color.light_blue, R.color.cyan, R.color.teal, R.color.green, R.color.light_green, R.color.lime, R.color.yellow, R.color.amber, R.color.orange, R.color.deep_orange, R.color.brown, R.color.grey, R.color.blue_grey};

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
        mContext = view.getContext();
        initList();
		AddSubject();
		super.onViewCreated(view, savedInstanceState);
	}

	private void AddSubject() {

        View mView = new View(mContext);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_addsubject);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Subject Name:");


                final EditText input = new EditText(mContext);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                //input.layout(50, 10, 50, 10);
                final ImageView subjcolor = new ImageView(mContext);
                final ShapeDrawable drawable = new ShapeDrawable(new OvalShape());

                drawable.getPaint().setColor(Color.BLUE);
                subjcolor.setBackground(drawable);
                LinearLayout ll = new LinearLayout(mContext);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(60, 20, 20, 20);
                layoutParams.width = 650;
                layoutParams.height = 100;
                imageParams.setMargins(50, 10, 10, 10);
                imageParams.width = 75;
                imageParams.height = 75;
                ll.addView(input, layoutParams);
                ll.addView(subjcolor, imageParams);
                builder.setView(ll);
                final int selectedColor = drawable.getPaint().getColor();
                subjcolor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
                        colorPickerDialog.initialize(
                                R.string.GroupSchool, colors, selectedColor, numColumns, colors.length);
                        colorPickerDialog.show(getFragmentManager(), "TAG");
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        String mText = input.getText().toString();
                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

                builder.show();
            }
		});
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

