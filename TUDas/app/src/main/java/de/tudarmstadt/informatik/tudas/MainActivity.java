package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.model.AppointmentViewModel;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private AppointmentViewModel mWordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final AppointmentListAdapter adapter = new AppointmentListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*Intent intent = new Intent(this, Test2.class);
        startActivity(intent);*/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        Intent intent = new Intent(this, Test.class);
        startActivity(intent);

        mWordViewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);

        final StringBuilder output = new StringBuilder();

        mWordViewModel.getAppointments().observe(this, new Observer<List<AppointmentContent>>() {
            @Override
            public void onChanged(@Nullable final List<AppointmentContent> words) {
                // Update the cached copy of the words in the adapter.
                //output.append(words).append('\n');
                adapter.setAppointment(words);
            }
        });

        /*TextView view = findViewById(R.id.textView3);

        view.setText(output.toString());*/
    }

    public void changeView(){
        Intent intent = new Intent(this, Test.class);
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
            AppointmentContent content = new AppointmentContent();
            content.setTitle("Ttiel");
            content.setDescription("DSteadfa");
            Appointment word = new Appointment();
            /*word.setStartDay(null);
            word.setEndDay(null);*/
            word.setStartDate(new GregorianCalendar(2018, 12, 2, 11, 40));
            word.setEndDate(new GregorianCalendar(2018, 12, 30, 13, 20));
            mWordViewModel.insert(content, word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
