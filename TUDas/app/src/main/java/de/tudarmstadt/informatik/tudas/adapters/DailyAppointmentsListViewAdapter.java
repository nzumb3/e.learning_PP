package de.tudarmstadt.informatik.tudas.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;

public class DailyAppointmentsListViewAdapter extends AbstractListAdapter<Appointment> {

    private LayoutInflater layoutInflater;
    private List<Appointment> appointments;

    public DailyAppointmentsListViewAdapter(Context context) {
        super(context);
    }

    public void setAppointments(List<Appointment> appointments){
        this.appointments = appointments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_daily_appointments_item, null);

        if (appointments != null && appointments.size() >= position + 1){
            Appointment appointment = list.get(position);
            RelativeLayout entry = convertView.findViewById(R.id.rlDailyAppointment);
            TextView title = convertView.findViewById(R.id.dailyAppointmentTitle);
            TextView abbr = convertView.findViewById(R.id.dailyAppointmentAbbrevation);
            TextView room = convertView.findViewById(R.id.dailyAppointmentRoom);
            TextView start = convertView.findViewById(R.id.tvAppointmentStartTime);
            TextView end = convertView.findViewById(R.id.tvAppointmentEndTime);
            entry.setBackgroundColor(Color.parseColor(appointment.getAppointmentContent().getColor()));
            int color = Color.parseColor(TimeTableViewModel.getComplementaryColor(appointment.getAppointmentContent().getColor()));
            title.setText(appointment.getAppointmentContent().getTitle());
            title.setTextColor(color);
            abbr.setText(appointment.getAppointmentContent().getAbbreviation());
            abbr.setTextColor(color);
            room.setText(appointment.getAppointmentContent().getRoom());
            room.setTextColor(color);
            start.setText(appointment.getStartDate().getTime().toString());
            start.setTextColor(color);
            end.setText(appointment.getEndDate().getTime().toString());
            end.setTextColor(color);
        }
        return convertView;
    }
}
