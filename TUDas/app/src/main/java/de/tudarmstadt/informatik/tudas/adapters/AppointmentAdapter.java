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


/*
* A listener, which handles the drawing of a column in the timetable(one day). The list contains the appointments
* for the corresponding day. With the PIXEL_PER_MINUTE field, the size of each appointment
* is computed along with its position on the screen. In addition, a Popupwindow is attached to each
* appointment to show up when it is clicked and shows additional information about the appointment.
* @param context: the current context of the listener, which is used to properly show the Popup
* @param popup: the view of the popup, since we do not need a distinct Popup for each appointment
* @param viewModel: the Viewmodel of the Acivity. Is used to enable the user to delete an appointment through the Popup
*/
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

    /*
    * Function handles the drawing of the appointments.
    * @param position: index in the list of an appointment to draw
    * @param convertView: The layout of an entry in the column
    * @param parent: The Listview, to which the appointment should be added.
    */
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

            // See if the current appointment is empty, which corresponds to filler entrys. If found set all text to emptry.
            if (appointment.getAppointmentContent().getTitle().equals("")){
                startTime.setText("");
                endTime.setText("");
                timeDivider.setText("");
            } else {
                startTime.setText(timeFormat.format(appointment.getStartDate().getTime()));
                endTime.setText(timeFormat.format(appointment.getEndDate().getTime()));

            }
            //Since the user can select a color of his choice, we need a text color w.r.t. the selected color. In our case the complementary color.
            int color = Color.parseColor(TimeTableViewModel.getComplementaryColor(appointment.getAppointmentContent().getColor()));
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

            //Prevent the filler entrys to show the popup with more infromation
            if (!appointment.getAppointmentContent().getTitle().equals("")) {
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
                        /*
                        * We want the user to be able to click the room to move to the google maps navigation
                        * which is set up here for the room text.
                        */
                        String roomString = "<u>" + appointment.getAppointmentContent().getRoom() + "</u>";
                        popRoom.setText(Html.fromHtml(roomString));
                        popRoom.setTextColor(Color.parseColor("blue"));
                        popRoom.setOnClickListener((v1 -> {
                            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + appointment.getAppointmentContent().getRoom() + "+TU+Darmstadt");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
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
                        popup.update((int) Math.round(width * 0.75), (int) Math.round(height * 0.6));
                    }
                });
            }
        }

        return convertView;
    }
}
