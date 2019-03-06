package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
/*import android.content.Context;
import android.graphics.Color;*/
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/*import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;*/
import android.view.View;
import android.widget.ListView;
/*import android.widget.RelativeLayout;
import android.widget.TextView;*/

//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tudas.adapters.AppointmentAdapter;
import de.tudarmstadt.informatik.tudas.adapters.HourAdapter;
//import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;
import de.tudarmstadt.informatik.tudas.utils.CalendarConverter;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModelNew;

public class TimeTableActivity extends AppCompatActivity {

    private List<ListView> listViews;
    private ListView timeSlotView;
    private TimeTableViewModel viewModel;
    private TimeTableViewModelNew viewModelNew;
    //private int titleHeight = 0;

    private Calendar startDate;
    private Calendar endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDate = Calendar.getInstance();
        startDate.set(2018, 11, 25, 0, 0);
        endDate = Calendar.getInstance();
        endDate.set(2018, 11, 26, 0, 0);
        //endDate.set(Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH) + 1);

        viewModel = ViewModelProviders.of(this).get(TimeTableViewModel.class);

        setContentView(R.layout.activity_timetable);
        setupUIViews();
        setupListView();

        FloatingActionButton fab = findViewById(R.id.fabNewAppointmentButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimeTableActivity.this, NewAppointmentActivity.class);
                startActivity(intent);
            }
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
        viewModel.setEarliestBeginning(CalendarConverter.fromCalendar(startDate), CalendarConverter.fromCalendar(endDate));
        viewModel.setLatestEnding(CalendarConverter.fromCalendar(startDate), CalendarConverter.fromCalendar(endDate));

        viewModelNew = ViewModelProviders.of(this).get(TimeTableViewModelNew.class);

        /*viewModelNew.getAppointments().observe(this, (appointments -> {
            Timber.d("MyLog: There are " + appointments.size() + " days.");
        }));*/

        listViews = new ArrayList<>();
        listViews.add((ListView) findViewById(R.id.lvToday));
        listViews.add((ListView) findViewById(R.id.lvTomorrow));
        timeSlotView = (ListView) findViewById(R.id.lvTimeSlots);
    }

    private void setupListView() {
        /*viewModel.getAppointments().observe(this, (lists -> {
            Timber.d("MyLog: Number of days = " + lists.size());
        }));*/
        Calendar date = (Calendar) startDate.clone();
        viewModelNew.getAppointments().observe(this, (appointments) -> {
            if(appointments != null) {
                for(int day = 0; day < appointments.size(); day++) {
                    AppointmentAdapter adapter = new AppointmentAdapter(this);
                    adapter.setList(appointments.get(day));
                    listViews.get(day).setAdapter(adapter);
                }
            }
        });
        /*for(int day = 0; day < viewModel.getDaysBetweenStartAndEnd(); day++) {
            AppointmentAdapter adapter = new AppointmentAdapter(this);
            //SimpleAdapter simpleAdapter = new SimpleAdapter(this);
            viewModel.getAppointmentsForDay(CalendarConverter.toDateString(date)).observe(this, adapter::setList);*/
            //viewModel.getAppointmentsForDay(CalendarConverter.toDateString(date)).observe(this, (appointments -> simpleAdapter.setAppointments(appointments)));
            /*viewModel.getAppointmentsForDay(CalendarConverter.toDateString(date)).observe(this, new Observer<List<Appointment>>() {
                @Override
                public void onChanged(@Nullable List<Appointment> appointments) {
                    simpleAdapter.setAppointments(appointments);
                }
            });*/
            /*listViews.get(day).setAdapter(adapter);
            date.add(Calendar.DATE, 1);
        }*/
        HourAdapter adapter = new HourAdapter(this, this);
        //TimeSlotAdapter adapter= new TimeSlotAdapter(this);
        //viewModel.getTimeSlots().observe(this, adapter::setTimeslots);
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

    /*private class TimeSlotAdapter extends BaseAdapter{

        private LayoutInflater layoutInflater;
        private List<Calendar> hourCalendars;
        private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        TimeSlotAdapter(Context context){
            hourCalendars = new LinkedList<>();
            layoutInflater = LayoutInflater.from(context);
        }

        private int getRelativeSliderPosition(Calendar current, Calendar beginning){
            int currentMinutes = current.get(Calendar.HOUR_OF_DAY)*60+current.get(Calendar.MINUTE);
            int earlyMinutes = beginning.get(Calendar.HOUR_OF_DAY)*60+beginning.get(Calendar.MINUTE);
            return currentMinutes-earlyMinutes;
        }

        private void setTimeSlider(){
            View timeslider = (View) findViewById(R.id.currentTimeSlider);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) timeslider.getLayoutParams();
            Calendar current = Calendar.getInstance();
            int sliderPosition = -1;
            if (hourCalendars != null && hourCalendars.size() > 0){

                sliderPosition = getRelativeSliderPosition(current, hourCalendars.get(0));
            }
            if (sliderPosition >= 0) {
                p.setMargins(0, sliderPosition * TimeTableViewModel.PIXEL_PER_MINUTE + titleHeight, 0, 0);
                timeslider.setVisibility(View.VISIBLE);
            }
            else
                timeslider.setVisibility(View.INVISIBLE);
            timeslider.requestLayout();
        }

        public void setTimeslots(List<Calendar> calendars){
            hourCalendars = calendars;
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
                convertView = layoutInflater.inflate(R.layout.component_timeslot_layout, null);

            if(hourCalendars != null && hourCalendars.size() >= position + 1) {
                RelativeLayout timeslotBlock = convertView.findViewById(R.id.timeslotBlock);
                TextView time = convertView.findViewById(R.id.timeslotText);

                Calendar calendar = hourCalendars.get(position);
                timeslotBlock.setBackgroundColor(Color.WHITE);
                time.setText(timeFormat.format(calendar.getTime()));

                int gridlinePosition = getRelativeSliderPosition(calendar, hourCalendars.get(0));
                if (gridlinePosition > 0){
                    View gridline = new View(findViewById(R.id.rlTimeTable).getContext());
                    gridline.setLayoutParams(new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.FILL_PARENT,
                            2
                    ));
                    gridline.setBackgroundColor(getResources().getColor(R.color.GREY));
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) gridline.getLayoutParams();
                    p.setMargins(0, gridlinePosition*TimeTableViewModel.PIXEL_PER_MINUTE+titleHeight, 0, 0);
                    gridline.requestLayout();
                    RelativeLayout timetable = (RelativeLayout) findViewById(R.id.rlTimeTable);
                    timetable.addView(gridline);
                }

                timeslotBlock.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 50));
                timeslotBlock.getLayoutParams().height = 60 * TimeTableViewModel.PIXEL_PER_MINUTE;
                timeslotBlock.getLayoutParams().width = RelativeLayout.LayoutParams.FILL_PARENT;
                setTimeSlider();
            }

            return convertView;
        }
    }*/

    /*private class SimpleAdapter extends BaseAdapter{

        private LayoutInflater layoutInflater;
        private List<Appointment> appointments;

        SimpleAdapter(Context context){
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
                convertView = layoutInflater.inflate(R.layout.component_timetable_entry_item, null);

            if(appointments != null && appointments.size() >= position + 1) {
                RelativeLayout timetableBlock = convertView.findViewById(R.id.timetableEntryBlock);
                TextView abbr = convertView.findViewById(R.id.timetableEntryAbbreviation);
                TextView time = convertView.findViewById(R.id.timetableEntryTime);
                TextView room = convertView.findViewById(R.id.timetableEntryRoom);

                Appointment appointment = appointments.get(position);
                timetableBlock.setBackgroundColor(Color.parseColor(appointment.getAppointmentContent().getColor()));

                time.setText(appointment.toTimeString());
                int color = Color.parseColor(TimeTableViewModel.getComplementaryColor(appointment.getAppointmentContent().getColor()));
                time.setTextColor(color);
                room.setText(appointment.getAppointmentContent().getRoom());
                room.setTextColor(color);
                abbr.setText(appointment.getAppointmentContent().getAbbreviation());
                abbr.setTextColor(color);

                timetableBlock.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 50));
                timetableBlock.getLayoutParams().height = appointment.getDurationBeforeMidnight() * TimeTableViewModel.PIXEL_PER_MINUTE;
                timetableBlock.getLayoutParams().width = RelativeLayout.LayoutParams.FILL_PARENT;
            }

            return convertView;
        }
    }*/

}