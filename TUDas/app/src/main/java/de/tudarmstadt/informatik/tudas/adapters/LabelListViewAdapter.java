package de.tudarmstadt.informatik.tudas.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.model.Label;

public class LabelListViewAdapter extends AbstractListAdapter<String> {

    public LabelListViewAdapter(Context context){
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_label_list_item, null);
        if (list != null && list.size() >= position +1){
            String label = list.get(position);
            RelativeLayout entry = convertView.findViewById(R.id.rlLabels);
            TextView labelText = convertView.findViewById(R.id.labelText);
            labelText.setText(label);
        }
        return convertView;
    }
}
