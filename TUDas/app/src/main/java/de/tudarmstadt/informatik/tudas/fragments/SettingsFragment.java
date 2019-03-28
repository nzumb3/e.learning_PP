package de.tudarmstadt.informatik.tudas.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import de.tudarmstadt.informatik.tudas.R;

/*
* Fragment, which is loaded by the SettingsActivity and holds the view of the Settings.
*/
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
