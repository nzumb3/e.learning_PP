package de.tudarmstadt.informatik.tudas.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import de.tudarmstadt.informatik.tudas.R;

public class TimetablePopupView extends PopupWindow {

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
