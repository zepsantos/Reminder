package com.josepgrs.reminder.Model;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class Subject {

	public String Name;
	public String Color;

	public Subject() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

	public Subject(String Name, String Color) {
		this.Name = Name;
		this.Color = Color;
	}


}
