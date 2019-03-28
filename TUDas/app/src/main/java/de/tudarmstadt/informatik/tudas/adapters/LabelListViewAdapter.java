package de.tudarmstadt.informatik.tudas.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.viewmodels.ManageLabelsViewModel;


/*
* Adapter which manages the view of the subscribed labels by the user.
* @param viewModel: Is needed to allow the user to unsubscribe to a label.
*/
public class LabelListViewAdapter extends AbstractListAdapter<String> {

    private ManageLabelsViewModel viewModel;

    public LabelListViewAdapter(Context context, ManageLabelsViewModel viewModel){
        super(context);

        this.viewModel = viewModel;
    }

    /*
    * Display all subscribed labels in a listview. And add a dialog, which lets the user unsubscribe
    * from a label.
    */
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

    /*
    * Setup of the unsubscribe dialog.
    */
    private AlertDialog createDialog(View v, String label) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
        builder.setMessage(R.string.confirm_label_deletion + " \"" + label + "\"?")
                .setTitle(R.string.label_deletion)
                .setPositiveButton(R.string.popupDeleteButton, ((dialog, which) -> viewModel.deleteLabel(label)))
                .setNegativeButton(R.string.cancel, ((dialog, which) -> dialog.cancel()));

        return builder.create();
    }
}
