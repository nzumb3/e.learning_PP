package de.tudarmstadt.informatik.tudas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.tudarmstadt.informatik.tudas.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settintsContainer, new SettingsFragment())
                .commit();

    }
}
