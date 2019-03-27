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
    TimeTableViewModel viewModel;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public AppointmentAdapter(Context context, int size, TimetablePopupView popup, TimeTableViewModel viewModel) {
        super(context);
        this.PIXEL_PER_MINUTE = size;
        this.popup = popup;
        this.context = context;
        this.viewModel = viewModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_timetable_entry_item, null);

        if(list != null && list.size() >= position + 1) {
            RelativeLayout timetableBlock = convertView.findViewById(R.id.timetableEntryBlock);
            TextView abbr = convertView.findViewById(R.id.timetableEntryAbbreviation);
            TextView startTime = convertView.findViewById(R.id.timetableEntryStartTime);
            TextView endTime = convertView.findViewById(R.id.timetableEntryEndtime);
            TextView timeDivider = convertView.findViewById(R.id.timetableEntryTimeDivider);
            TextView room = convertView.findViewById(R.id.timetableEntryRoom);

            Appointment appointment = list.get(position);
            timetableBlock.setBackgroundColor(Color.parseColor(appointment.getAppointmentContent().getColor()));

            startTime.setText(timeFormat.format(appointment.getStartDate().getTime()));
            endTime.setText(timeFormat.format(appointment.getEndDate().getTime()));
            //time.setText(appointment.toTimeString());
            int color = Color.parseColor(TimeTableViewModel.getComplementaryColor(appointment.getAppointmentContent().getColor()));
            //time.setTextColor(color);
            startTime.setTextColor(color);
            endTime.setTextColor(color);
            timeDivider.setTextColor(color);
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
                    popTitle.setTextColor(context.getResources().getColor(R.color.BLACK));
                    TextView popDescription = popup.getContentView().findViewById(R.id.timetablePopupDescription);
                    popDescription.setText(appointment.getAppointmentContent().getDescription());
                    popDescription.setTextColor(context.getResources().getColor(R.color.BLACK));
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
                    TextView popTimeDivider = popup.getContentView().findViewById(R.id.timetablePopupTimeDivider);
                    popStartTime.setText(timeFormat.format(appointment.getStartDate().getTime()));
                    popEndTime.setText(timeFormat.format(appointment.getEndDate().getTime()));
                    popStartTime.setTextColor(context.getResources().getColor(R.color.BLACK));
                    popEndTime.setTextColor(context.getResources().getColor(R.color.BLACK));
                    popTimeDivider.setTextColor(context.getResources().getColor(R.color.BLACK));
                    Button deleteButton = popup.getContentView().findViewById(R.id.timetablePopupDeleteButton);
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.deleteAppointment(appointment);
                            popup.dismiss();
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
