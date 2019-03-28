package de.tudarmstadt.informatik.tudas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;

/*
* This adapter is responsible for drawing the hour boxes and also draws and
* updates a red line which symbolizes the current time inside of the timetable.
* @param context: the context of the adapter. Only used because super class needs it.
* @param activity: Used to get access to different elements of the activities layout.
* @param size: The size of the hour boxes, which can be set by the user in the settings, but needs to be forwarded, because the Adapter has no access to the SharedPreferences
*/
public class HourAdapter extends AbstractListAdapter<Calendar> {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private AppCompatActivity activity;
    private int PIXEL_PER_MINUTE;

    public HourAdapter(Context context, AppCompatActivity activity, int size) {
        super(context);
        this.activity = activity;
        this.PIXEL_PER_MINUTE = size;
    }

    /*
    * Computes the relative position of a timeslot with respect to another timeslot. Used for
    * arranging the elements of the layout.
    */
    private static int getRelativeSliderPosition(Calendar current, Calendar beginning){
        int currentMinutes = current.get(Calendar.HOUR_OF_DAY)*60+current.get(Calendar.MINUTE);
        int earlyMinutes = beginning.get(Calendar.HOUR_OF_DAY)*60+beginning.get(Calendar.MINUTE);

        return currentMinutes - earlyMinutes;
    }

    /*
    * Computes the position of the current time in the layout and sets the position of the timeslider
    * accordingly.
    */
    private void setTimeSlider() {
        View timeSlider = activity.findViewById(R.id.currentTimeSlider);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) timeSlider.getLayoutParams();
        Calendar current = Calendar.getInstance();
        int sliderPosition = -1;
        if(list != null && list.size() > 0)
            sliderPosition = getRelativeSliderPosition(current, list.get(0));
        if (sliderPosition >= 0) {
            p.setMargins(0, sliderPosition * this.PIXEL_PER_MINUTE, 0, 0);
            timeSlider.setVisibility(View.VISIBLE);
        }
        else
            timeSlider.setVisibility(View.INVISIBLE);
        timeSlider.requestLayout();
    }

    /*
    * This function is responsible to draw every timeslot in the correct form.
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_timeslot_layout, null);

        if(list != null && list.size() >= position + 1) {
            RelativeLayout timeSlotBlock = convertView.findViewById(R.id.timeslotBlock);
            TextView time = convertView.findViewById(R.id.timeslotText);
            RelativeLayout rLTimeTable = activity.findViewById(R.id.rlTimeTable);

            Calendar calendar = list.get(position);
            timeSlotBlock.setBackgroundColor(Color.WHITE);
            time.setText(timeFormat.format(calendar.getTime()));

            int gridlinePosition = getRelativeSliderPosition(calendar, list.get(0));
            if (gridlinePosition > 0){
                View gridline = new View(rLTimeTable.getContext());
                gridline.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        2
                ));
                gridline.setBackgroundColor(activity.getResources().getColor(R.color.GREY));
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) gridline.getLayoutParams();
                p.setMargins(0, gridlinePosition*this.PIXEL_PER_MINUTE, 0, 0);
                gridline.requestLayout();
                rLTimeTable.addView(gridline);
            }

            timeSlotBlock.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 50));
            timeSlotBlock.getLayoutParams().height = 60 * this.PIXEL_PER_MINUTE;
            timeSlotBlock.getLayoutParams().width = RelativeLayout.LayoutParams.FILL_PARENT;
            setTimeSlider();
        }

        return convertView;
    }
}
