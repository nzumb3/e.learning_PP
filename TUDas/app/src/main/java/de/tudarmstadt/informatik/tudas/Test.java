package de.tudarmstadt.informatik.tudas;

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
import android.widget.TextView;

import java.util.List;

import de.tudarmstadt.informatik.tudas.model.AppointmentContentWithAppointments;

public class Test extends AppCompatActivity {

    private ListView listview;
    private ListView listview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setupUIViews();
        setupListView();
    }

    private void setupUIViews() {
        listview = (ListView) findViewById(R.id.lvToday);
        listview2 = (ListView) findViewById(R.id.lvTomorrow);
    }

    private void setupListView() {
        String[] title = getResources().getStringArray(R.array.Main);
        String[] description = getResources().getStringArray(R.array.Description);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, title, description);
        listview.setAdapter(simpleAdapter);
        title = getResources().getStringArray(R.array.Main2);
        description = getResources().getStringArray(R.array.Description2);
        SimpleAdapter simpleAdapter2 = new SimpleAdapter(this, title, description);
        listview2.setAdapter(simpleAdapter2);
    }

    public class SimpleAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater layoutInflater;
        private List<AppointmentContentWithAppointments> appointments;
        private ImageView imageView;

        public SimpleAdapter(Context context, List<AppointmentContentWithAppointments> appointments){
            mContext = context;
            this.appointments = appointments;
            layoutInflater = LayoutInflater.from(context);
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
                convertView = layoutInflater.inflate(R.layout.rect, null);

            TextView abbr = (TextView) convertView.findViewById(R.id.timetableEntryAbbreviation);
            TextView time = (TextView) convertView.findViewById(R.id.timetableEntryTime);
            TextView 

            /*ImageView timeSlot = (ImageView) convertView.findViewById(R.id.timeSlot);
            ImageView todaySlot = (ImageView) convertView.findViewById(R.id.todaySlot);
            ImageView tomorrowSlot = (ImageView) convertView.findViewById(R.id.tomorrowSlot);
            timeSlot.setImageResource(R.drawable.beers);
            todaySlot.setImageResource(R.drawable.beers);
            tomorrowSlot.setImageResource(R.drawable.beers);
            */
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

            return convertView;
        }
    }

}