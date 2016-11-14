package com.josepgrs.reminder;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by josep on 14/11/2016.
 */

public class Settings extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }
}
