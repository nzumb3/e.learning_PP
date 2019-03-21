package de.tudarmstadt.informatik.tudas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.tudarmstadt.informatik.tudas.DailyAppointmentsActivity;
import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;
import de.tudarmstadt.informatik.tudas.views.DailyAppointmentPopupView;
import timber.log.Timber;

public class DailyAppointmentsListViewAdapter extends AbstractListAdapter<Appointment> {

    DailyAppointmentPopupView popUp;
    Context context;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public DailyAppointmentsListViewAdapter(Context context, DailyAppointmentPopupView popUp) {
        super(context);
        this.popUp = popUp;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_daily_appointments_item, null);

        if (list != null && list.size() >= position + 1){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
            Appointment appointment = list.get(position);
            RelativeLayout entry = convertView.findViewById(R.id.rlDailyAppointment);
            TextView title = convertView.findViewById(R.id.dailyAppointmentTitle);
            TextView abbr = convertView.findViewById(R.id.dailyAppointmentAbbrevation);
            TextView room = convertView.findViewById(R.id.dailyAppointmentRoom);
            TextView start = convertView.findViewById(R.id.tvAppointmentStartTime);
            TextView end = convertView.findViewById(R.id.tvAppointmentEndTime);
            ImageView overlapInfo = convertView.findViewById(R.id.ivOverlapInfo);
            entry.setBackgroundColor(Color.parseColor(appointment.getAppointmentContent().getColor()));
            int color = Color.parseColor(TimeTableViewModel.getComplementaryColor(appointment.getAppointmentContent().getColor()));

            if(appointment.overlap()) {
                overlapInfo.setVisibility(View.VISIBLE);
                overlapInfo.setColorFilter(color);
            }
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
                    /* Known Bug: In newer android versions Gravity.CENTER not working -> PopupWindow is shown on left upper corner */
                    popUp.showAtLocation(popUp.getContentView(), Gravity.CENTER, 0, 0);
                    int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                    int height = Resources.getSystem().getDisplayMetrics().heightPixels;
                    popUp.update((int) Math.round(width*0.75), (int) Math.round(height*0.6));
                }
            });
        }
        return convertView;
    }
}
