package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.tudarmstadt.informatik.tudas.adapters.AppointmentAdapter;
import de.tudarmstadt.informatik.tudas.adapters.HourAdapter;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;

public class TimeTableActivity extends AppCompatActivity {

    private List<ListView> listViews;
    private ListView timeSlotView;
    private TimeTableViewModel viewModel;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(TimeTableViewModel.class);

        setContentView(R.layout.activity_timetable);
        setupUIViews();
        setupListView();

        FloatingActionButton fab = findViewById(R.id.fabNewAppointmentButton);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewAppointmentActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
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