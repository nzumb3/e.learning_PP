package de.tudarmstadt.informatik.tudas;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tudas.adapters.DailyAppointmentsListViewAdapter;
import de.tudarmstadt.informatik.tudas.listeners.NavigationButtonListener;
import de.tudarmstadt.informatik.tudas.listeners.NavigationListener;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.utils.CalendarConverter;
import de.tudarmstadt.informatik.tudas.viewmodels.DailyAppointmentsViewModel;
import de.tudarmstadt.informatik.tudas.views.DailyAppointmentPopupView;
import timber.log.Timber;

public class DailyAppointmentsActivity extends AppCompatActivity {

    DailyAppointmentsViewModel viewModel;
    ListView dailyAppointmentsListView;
    Calendar startDate;
    DrawerLayout drawerLayout;

    DailyAppointmentPopupView popUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_daily_appointments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDailyAppointments);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        viewModel = ViewModelProviders.of(this).get(DailyAppointmentsViewModel.class);

        viewModel.getCurrentDate().observe(this, (calendar -> {
            if(calendar != null) {
                TextView dateTextView = findViewById(R.id.tvTimeFrame);
                SimpleDateFormat format = new SimpleDateFormat("dd. MMM yyyy");
                dateTextView.setText(format.format(calendar.getTime()));
            }
        }));

        this.refreshView();

        dailyAppointmentsListView = findViewById(R.id.lvDailyAppointments);
        startDate = Calendar.getInstance();
        startDate.set(2019, Calendar.MARCH, 15, 0, 0, 0);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        popUp = new DailyAppointmentPopupView(this);

        viewModel.getInformationCode().observe(this, (code) -> {
            if(code != null) {
                switch (code) {
                    case 1:
                        findViewById(R.id.tvNoAppointments).setVisibility(View.VISIBLE);
                        findViewById(R.id.tvNoAppointmentsNoLists).setVisibility(View.INVISIBLE);
                        findViewById(R.id.lvDailyAppointments).setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        findViewById(R.id.tvNoAppointments).setVisibility(View.INVISIBLE);
                        findViewById(R.id.tvNoAppointmentsNoLists).setVisibility(View.VISIBLE);
                        findViewById(R.id.lvDailyAppointments).setVisibility(View.INVISIBLE);
                        break;
                    default:
                        findViewById(R.id.tvNoAppointments).setVisibility(View.INVISIBLE);
                        findViewById(R.id.tvNoAppointmentsNoLists).setVisibility(View.INVISIBLE);
                        findViewById(R.id.lvDailyAppointments).setVisibility(View.VISIBLE);
                }
            }
        });

        DailyAppointmentsListViewAdapter adapter = new DailyAppointmentsListViewAdapter(this, popUp);
        viewModel.getAppointmentsForDay().observe(this, adapter::setList);
        dailyAppointmentsListView.setAdapter(adapter);

        drawerLayout = findViewById(R.id.drawerLayoutDailyAppointments);
        NavigationView navView = findViewById(R.id.nav_view);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navView.setNavigationItemSelectedListener(new NavigationListener(this, drawerLayout));

        Button navButton = findViewById(R.id.navButton_dailyAppointments);
        navButton.setOnClickListener(new NavigationButtonListener(drawerLayout));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refreshView(){
        dailyAppointmentsListView = findViewById(R.id.lvDailyAppointments);
        startDate = Calendar.getInstance();
        startDate.set(2019, Calendar.MARCH, 15, 0, 0, 0);

        popUp = new DailyAppointmentPopupView(this);

        DailyAppointmentsListViewAdapter adapter = new DailyAppointmentsListViewAdapter(this, popUp);
        viewModel.getAppointmentsForDay().observe(this, adapter::setList);
        dailyAppointmentsListView.setAdapter(adapter);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
        else
            viewModel.setPermissionStatus(PackageManager.PERMISSION_GRANTED);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DailyAppointmentsActivity.this);
                Timber.d("MyLog: Shared Prefs: " + prefs.getAll().toString());
                String loc = Locale.getDefault().getDisplayCountry();
                Timber.d("MyLog: Current Locale -> " + loc);
            }
        });

        drawerLayout = findViewById(R.id.drawerLayoutDailyAppointments);
        NavigationView navView = findViewById(R.id.nav_view);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navView.setNavigationItemSelectedListener(new NavigationListener(this, drawerLayout));

        Button navButton = findViewById(R.id.navButton_dailyAppointments);
        navButton.setOnClickListener(new NavigationButtonListener(drawerLayout));
    }
}