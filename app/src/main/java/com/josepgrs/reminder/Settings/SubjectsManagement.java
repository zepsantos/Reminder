package com.josepgrs.reminder.Settings;import android.app.AlertDialog;import android.content.Context;import android.content.DialogInterface;import android.graphics.Color;import android.graphics.drawable.ShapeDrawable;import android.graphics.drawable.shapes.OvalShape;import android.os.Bundle;import android.os.Handler;import android.support.annotation.ColorInt;import android.support.annotation.NonNull;import android.support.v4.app.Fragment;import android.support.v4.content.res.ResourcesCompat;import android.text.InputType;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.EditText;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.ListView;import android.widget.TableLayout;import android.widget.TextView;import android.widget.Toast;import com.firebase.ui.database.FirebaseListAdapter;import com.github.clans.fab.FloatingActionButton;import com.google.android.gms.tasks.OnCompleteListener;import com.google.android.gms.tasks.Task;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import com.josepgrs.reminder.GetUserInformation;import com.josepgrs.reminder.Model.Subject;import com.josepgrs.reminder.R;import com.thebluealliance.spectrum.SpectrumPalette;/** * A simple {@link Fragment} subclass. */public class SubjectsManagement extends android.app.Fragment {	DatabaseReference mDatabase;	DatabaseReference userSubject;	GetUserInformation userInfo;	ListView subjectlv;	FirebaseListAdapter mAdapter;    Context mContext;	AlertDialog.Builder addSubjectDialog;	int selectedColor;    public SubjectsManagement() {		// Required empty public constructor	}	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,	                         Bundle savedInstanceState) {		// Inflate the layout for this fragment		return inflater.inflate(R.layout.fragment_subjects_management, container, false);	}	@Override	public void onViewCreated(View view, Bundle savedInstanceState) {		subjectlv = (ListView) view.findViewById(R.id.subjectlv);		userInfo = new GetUserInformation().getInstance();		mDatabase = FirebaseDatabase.getInstance().getReference();        mContext = view.getContext();        initList();		AddSubject();		super.onViewCreated(view, savedInstanceState);	}	private void AddSubject() {		selectedColor = ResourcesCompat.getColor(getResources(), R.color.blue, null);		View mView = new View(mContext);        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_addsubject);		fab.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View view) {				AddSubjectDialog();			}		});	}	private void AddSubjectDialog() {		addSubjectDialog = new AlertDialog.Builder(mContext);		addSubjectDialog.setTitle("Subject Name:");		final EditText input = new EditText(mContext);		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);		//input.layout(50, 10, 50, 10);		final ImageView subjcolor = new ImageView(mContext);		final ShapeDrawable drawable = new ShapeDrawable(new OvalShape());		drawable.getPaint().setColor(selectedColor);		subjcolor.setBackground(drawable);		final LinearLayout ll = new LinearLayout(mContext);		ll.setOrientation(LinearLayout.HORIZONTAL);		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);		layoutParams.setMargins(60, 20, 20, 20);		layoutParams.width = 650;		layoutParams.height = 100;		imageParams.setMargins(50, 10, 10, 10);		imageParams.width = 75;		imageParams.height = 75;		ll.addView(input, layoutParams);		ll.addView(subjcolor, imageParams);		addSubjectDialog.setView(ll);		selectedColor = drawable.getPaint().getColor();		subjcolor.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				colorPick(subjcolor);			}		});		addSubjectDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {			@Override			public void onClick(final DialogInterface dialog, int which) {				String mText = input.getText().toString();				UploadNewSubject(mText);			}		});		addSubjectDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {			@Override			public void onClick(DialogInterface dialog, int which) {				dialog.cancel();			}		});		addSubjectDialog.show();	}	private void UploadNewSubject(final String mText) {		final DatabaseReference subjectName = mDatabase.child("/userSubjects/").child(userInfo.getUserUid()).child("/" + mText).child("/Name");		subjectName.setValue(mText).addOnCompleteListener(new OnCompleteListener<Void>() {			@Override			public void onComplete(@NonNull Task<Void> task) {				if (task.isSuccessful()) {					DatabaseReference subjectColor = mDatabase.child("/userSubjects/").child(userInfo.getUserUid()).child("/" + mText).child("/Color");					String hexColor = "#" + Integer.toHexString(selectedColor).toUpperCase().substring(2);					subjectColor.setValue(hexColor);				} else {					Toast.makeText(mContext, "Subject already exists!", Toast.LENGTH_LONG).show();				}			}		});	}	private void colorPick(final ImageView subjcolor) {		AlertDialog.Builder colorpick = new AlertDialog.Builder(mContext);		colorpick.setTitle("Pick a color");		TableLayout tableLayout = new TableLayout(mContext);		TableLayout.LayoutParams tlLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);		tlLayoutParams.setMargins(100, 70, 0, 100);		final SpectrumPalette spectrumPalette = new SpectrumPalette(mContext);		spectrumPalette.setColors(getResources().getIntArray(R.array.demo_colors));		spectrumPalette.setFixedColumnCount(4);		spectrumPalette.setOutlineWidth(0);		tableLayout.addView(spectrumPalette, tlLayoutParams);		colorpick.setView(tableLayout);		colorpick.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {			@Override			public void onClick(DialogInterface dialog, int which) {				dialog.dismiss();			}		});		final AlertDialog show = colorpick.show();		show.create();		spectrumPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {			@Override			public void onColorSelected(@ColorInt int color) {				selectedColor = color;				final ShapeDrawable drawable = new ShapeDrawable(new OvalShape());				drawable.getPaint().setColor(selectedColor);				subjcolor.setBackground(drawable);				subjcolor.refreshDrawableState();				final Handler handler = new Handler();				handler.postDelayed(new Runnable() {					@Override					public void run() {						// Do something after 5s = 5000ms						show.dismiss();					}				}, 200);			}		});	}	private void initList() {		userSubject = mDatabase.child("userSubjects").child(userInfo.getUserUid());		mAdapter = new FirebaseListAdapter<Subject>(getActivity(), Subject.class, R.layout.subjectlist, userSubject) {			@Override			protected void populateView(View v, Subject model, int position) {				TextView tv = (TextView) v.findViewById(R.id.subjectsname);				tv.setText(model.Name);				ImageView subjcolor = (ImageView) v.findViewById(R.id.subjectscolor);				ShapeDrawable drawable = new ShapeDrawable(new OvalShape());				if (model.Color != null) {					drawable.getPaint().setColor(Color.parseColor(model.Color));					subjcolor.setBackground(drawable);				}			}		};		subjectlv.setAdapter(mAdapter);	}}