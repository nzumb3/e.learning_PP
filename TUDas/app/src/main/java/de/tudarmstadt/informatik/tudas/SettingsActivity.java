package de.tudarmstadt.informatik.tudas;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Locale;

import de.tudarmstadt.informatik.tudas.fragments.SettingsFragment;
import de.tudarmstadt.informatik.tudas.listeners.NavigationButtonListener;
import de.tudarmstadt.informatik.tudas.listeners.NavigationListener;

public class SettingsActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settintsContainer, new SettingsFragment())
                .commit();
        setSupportActionBar(findViewById(R.id.toolbarSettings));

        drawerLayout = findViewById(R.id.drawerLayout_settings);

        ImageButton navButton = findViewById(R.id.navButton_settings);
        navButton.setOnClickListener(new NavigationButtonListener(drawerLayout));
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationListener(this, drawerLayout));
    }
}