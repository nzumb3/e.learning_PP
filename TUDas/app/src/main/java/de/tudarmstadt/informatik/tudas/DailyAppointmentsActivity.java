package de.tudarmstadt.informatik.tudas;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.tudarmstadt.informatik.tudas.adapters.DailyAppointmentsListViewAdapter;
import de.tudarmstadt.informatik.tudas.listeners.NavigationButtonListener;
import de.tudarmstadt.informatik.tudas.listeners.NavigationListener;
import de.tudarmstadt.informatik.tudas.viewmodels.DailyAppointmentsViewModel;
import de.tudarmstadt.informatik.tudas.views.DailyAppointmentPopupView;
import timber.log.Timber;


/*
* Entry point of TuDas. The Activity, which shows every appointment for the current day.
* The View consists of a list of all appointments, which are due on the current day. Each appointment
* is shown in a box which opens a popup window when clicked and provieds additional information about
* the appointment and also enables the user to get navigated to the google maps app for navigation to
* the room.
*/
public class DailyAppointmentsActivity extends AppCompatActivity {

    DailyAppointmentsViewModel viewModel;
    ListView dailyAppointmentsListView;
    DrawerLayout drawerLayout;

    DailyAppointmentPopupView popUp;

    /*
    * On creation, the interface of the layout is initialized and the default preferences are set
    * on the first start of the app. Also Tudas needs access to other calendar apps to check for
    * overlaps, which is also handled on creation. Lastly the navigation bar is initialized.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_daily_appointments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDailyAppointments);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(DailyAppointmentsActivity.this, R.xml.preferences, false);
        viewModel = ViewModelProviders.of(this).get(DailyAppointmentsViewModel.class);
        viewModel.getCurrentDate().observe(this, (calendar -> {
            if(calendar != null) {
                TextView dateTextView = findViewById(R.id.tvTimeFrame);
                SimpleDateFormat format = new SimpleDateFormat("dd. MMM yyyy");
                dateTextView.setText(format.format(calendar.getTime()));
            }
        }));

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
        else
            viewModel.setPermissionStatus(PackageManager.PERMISSION_GRANTED);

        drawerLayout = findViewById(R.id.drawerLayoutDailyAppointments);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationListener(this, drawerLayout));
        ImageButton navButton = findViewById(R.id.navButton_dailyAppointments);
        navButton.setOnClickListener(new NavigationButtonListener(drawerLayout));
    }

    /*
    * Every time the user returns to the activity, the list of appointments is updated. It is also checked
    * if there are any appointments due today. If not, a special message is shown instead of the listview.
    */
    @Override
    protected void onResume() {
        super.onResume();
        popUp = new DailyAppointmentPopupView(this);

        viewModel.getInformationCode().observe(this, (code) -> {
            if(code != null) {
                switch (code) {
                    case 1:
                        findViewById(R.id.tvNoAppointments).setVisibility(View.VISIBLE);
                        findViewById(R.id.tvNoAppointmentsNoLists).setVisibility(View.INVISIBLE);
                        findViewById(R.id.lvDailyAppointments).setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        findViewById(R.id.tvNoAppointments).setVisibility(View.INVISIBLE);
                        findViewById(R.id.tvNoAppointmentsNoLists).setVisibility(View.VISIBLE);
                        findViewById(R.id.lvDailyAppointments).setVisibility(View.INVISIBLE);
                        break;
                    default:
                        findViewById(R.id.tvNoAppointments).setVisibility(View.INVISIBLE);
                        findViewById(R.id.tvNoAppointmentsNoLists).setVisibility(View.INVISIBLE);
                        findViewById(R.id.lvDailyAppointments).setVisibility(View.VISIBLE);
                }
            }
        });
        dailyAppointmentsListView = findViewById(R.id.lvDailyAppointments);
        DailyAppointmentsListViewAdapter adapter = new DailyAppointmentsListViewAdapter(this, popUp);
        viewModel.getAppointmentsForDay().observe(this, adapter::setList);
        dailyAppointmentsListView.setAdapter(adapter);

    }
}