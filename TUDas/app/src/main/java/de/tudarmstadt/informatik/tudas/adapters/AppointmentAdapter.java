package de.tudarmstadt.informatik.tudas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.tudarmstadt.informatik.tudas.DailyAppointmentsActivity;
import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;
import de.tudarmstadt.informatik.tudas.views.TimetablePopupView;

public class AppointmentAdapter extends AbstractListAdapter<Appointment> {

    private int PIXEL_PER_MINUTE;
    TimetablePopupView popup;
    Context context;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public AppointmentAdapter(Context context, int size, TimetablePopupView popup) {
        super(context);
        this.PIXEL_PER_MINUTE = size;
        this.popup = popup;
        this.context = context;
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
            timetableBlock.getLayoutParams().height = appointment.getDurationBeforeMidnight() * this.PIXEL_PER_MINUTE;
            timetableBlock.getLayoutParams().width = RelativeLayout.LayoutParams.FILL_PARENT;

            timetableBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView popTitle = popup.getContentView().findViewById(R.id.timetablePopupTitle);
                    popTitle.setText(appointment.getAppointmentContent().getTitle());
                    TextView popDescription = popup.getContentView().findViewById(R.id.timetablePopupDescription);
                    popDescription.setText(appointment.getAppointmentContent().getDescription());
                    TextView popRoom = popup.getContentView().findViewById(R.id.timetablePopupRoom);
                    String roomString = "<u>" + appointment.getAppointmentContent().getRoom() + "</u>";
                    popRoom.setText(Html.fromHtml(roomString));
                    popRoom.setTextColor(Color.parseColor("blue"));
                    popRoom.setOnClickListener((v1 -> {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + appointment.getAppointmentContent().getRoom() + "+TU+Darmstadt");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if(mapIntent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(mapIntent);
                        }
                    }));
                    TextView popStartTime = popup.getContentView().findViewById(R.id.timetablePopupStartTime);
                    TextView popEndTime = popup.getContentView().findViewById(R.id.timetablePopupEndTime);
                    popStartTime.setText(timeFormat.format(appointment.getStartDate().getTime()));
                    popEndTime.setText(timeFormat.format(appointment.getEndDate().getTime()));
                    Button deleteButton = popup.getContentView().findViewById(R.id.timetablePopupDeleteButton);
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    /* Known Bug: In newer android versions Gravity.CENTER not working -> PopupWindow is shown on left upper corner */
                    popup.showAtLocation(popup.getContentView(), Gravity.CENTER, 0, 0);
                    int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                    int height = Resources.getSystem().getDisplayMetrics().heightPixels;
                    popup.update((int) Math.round(width*0.75), (int) Math.round(height*0.6));
                }
            });
        }

        return convertView;
    }
}
