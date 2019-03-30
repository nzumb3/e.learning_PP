package de.tudarmstadt.informatik.tudas.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import de.tudarmstadt.informatik.tudas.R;

public class LabelPopupView extends PopupWindow {

    /*
    * Setup the popup to subscribe to a label.
    */
    public LabelPopupView(Context context){
        super(context);
        RelativeLayout popUpLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.component_manage_labels_popup, null) ;
        this.setContentView(popUpLayout);
        this.setFocusable(true);
    }
}
