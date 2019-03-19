package de.tudarmstadt.informatik.tudas.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.tudarmstadt.informatik.tudas.DailyAppointmentsActivity;
import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;

public class AppointmentAdapter extends AbstractListAdapter<Appointment> {

    private int fontsize;

    public AppointmentAdapter(Context context) {
        super(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.fontsize = prefs.getInt("fontSize", 12);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_timetable_entry_item, null);

        if(list != null && list.size() >= position + 1) {
            RelativeLayout timetableBlock = convertView.findViewById(R.id.timetableEntryBlock);
            TextView abbr = convertView.findViewById(R.id.timetableEntryAbbreviation);
            TextView time = convertView.findViewById(R.id.timetableEntryTime);
            TextView room = convertView.findViewById(R.id.timetableEntryRoom);

            Appointment appointment = list.get(position);
            timetableBlock.setBackgroundColor(Color.parseColor(appointment.getAppointmentContent().getColor()));

            time.setText(appointment.toTimeString());
            int color = Color.parseColor(TimeTableViewModel.getComplementaryColor(appointment.getAppointmentContent().getColor()));
            time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, this.fontsize);
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
}
