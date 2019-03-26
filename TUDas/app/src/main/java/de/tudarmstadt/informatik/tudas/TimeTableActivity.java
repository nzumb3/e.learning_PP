package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.tudarmstadt.informatik.tudas.adapters.AppointmentAdapter;
import de.tudarmstadt.informatik.tudas.adapters.HourAdapter;
import de.tudarmstadt.informatik.tudas.listeners.NavigationButtonListener;
import de.tudarmstadt.informatik.tudas.listeners.NavigationListener;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;
import timber.log.Timber;

public class TimeTableActivity extends AppCompatActivity {

    private List<ListView> listViews;
    private ListView timeSlotView;
    private TimeTableViewModel viewModel;
    DrawerLayout drawerLayout;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(TimeTableViewModel.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int days = prefs.getInt(getString(R.string.timetableSizeTitle), getResources().getInteger(R.integer.timetableDaysDefault));
        Timber.d("Mylog: " + days);
        viewModel.setNumDays(days);

        setContentView(R.layout.activity_timetable);
        setupUIViews();
        setupListView();

        FloatingActionButton fab = findViewById(R.id.fabNewAppointmentButton);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewAppointmentActivity.class);
            startActivity(intent);
        });

        setSupportActionBar(findViewById(R.id.toolbarTimetable));
        drawerLayout = findViewById(R.id.drawerLayout_timetable);
        NavigationView navView = findViewById(R.id.nav_view);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navView.setNavigationItemSelectedListener(new NavigationListener(TimeTableActivity.this, drawerLayout));
        Button navButton = findViewById(R.id.navButton_timetable);
        navButton.setOnClickListener(new NavigationButtonListener(drawerLayout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int days = prefs.getInt(getString(R.string.timetableSizeTitle), getResources().getInteger(R.integer.timetableDaysDefault));
        viewModel.setNumDays(days);
    }

    private void setupUIViews() {
        DatePickerDialog.OnDateSetListener dateStartFunction = (view, year, month, dayOfMonth) -> {
            Calendar startDate = Calendar.getInstance();
            startDate.set(year, month, dayOfMonth, 0, 0, 0);
            startDate.set(Calendar.MILLISECOND, 0);
            datePickerDialog.updateDate(year, month, dayOfMonth);
            viewModel.setStartDate(startDate);
        };

        viewModel.getStartDate().observe(this, (calendar -> datePickerDialog = new DatePickerDialog(this, dateStartFunction, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))));

        findViewById(R.id.tvTimeFrame).setOnClickListener(v -> datePickerDialog.show());

        listViews = new ArrayList<>();
        listViews.add((ListView) findViewById(R.id.lvToday));
        listViews.add((ListView) findViewById(R.id.lvTomorrow));
        timeSlotView = (ListView) findViewById(R.id.lvTimeSlots);
    }

    private void setupListView() {
        viewModel.getPeriodString().observe(this, (string) -> {
            if(string != null) {
                TextView periodTextView = findViewById(R.id.tvTimeFrame);
                periodTextView.setText(string);
            }
        });
        viewModel.getAppointments().observe(this, (appointments) -> {
            if(appointments != null) {
                for(int day = 0; day < appointments.size(); day++) {
                    AppointmentAdapter adapter = new AppointmentAdapter(this);
                    adapter.setList(appointments.get(day));
                    listViews.get(day).setAdapter(adapter);
                }
            }
        });

        HourAdapter adapter = new HourAdapter(this, this);
        viewModel.getTimeSlots().observe(this, adapter::setList);
        timeSlotView.setAdapter(adapter);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 60*1000);
            }
        }, 60*1000);
    }
}