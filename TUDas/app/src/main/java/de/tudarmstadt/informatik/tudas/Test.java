package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentViewModel;
import de.tudarmstadt.informatik.tudas.model.CalendarConverter;
import timber.log.Timber;

public class Test extends AppCompatActivity {

    private List<ListView> listViews;
    private ListView timeSlotView;
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
        timeSlotView = (ListView) findViewById(R.id.lvTimeSlots);
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
        TimeSlotAdapter adapter= new TimeSlotAdapter(this);
        viewModel.getTimeSlots().observe(this, adapter::setTimeslots);
        timeSlotView.setAdapter(adapter);
    }

    private class TimeSlotAdapter extends BaseAdapter{

        private LayoutInflater layoutInflater;
        private List<Calendar> hourCalendars;
        private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        TimeSlotAdapter(Context context){
            hourCalendars = new LinkedList<>();
            layoutInflater = LayoutInflater.from(context);
        }

        public void setTimeslots(List<Calendar> calendars){
            hourCalendars = calendars;
            Timber.d("MyLog: timeslots = " + CalendarConverter.fromCalendar(calendars.get(calendars.size() - 1)));
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return hourCalendars.size();
        }

        @Override
        public Object getItem(int position) {
            return hourCalendars.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = layoutInflater.inflate(R.layout.timeslot_layout, null);

            if(hourCalendars != null && hourCalendars.size() >= position + 1){
                RelativeLayout timeslotBlock = convertView.findViewById(R.id.timeslotBlock);
                TextView time = convertView.findViewById(R.id.timeslotText);

                Calendar calendar = hourCalendars.get(position);
                timeslotBlock.setBackgroundColor(Color.WHITE);
                time.setText(timeFormat.format(calendar.getTime()));

                timeslotBlock.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 50));
                timeslotBlock.getLayoutParams().height = 60*AppointmentViewModel.pixelPerMinute;
                timeslotBlock.getLayoutParams().width = RelativeLayout.LayoutParams.FILL_PARENT;
            }

            return convertView;
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
                timetableBlock.setBackgroundColor(Color.parseColor(appointment.getAppointmentContent().getColor()));

                time.setText(appointment.toTimeString());
                room.setText(appointment.getAppointmentContent().getRoom());
                abbr.setText(appointment.getAppointmentContent().getAbbreviation());

                timetableBlock.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 50));
                timetableBlock.getLayoutParams().height = appointment.getDurationBeforeMidnight()*AppointmentViewModel.pixelPerMinute;
                timetableBlock.getLayoutParams().width = RelativeLayout.LayoutParams.FILL_PARENT;
            }

            return convertView;
        }
    }

}