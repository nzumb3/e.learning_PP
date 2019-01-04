package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.model.AppointmentViewModel;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private AppointmentViewModel mWordViewModel;

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewAppointmentActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        mWordViewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);

        final StringBuilder output = new StringBuilder();

        Calendar startDate = Calendar.getInstance();
        startDate.set(2018, 12, 26, 0, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.set(2018, 12, 26, 23, 59);

        /*mWordViewModel.getAppointmentsInPeriod(CalendarConverter.fromCalendar(startDate), CalendarConverter.fromCalendar(endDate)).observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable final List<Appointment> words) {
                // Update the cached copy of the words in the adapter.
                //output.append(words).append('\n');
                //adapter.setAppointment(words);
            }
        });*/

        /*mWordViewModel.getAppointments().observe(this, new Observer<List<AppointmentContent>>() {
            @Override
            public void onChanged(@Nullable final List<AppointmentContent> words) {
                // Update the cached copy of the words in the adapter.
                //output.append(words).append('\n');
                adapter.setAppointment(words);
            }
        });*/

        /*TextView view = findViewById(R.id.textView3);

        view.setText(output.toString());*/
        //this.createAppointments();

        Intent intent = new Intent(this, NewAppointmentActivity.class);
        startActivity(intent);

        //Intent intent = new Intent(this, TimeTableActivity.class);
        //startActivity(intent);
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
            //String starttime = data.getStringExtra("appointment_starttime");
            //String endtime = data.getStringExtra("appointment_endtime");
            Calendar endtime = (Calendar) data.getExtras().get("appointment_endtime");
            AppointmentContent content = new AppointmentContent();
            content.setTitle(title);
            content.setDescription(description);
            Appointment word = new Appointment();
            word.setStartDate(starttime);
            word.setEndDate(endtime);
            Log.d("BlaBla", word.toString());
            mWordViewModel.insert(content, word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void createAppointments(){
        AppointmentContent test1 = new AppointmentContent();
        test1.setTitle("Appointment 1");
        test1.setDescription("Hier mache ich etwas. Wühüüü");
        test1.setAbbreviation("App1");
        test1.setRoom("S202/C205");
        Appointment app1 = new Appointment();
        Calendar start1 = Calendar.getInstance();
        start1.set(2018,12,24,11,40);
        Calendar end1 = Calendar.getInstance();
        end1.set(2018,12,24,13, 10);
        app1.setStartDate(start1);
        app1.setEndDate(end1);
        mWordViewModel.insert(test1, app1);

        AppointmentContent test2 = new AppointmentContent();
        test2.setTitle("Appointment 2");
        test2.setDescription("Hier mache ich etwas anderes. Wühüüü!?!");
        test2.setAbbreviation("App2");
        test2.setRoom("S202/C205");
        Appointment app2 = new Appointment();
        Calendar start2 = Calendar.getInstance();
        start2.set(2018,12,24,17,15);
        Calendar end2 = Calendar.getInstance();
        end2.set(2018, 12, 24, 18, 55);
        app2.setStartDate(start2);
        app2.setEndDate(end2);
        mWordViewModel.insert(test2, app2);

        AppointmentContent test3 = new AppointmentContent();
        test3.setTitle("Appointment 3");
        test3.setDescription("Was gibts zu essen?!!!!!?");
        test3.setAbbreviation("App3");
        test3.setRoom("S202/C205");
        Appointment app3 = new Appointment();
        Calendar start3 = Calendar.getInstance();
        start3.set(2018,12,25,11,40);
        Calendar end3 = Calendar.getInstance();
        end3.set(2018,12,25,12, 15);
        app3.setStartDate(start3);
        app3.setEndDate(end3);
        mWordViewModel.insert(test3, app3);

        AppointmentContent test4 = new AppointmentContent();
        test4.setTitle("Appointment 4");
        test4.setDescription("Essen hat geschmeckt! ;)");
        test4.setAbbreviation("App4");
        test4.setRoom("S202/C205");
        Appointment app4= new Appointment();
        Calendar start4 = Calendar.getInstance();
        start4.set(2018,12,25,19,0);
        Calendar end4 = Calendar.getInstance();
        end4.set(2018,12,26,8,0);
        app4.setStartDate(start4);
        app4.setEndDate(end4);
        mWordViewModel.insert(test4, app4);
    }
}
