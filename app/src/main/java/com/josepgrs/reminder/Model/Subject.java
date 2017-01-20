package com.josepgrs.reminder.Model;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class Subject {

	public String Name;


	public Subject() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

	public Subject(String name) {
		this.Name = name;


	}

	public String getName() {
		return Name;
	}

}
