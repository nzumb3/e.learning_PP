package de.tudarmstadt.informatik.tudas.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;

public class AppointmentAdapter extends AbstractListAdapter<Appointment> {

    public AppointmentAdapter(Context context) {
        super(context);
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
            time.setTextColor(color);
            room.setText(appointment.getAppointmentContent().getRoom());
            room.setTextColor(color);
            abbr.setText(appointment.getAppointmentContent().getAbbreviation());
            abbr.setTextColor(color);

            timetableBlock.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 50));
            timetableBlock.getLayoutParams().height = appointment.getDurationBeforeMidnight() * TimeTableViewModel.pixelPerMinute;
            timetableBlock.getLayoutParams().width = RelativeLayout.LayoutParams.FILL_PARENT;
        }

        return convertView;
    }
}
