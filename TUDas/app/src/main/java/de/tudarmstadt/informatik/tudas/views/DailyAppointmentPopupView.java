package de.tudarmstadt.informatik.tudas.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.mapbox.mapboxsdk.storage.Resource;

import de.tudarmstadt.informatik.tudas.R;

public class DailyAppointmentPopupView extends PopupWindow {

    public DailyAppointmentPopupView(Context context) {
        super(context);

        RelativeLayout popUpLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.component_daily_appointment_popup, null);
        popUpLayout.setBackgroundColor(context.getResources().getColor(R.color.popupBackground));
        popUpLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DailyAppointmentPopupView.this.dismiss();
                return true;
            }
        });
        this.setContentView(popUpLayout);
        this.setFocusable(true);
    }
}
