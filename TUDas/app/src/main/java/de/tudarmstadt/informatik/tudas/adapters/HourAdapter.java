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
import java.util.List;

import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.viewmodels.TimeTableViewModel;

public class HourAdapter extends AbstractListAdapter<Calendar> {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private AppCompatActivity activity;

    public HourAdapter(Context context, AppCompatActivity activity) {
        super(context);
        this.activity = activity;
    }

    private static int getRelativeSliderPosition(Calendar current, Calendar beginning){
        int currentMinutes = current.get(Calendar.HOUR_OF_DAY)*60+current.get(Calendar.MINUTE);
        int earlyMinutes = beginning.get(Calendar.HOUR_OF_DAY)*60+beginning.get(Calendar.MINUTE);

        return currentMinutes - earlyMinutes;
    }

    private void setTimeSlider() {
        View timeslider = (View) activity.findViewById(R.id.currentTimeSlider);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) timeslider.getLayoutParams();
        Calendar current = Calendar.getInstance();
        int sliderPosition = -1;
        if(list != null && list.size() > 0)
            sliderPosition = getRelativeSliderPosition(current, list.get(0));
        if (sliderPosition >= 0) {
            p.setMargins(0, sliderPosition * TimeTableViewModel.pixelPerMinute, 0, 0);
            timeslider.setVisibility(View.VISIBLE);
        }
        else
            timeslider.setVisibility(View.INVISIBLE);
        timeslider.requestLayout();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_timeslot_layout, null);

        if(list != null && list.size() >= position + 1) {
            RelativeLayout timeslotBlock = convertView.findViewById(R.id.timeslotBlock);
            TextView time = convertView.findViewById(R.id.timeslotText);

            Calendar calendar = list.get(position);
            timeslotBlock.setBackgroundColor(Color.WHITE);
            time.setText(timeFormat.format(calendar.getTime()));

            int gridlinePosition = getRelativeSliderPosition(calendar, list.get(0));
            if (gridlinePosition > 0){
                View gridline = new View(activity.findViewById(R.id.rlTimeTable).getContext());
                gridline.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        2
                ));
                gridline.setBackgroundColor(activity.getResources().getColor(R.color.GREY));
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) gridline.getLayoutParams();
                p.setMargins(0, gridlinePosition*TimeTableViewModel.pixelPerMinute, 0, 0);
                gridline.requestLayout();
                RelativeLayout timetable = (RelativeLayout) activity.findViewById(R.id.rlTimeTable);
                timetable.addView(gridline);
            }

            timeslotBlock.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 50));
            timeslotBlock.getLayoutParams().height = 60 * TimeTableViewModel.pixelPerMinute;
            timeslotBlock.getLayoutParams().width = RelativeLayout.LayoutParams.FILL_PARENT;
            setTimeSlider();
        }

        return convertView;
    }
}
