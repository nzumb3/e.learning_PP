package de.tudarmstadt.informatik.tudas.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import de.tudarmstadt.informatik.tudas.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
