package de.tudarmstadt.informatik.tudas.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.localdatabase.daos.LabelDao;
import de.tudarmstadt.informatik.tudas.model.Label;
import de.tudarmstadt.informatik.tudas.viewmodels.ManageLabelsViewModel;

public class LabelListViewAdapter extends AbstractListAdapter<String> {

    private ManageLabelsViewModel viewModel;

    public LabelListViewAdapter(Context context, ManageLabelsViewModel viewModel){
        super(context);

        this.viewModel = viewModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null)
            convertView = layoutInflater.inflate(R.layout.component_label_list_item, null);
        if (list != null && list.size() >= position +1){
            String label = list.get(position);
            RelativeLayout entry = convertView.findViewById(R.id.rlLabels);
            entry.setOnClickListener((v) -> {
                AlertDialog dialog = createDialog(v, label);
                dialog.show();
            });
            TextView labelText = convertView.findViewById(R.id.labelText);
            labelText.setText(label);
        }
        return convertView;
    }

    private AlertDialog createDialog(View v, String label) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
        builder.setMessage("Do you really want to delete the label \"" + label + "\"?")
                .setTitle("Label Deletion")
                .setPositiveButton("Delete", ((dialog, which) -> viewModel.deleteLabel(label)))
                .setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));

        return builder.create();
    }
}
