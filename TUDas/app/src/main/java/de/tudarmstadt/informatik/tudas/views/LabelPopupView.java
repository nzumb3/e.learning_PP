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

    public LabelPopupView(Context context){
        super(context);

        RelativeLayout popUpLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.component_manage_labels_popup, null) ;
        this.setContentView(popUpLayout);
        this.setFocusable(true);

        EditText labelInput = popUpLayout.findViewById(R.id.addLabel_input);
        Button labelButton = popUpLayout.findViewById(R.id.addLabel_button);

        labelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
