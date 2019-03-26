package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import de.tudarmstadt.informatik.tudas.adapters.LabelListViewAdapter;
import de.tudarmstadt.informatik.tudas.listeners.NavigationButtonListener;
import de.tudarmstadt.informatik.tudas.listeners.NavigationListener;
import de.tudarmstadt.informatik.tudas.model.Label;
import de.tudarmstadt.informatik.tudas.viewmodels.ManageLabelsViewModel;
import de.tudarmstadt.informatik.tudas.views.LabelPopupView;

public class ManageLabelsActivity extends AppCompatActivity {

    ManageLabelsViewModel viewModel;
    LabelPopupView popup;

    DrawerLayout drawerLayout;
    ListView manageLabelsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_labels);
        setSupportActionBar(findViewById(R.id.toolbarManageLabels));
        viewModel = ViewModelProviders.of(this).get(ManageLabelsViewModel.class);

        //Setup the listview
        manageLabelsListView = findViewById(R.id.lvLabels);
        LabelListViewAdapter adapter = new LabelListViewAdapter(this);
        viewModel.getLabels().observe(this, adapter::setList);
        manageLabelsListView.setAdapter(adapter);

        //Configure popup for inserting
        popup = new LabelPopupView(this);

        //Button for adding a label
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.showAtLocation(popup.getContentView(), Gravity.CENTER, 0, 0);
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                int height = Resources.getSystem().getDisplayMetrics().heightPixels;
                popup.update((int) Math.round(width*0.75), (int) Math.round(height*0.1));
            }
        });

        EditText labelInput = popup.getContentView().findViewById(R.id.addLabel_input);
        Button labelButton = popup.getContentView().findViewById(R.id.addLabel_button);

        labelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Label label = new Label();
                label.setName(labelInput.getText().toString());
                viewModel.validateLabel(label).observe(ManageLabelsActivity.this, (validation) -> {
                    if(validation != null && validation)
                        viewModel.insertLabel(label);
                });
                labelInput.setText("");
                popup.dismiss();
            }
        });

        //Setup the navigation bar
        drawerLayout = findViewById(R.id.drawerLayoutManageLabels);
        NavigationView navView = findViewById(R.id.nav_view);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navView.setNavigationItemSelectedListener(new NavigationListener(this, drawerLayout));
        Button navButton = findViewById(R.id.navButton_manageLabels);
        navButton.setOnClickListener(new NavigationButtonListener(drawerLayout));
    }
}
