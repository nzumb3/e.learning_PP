package de.tudarmstadt.informatik.tudas.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import de.tudarmstadt.informatik.tudas.R;

public class TimetablePopupView extends PopupWindow {

    /*
    * Setup the layout of the popup window, which displays the detailed informations of an appointment.
    * The window closes, if the user touches an area anywhere on the screen.
    */
    public TimetablePopupView(Context context) {
        super(context);
        RelativeLayout popUpLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.component_timetable_popup, null);
        popUpLayout.setBackgroundColor(context.getResources().getColor(R.color.popupBackground));
        popUpLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TimetablePopupView.this.dismiss();
                return true;
            }
        });
        this.setContentView(popUpLayout);
        this.setFocusable(true);
    }
}
