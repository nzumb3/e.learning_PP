package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tudas.model.AppointmentContentWithAppointments;
import de.tudarmstadt.informatik.tudas.model.AppointmentViewModel;
import de.tudarmstadt.informatik.tudas.model.CalendarConverter;

public class Test extends AppCompatActivity {

    private ListView listview;
    private ListView listview2;
    private AppointmentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);
        setContentView(R.layout.activity_test);
        setupUIViews();
        setupListView();
    }

    private void setupUIViews() {
        listview = (ListView) findViewById(R.id.lvToday);
        listview2 = (ListView) findViewById(R.id.lvTomorrow);
    }

    private void setupListView() {
        Calendar start = Calendar.getInstance();
        start.set(2018, 12, 24, 0, 0);

        Calendar end = Calendar.getInstance();
        end.set(2018, 12, 26, 23, 59);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this);
        viewModel.getAppointmentsInPeriod(CalendarConverter.fromCalendar(start), CalendarConverter.fromCalendar(end)).observe(this, (words) -> {simpleAdapter.setAppointments(words);});
        listview.setAdapter(simpleAdapter);
        //SimpleAdapter simpleAdapter2 = new SimpleAdapter(this, title, description);
        listview2.setAdapter(simpleAdapter);
    }

    public class SimpleAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater layoutInflater;
        private List<AppointmentContentWithAppointments> appointments;
        private ImageView imageView;

        public SimpleAdapter(Context context){
            mContext = context;
            this.appointments = new LinkedList<>();
            layoutInflater = LayoutInflater.from(context);
        }

        void setAppointments(List<AppointmentContentWithAppointments> appointments){
            Log.d("AppointmentsLength", Integer.toString(appointments.size()));
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

            Log.d("Zugriff", "Size = " + appointments.size() + ", Position = " + position);
            if(appointments != null && appointments.size() >= position + 1) {
                RelativeLayout timetableBlock = convertView.findViewById(R.id.timetableEntryBlock);
                TextView abbr = convertView.findViewById(R.id.timetableEntryAbbreviation);
                TextView time = convertView.findViewById(R.id.timetableEntryTime);
                TextView room = convertView.findViewById(R.id.timetableEntryRoom);
                AppointmentContentWithAppointments appointment = appointments.get(position);
                time.setText(appointment.getAppointments().get(0).toTimeString());
                room.setText(appointment.getContent().getRoom());
                abbr.setText(appointment.getContent().getAbbreviation());
                timetableBlock.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 50));
                timetableBlock.getLayoutParams().height = appointment.getAppointments().get(0).getDurationBeforeMidnight()*AppointmentViewModel.pixelPerMinute;
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