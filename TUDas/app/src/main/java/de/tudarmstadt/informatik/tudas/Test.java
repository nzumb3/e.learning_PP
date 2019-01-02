package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import timber.log.Timber;

public class Test extends AppCompatActivity {

    private List<ListView> listViews;
    //private ListView listview2;
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
        viewModel.setEarliestBeginning(CalendarConverter.fromCalendar(startDate), CalendarConverter.fromCalendar(endDate)).observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(@Nullable Calendar calendar) {
                if(calendar != null)
                    Timber.d("MyLog: " + CalendarConverter.fromCalendar(calendar));
            }
        });
        listViews = new ArrayList<>();
        listViews.add((ListView) findViewById(R.id.lvToday));
        listViews.add((ListView) findViewById(R.id.lvTomorrow));

        /*listview = (ListView) findViewById(R.id.lvToday);
        listview2 = (ListView) findViewById(R.id.lvTomorrow);*/
    }

    private int getDaysBetweenStartAndEnd() {
        return (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis()) / 1000 / 60 / 60 / 24);
    }

    private void setupListView() {
        Calendar date = (Calendar) startDate.clone();
        for(int day = 0; day <= getDaysBetweenStartAndEnd(); day++) {
            SimpleAdapter simpleAdapter = new SimpleAdapter(this);
            viewModel.getAppointmentsForDay(CalendarConverter.toDateString(date)).observe(this, new Observer<List<Appointment>>() {
                @Override
                public void onChanged(@Nullable List<Appointment> appointments) {
                    Timber.d("MyLog: List for day: " + appointments);
                    simpleAdapter.setAppointments(appointments);
                }
            });
            listViews.get(day).setAdapter(simpleAdapter);
            date.add(Calendar.DATE, 1);
        }

        //final SimpleAdapter simpleAdapter = new SimpleAdapter(this);
        //viewModel.getAppointmentsInPeriod(CalendarConverter.fromCalendar(start), CalendarConverter.fromCalendar(end)).observe(this, (words) -> {simpleAdapter.setAppointments(words);});
        /*viewModel.getAppointmentsForView().observe(this, new Observer<List<Appointment>>() {

            private int count = 0;
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {
                count++;
                Timber.d("MyLog: Called for " + count + " times; appointments = " + appointments + " (" + (appointments != null ? appointments.size() : 0) + ") items");
            }
        });*/
        /*viewModel.getAppointmentsInPeriod(CalendarConverter.fromCalendar(start), CalendarConverter.fromCalendar(end)).observe(this, new Observer<List<Appointment>>() {

            private int count = 0;
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {
                count++;
                simpleAdapter.setAppointments(appointments);
                //Timber.d("MyLog: AppointmentsInPeriod called for " + count + " times; appointments = " + appointments + " (" + (appointments != null ? appointments.size() : 0) + ") items");
            }
        });
        //viewModel.getEarliestBeginningInPeriod(CalendarConverter.fromCalendar(start), CalendarConverter.fromCalendar(end)).observe(this, (calendar) -> {simpleAdapter.setEarliestBeginning(calendar);});
        listview.setAdapter(simpleAdapter);
        //SimpleAdapter simpleAdapter2 = new SimpleAdapter(this, title, description);
        listview2.setAdapter(simpleAdapter);*/
    }

    public class SimpleAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater layoutInflater;
        private List<Appointment> appointments;
        private ImageView imageView;
        //private Calendar earliestBeginning;

        public SimpleAdapter(Context context){
            mContext = context;
            this.appointments = new LinkedList<>();
            layoutInflater = LayoutInflater.from(context);
        }

        void setAppointments(List<Appointment> appointments){
            this.appointments = appointments;
            notifyDataSetChanged();
        }

        /*void setEarliestBeginning(Calendar earliestBeginning) {
            this.earliestBeginning = (Calendar) earliestBeginning.clone();
            this.earliestBeginning.set(Calendar.MINUTE, 0);
            notifyDataSetChanged();
        }*/

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

            if(/*earliestBeginning != null && */appointments != null && appointments.size() >= position + 1) {
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

            /*ImageView timeSlot = (ImageView) convertView.findViewById(R.id.timeSlot);
            ImageView todaySlot = (ImageView) convertView.findViewById(R.id.todaySlot);
            ImageView tomorrowSlot = (ImageView) convertView.findViewById(R.id.tomorrowSlot);
            timeSlot.setImageResource(R.drawable.beers);
            todaySlot.setImageResource(R.drawable.beers);
            tomorrowSlot.setImageResource(R.drawable.beers);

            title = (TextView) convertView.findViewById(R.id.tvMain);
            desciption = (TextView) convertView.findViewById(R.id.tvDescription);
            imageView = (ImageView) convertView.findViewById(R.id.ivMain);
            title.setText(titleArray[position]);
            desciption.setText(descriptionArray[position]);
            if (titleArray[position].equalsIgnoreCase("Timetable")){
                imageView.setImageResource(R.drawable.beers);
            } else if (titleArray[position].equalsIgnoreCase("Subjects")) {
                imageView.setImageResource(R.drawable.beers);
            } else if (titleArray[position].equalsIgnoreCase("Faculty")) {
                imageView.setImageResource(R.drawable.beers);
            } else {
                imageView.setImageResource(R.drawable.beers);
            }
            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            Log.d("Blubb Bluee", Boolean.toString(layoutParams==null));
            if (position%2==0){
                //layoutParams.setMargins(10, 50, 10, 0);
                //convertView.setLayoutParams(layoutParams);
            }
            */
            return convertView;
        }
    }

}