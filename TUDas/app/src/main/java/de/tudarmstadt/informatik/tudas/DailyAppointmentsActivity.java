package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import de.tudarmstadt.informatik.tudas.viewmodels.DailyAppointmentsViewModel;
import timber.log.Timber;

public class DailyAppointmentsActivity extends AppCompatActivity {

    DailyAppointmentsViewModel viewModel;
    ListView dailyAppointmentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_appointments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(DailyAppointmentsViewModel.class);
        dailyAppointmentsListView = findViewById(R.id.lvDailyAppointments);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("MyLog: PRESS!!");
            }
        });
    }
}
