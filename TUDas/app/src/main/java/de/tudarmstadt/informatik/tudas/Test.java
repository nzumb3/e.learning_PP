package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentViewModel;
import de.tudarmstadt.informatik.tudas.model.CalendarConverter;

public class Test extends AppCompatActivity {

    private List<ListView> listViews;
    private AppointmentViewModel viewModel;

    private Calendar startDate;
    private Calendar endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDate = Calendar.getInstance();
        startDate.set(2018, 11, 24, 0, 0);
        endDate = Calendar.getInstance();
        endDate.set(2018, 11, 25, 0, 0);

        viewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);

        setContentView(R.layout.activity_test);
        setupUIViews();
        setupListView();
    }

    private void setupUIViews() {
        viewModel.setEarliestBeginning(CalendarConverter.fromCalendar(startDate), CalendarConverter.fromCalendar(endDate));
        listViews = new ArrayList<>();
        listViews.add((ListView) findViewById(R.id.lvToday));
        listViews.add((ListView) findViewById(R.id.lvTomorrow));
    }

    private int getDaysBetweenStartAndEnd() {
        return (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis()) / 1000 / 60 / 60 / 24);
    }

    private void setupListView() {
        Calendar date = (Calendar) startDate.clone();
        for(int day = 0; day <= getDaysBetweenStartAndEnd(); day++) {
            SimpleAdapter simpleAdapter = new SimpleAdapter(this);
            viewModel.getAppointmentsForDay(CalendarConverter.toDateString(date)).observe(this, simpleAdapter::setAppointments);
            //viewModel.getAppointmentsForDay(CalendarConverter.toDateString(date)).observe(this, (appointments -> simpleAdapter.setAppointments(appointments)));
            /*viewModel.getAppointmentsForDay(CalendarConverter.toDateString(date)).observe(this, new Observer<List<Appointment>>() {
                @Override
                public void onChanged(@Nullable List<Appointment> appointments) {
                    simpleAdapter.setAppointments(appointments);
                }
            });*/
            listViews.get(day).setAdapter(simpleAdapter);
            date.add(Calendar.DATE, 1);
        }
    }

    private class SimpleAdapter extends BaseAdapter{

        //private Context mContext;
        private LayoutInflater layoutInflater;
        private List<Appointment> appointments;

        SimpleAdapter(Context context){
            //mContext = context;
            appointments = new LinkedList<>();
            layoutInflater = LayoutInflater.from(context);
        }

        void setAppointments(List<Appointment> appointments){
            this.appointments = appointments;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return appointments.size();
        }

        @Override
        public Object getItem(int position) {
            return appointments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = layoutInflater.inflate(R.layout.timetable_entry, null);

            if(appointments != null && appointments.size() >= position + 1) {
                RelativeLayout timetableBlock = convertView.findViewById(R.id.timetableEntryBlock);
                TextView abbr = convertView.findViewById(R.id.timetableEntryAbbreviation);
                TextView time = convertView.findViewById(R.id.timetableEntryTime);
                TextView room = convertView.findViewById(R.id.timetableEntryRoom);

                Appointment appointment = appointments.get(position);

                time.setText(appointment.toTimeString());
                room.setText(appointment.getAppointmentContent().getRoom());
                abbr.setText(appointment.getAppointmentContent().getAbbreviation());

                timetableBlock.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 50));
                timetableBlock.getLayoutParams().height = appointment.getDurationBeforeMidnight()*AppointmentViewModel.pixelPerMinute;
            }

            return convertView;
        }
    }

}