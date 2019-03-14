package de.tudarmstadt.informatik.tudas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;
import de.tudarmstadt.informatik.tudas.views.DailyAppointmentPopupView;
import timber.log.Timber;

public class DailyAppointmentsListViewAdapter extends AbstractListAdapter<Appointment> {

    DailyAppointmentPopupView popUp;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public DailyAppointmentsListViewAdapter(Context context, DailyAppointmentPopupView popUp) {
        super(context);
        this.popUp = popUp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_daily_appointments_item, null);

        if (list != null && list.size() >= position + 1){
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
            start.setText(timeFormat.format(appointment.getStartDate().getTime()) + " - ");
            start.setTextColor(color);
            end.setText(timeFormat.format(appointment.getEndDate().getTime()));
            end.setTextColor(color);

            entry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView popTitle = popUp.getContentView().findViewById(R.id.dailyAppointmentPopupTitle);
                    popTitle.setText(appointment.getAppointmentContent().getTitle());
                    TextView popDescription = popUp.getContentView().findViewById(R.id.dailyAppointmentPopupDescription);
                    popDescription.setText(appointment.getAppointmentContent().getDescription());
                    popUp.showAtLocation(popUp.getContentView(), Gravity.CENTER, 0, 0);
                    //popUp.showAtLocation(popUp.getContentView(), Gravity.BOTTOM, 10, 10);
                    //popUp.update(20, 50, 300,300);
                    int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                    int height = Resources.getSystem().getDisplayMetrics().heightPixels;
                    popUp.update(width-40, height-300);
                }
            });
        }
        return convertView;
    }
}
