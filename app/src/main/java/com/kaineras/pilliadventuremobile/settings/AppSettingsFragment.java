package com.kaineras.pilliadventuremobile.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.kaineras.pilliadventuremobile.R;

/**
 * Created the first version by kaineras on 3/02/15.
 */
public class AppSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}