package de.tudarmstadt.informatik.tudas.adapters;

import android.content.Context;
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

public class DailyAppointmentsListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Appointment> appointments;

    public DailyAppointmentsListViewAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    private void setAppointments(List<Appointment> appointments){
        this.appointments = appointments;
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
            convertView = layoutInflater.inflate(R.layout.component_daily_appointments_item, null);

        if (appointments != null && appointments.size() >= position + 1){
            RelativeLayout entry = convertView.findViewById(R.id.rlDailyAppointment);
            //TODO
        }
        return convertView;
    }
}
