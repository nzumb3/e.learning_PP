package de.tudarmstadt.informatik.tudas.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
        this.setContentView(popUpLayout);
        this.setFocusable(true);
        //int widht = Resources.getSystem().getDisplayMetrics().widthPixels;
        //int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        //this.update(widht-20, height-20);
    }
}
