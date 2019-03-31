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
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;
import de.tudarmstadt.informatik.tudas.views.DailyAppointmentPopupView;
import timber.log.Timber;


/*
* Listener for the daily appointment listview. It handles the drawing of each appointment for the current day.
* Each appointment is show with fixed size for a better overview. In addition, each appointment can be
* clicked for further information, which is shown as a Popup. Inside of the Popup, the user can use a link
* on the room to start the google maps navigation to this room. Each entry has a flag in form of a lightning
* which sybolizes an overlap with a private calendar appointment.
* @param context: the context of the app the popup is used in. Is needed for starting new activities and getting some ressources.
* @param popUp: The view of the popup, which is used multiple times for different appointments
*/
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

    /*
    * The function is responsible for rendering each appointment in the ListView of the DailyAppointmentsActivity.
    *
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_daily_appointments_item, null);

        if (list != null && list.size() >= position + 1){
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
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

            Timber.d("MyLog: " + appointment.getAppointmentContent().getTitle());
            //Check if the appointment overlaps with another appointment and show a hint.
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

            //Setup the popup window for the appointment with the corresponding text.
            entry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView popTitle = popUp.getContentView().findViewById(R.id.dailyAppointmentPopupTitle);
                    popTitle.setText(appointment.getAppointmentContent().getTitle());
                    TextView popDescription = popUp.getContentView().findViewById(R.id.dailyAppointmentPopupDescription);
                    popDescription.setText(appointment.getAppointmentContent().getDescription());
                    TextView popRoom = popUp.getContentView().findViewById(R.id.dailyAppointmentPopupRoom);
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
                    TextView popStartTime = popUp.getContentView().findViewById(R.id.dailyAppointmentPopupStartTime);
                    TextView popEndTime = popUp.getContentView().findViewById(R.id.dailyAppointmentPopupEndTime);
                    TextView popTimeDivider = popUp.getContentView().findViewById(R.id.dailyAppointmentPopupTimeDivider);
                    popStartTime.setText(timeFormat.format(appointment.getStartDate().getTime()));
                    popEndTime.setText(timeFormat.format(appointment.getEndDate().getTime()));

                    popDescription.setTextColor(context.getResources().getColor(R.color.BLACK));
                    popEndTime.setTextColor(context.getResources().getColor(R.color.BLACK));
                    popStartTime.setTextColor(context.getResources().getColor(R.color.BLACK));
                    popTimeDivider.setTextColor(context.getResources().getColor(R.color.BLACK));
                    popTitle.setTextColor(context.getResources().getColor(R.color.BLACK));
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
