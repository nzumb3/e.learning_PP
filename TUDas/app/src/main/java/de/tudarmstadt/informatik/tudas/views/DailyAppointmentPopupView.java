package de.tudarmstadt.informatik.tudas.views;

import android.widget.PopupWindow;

public class DailyAppointmentPopupView extends PopupWindow {

    boolean click = true;

    public boolean getClick(){
        return this.click;
    }

    public void setClick(boolean click){
        this.click = click;
    }
}
