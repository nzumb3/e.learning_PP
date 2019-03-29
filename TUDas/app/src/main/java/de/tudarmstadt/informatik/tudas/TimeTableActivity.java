package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import de.tudarmstadt.informatik.tudas.views.TimetablePopupView;

/**
 * This activity shows the appointments for a specified period. It contains of x + 1 ListViews,
 * where x is the number of days to be displayed. The first ListView shows the integer hours of the
 * days.
 * The period string is shown at a line above the ListViews.
 */
public class TimeTableActivity extends AppCompatActivity {

    private List<ListView> listViews;
    private ListView timeSlotView;
    private TimeTableViewModel viewModel;
    private TimetablePopupView popup;
    int days;
    DrawerLayout drawerLayout;

    private DatePickerDialog datePickerDialog;

    /**
     * At creation time of the activity the preferences are loaded and the UI is initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(TimeTableViewModel.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        days = prefs.getInt("timetableSize", getResources().getInteger(R.integer.timetableDaysDefault));
        viewModel.setNumDays(days);

        setContentView(R.layout.activity_timetable);
        popup = new TimetablePopupView(this);
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
        navView.setNavigationItemSelectedListener(new NavigationListener(TimeTableActivity.this, drawerLayout));
        ImageButton navButton = findViewById(R.id.navButton_timetable);
        navButton.setOnClickListener(new NavigationButtonListener(drawerLayout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * The onResume has to be called, when the number of days has changed in the settings, so the
     * timetable has to be redrawn.
     */
    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int days = prefs.getInt(getString(R.string.timetableSizeTitle), getResources().getInteger(R.integer.timetableDaysDefault));
        viewModel.setNumDays(days);
    }

    /**
     * This method initializes the UI. It provides the DatePicker for choosing the startdate. It
     * dynamically creates the ListViews depending on the number of days to be shown.
     */
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
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.llTimetable);
        LayoutInflater layoutInflater = getLayoutInflater();
        ListView view;
        timeSlotView = (ListView) layoutInflater.inflate(R.layout.component_timetable_hour_colum, parentLayout, false);
        ViewGroup.LayoutParams tmp = timeSlotView.getLayoutParams();
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        /*Maybe change this in the future, because I think it does not use the layout parameters correctly*/
        tmp.width = (int) Math.round(width*0.55);
        timeSlotView.setLayoutParams(tmp);
        parentLayout.addView(timeSlotView);
        for (int i = 0; i < this.days; i++){
            view = (ListView) layoutInflater.inflate(R.layout.component_timetable_column, parentLayout, false);
            listViews.add(view);
            parentLayout.addView(view);
        }
    }

    /**
     * This method sets the period string and fills the ListViews with the appointments from the
     * database. After that, it initializes the time line, that shows the current time in the
     * timetable.
     */
    private void setupListView() {
        viewModel.getPeriodString().observe(this, (string) -> {
            if(string != null) {
                TextView periodTextView = findViewById(R.id.tvTimeFrame);
                periodTextView.setText(string);
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int size = prefs.getInt("hourSize", getResources().getInteger(R.integer.hourSizeDefault));
        viewModel.getAppointments().observe(this, (appointments) -> {
            if(appointments != null) {
                for(int day = 0; day < appointments.size(); day++) {
                    AppointmentAdapter adapter = new AppointmentAdapter(this, size, popup, viewModel);
                    adapter.setList(appointments.get(day));
                    listViews.get(day).setAdapter(adapter);
                }
            }
        });
        HourAdapter adapter = new HourAdapter(this, this, size);
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