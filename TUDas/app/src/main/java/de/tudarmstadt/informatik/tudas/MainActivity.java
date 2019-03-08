package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.Calendar;

import de.tudarmstadt.informatik.tudas.adapters.AppointmentListAdapter;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private TimeTableViewModel mWordViewModel;
    //private DailyAppointmentsViewModel mWordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final AppointmentListAdapter adapter = new AppointmentListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            //Do something when button is clicked
        });

        mWordViewModel = ViewModelProviders.of(this).get(TimeTableViewModel.class);
        //mWordViewModel = ViewModelProviders.of(this).get(DailyAppointmentsViewModel.class);

        //Intent intent = new Intent(this, NewAppointmentActivity.class);
        //Intent intent = new Intent(this, TimeTableActivity.class);
        //Intent intent = new Intent(this, DailyAppointmentsActivity.class);
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void changeView(){
        Intent intent = new Intent(this, TimeTableActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //changeView();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = (String) data.getExtras().get("appointment_title");
            String description = (String) data.getExtras().get("appointment_description");
            Calendar starttime = (Calendar) data.getExtras().get("appointment_starttime");
            Calendar endtime = (Calendar) data.getExtras().get("appointment_endtime");
            AppointmentContent content = new AppointmentContent();
            content.setTitle(title);
            content.setDescription(description);
            Appointment word = new Appointment();
            word.setStartDate(starttime);
            word.setEndDate(endtime);
            mWordViewModel.insert(content, word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
