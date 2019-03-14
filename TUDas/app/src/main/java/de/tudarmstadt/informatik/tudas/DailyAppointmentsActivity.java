package de.tudarmstadt.informatik.tudas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

import de.tudarmstadt.informatik.tudas.adapters.DailyAppointmentsListViewAdapter;
import de.tudarmstadt.informatik.tudas.listeners.NavigationButtonListener;
import de.tudarmstadt.informatik.tudas.listeners.NavigationListener;
import de.tudarmstadt.informatik.tudas.utils.CalendarConverter;
import de.tudarmstadt.informatik.tudas.viewmodels.DailyAppointmentsViewModel;
import de.tudarmstadt.informatik.tudas.views.DailyAppointmentPopupView;
import timber.log.Timber;

public class DailyAppointmentsActivity extends AppCompatActivity {

    DailyAppointmentsViewModel viewModel;
    ListView dailyAppointmentsListView;
    Calendar startDate;
    DrawerLayout drawerLayout;

    DailyAppointmentPopupView popUp;
    RelativeLayout popUpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_daily_appointments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDailyAppointments);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(DailyAppointmentsViewModel.class);
        dailyAppointmentsListView = findViewById(R.id.lvDailyAppointments);
        startDate = Calendar.getInstance();
        startDate.set(2019, Calendar.MARCH, 15, 0, 0, 0);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DailyAppointmentsActivity.this);

        popUp = new DailyAppointmentPopupView(this);

        DailyAppointmentsListViewAdapter adapter = new DailyAppointmentsListViewAdapter(this, popUp);
        viewModel.getAppointmentsForDay(CalendarConverter.toDateString(startDate)).observe(this, adapter::setList);
        dailyAppointmentsListView.setAdapter(adapter);

        //popUpLayout = findViewById(R.id.dailyAppointmentPopup);//new LinearLayout(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DailyAppointmentsActivity.this);
                Timber.d("MyLog: Shared Prefs: " + prefs.getAll().toString());
                String loc = Locale.getDefault().getDisplayCountry();
                Timber.d("MyLog: Current Locale -> " + loc);*/
                /*if (clicked) {
                    popUp.showAtLocation(popUpLayout, Gravity.BOTTOM, 10, 10);
                    popUp.update(20, 50, 300, 300);
                    clicked = false;
                } else {
                    popUp.dismiss();
                    clicked = true;
                }*/
            }
        });

        //params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //popUpLayout.setOrientation(LinearLayout.VERTICAL);
        //popUpText.setText("Example Popup!!!! YAY! I guess...");
        //popUpLayout.addView(popUpText, params);
        //popUp.setContentView(popUpLayout);

        drawerLayout = findViewById(R.id.drawerLayoutDailyAppointments);
        NavigationView navView = findViewById(R.id.nav_view);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navView.setNavigationItemSelectedListener(new NavigationListener(this, drawerLayout));

        Button navButton = findViewById(R.id.navButton_dailyAppointments);
        navButton.setOnClickListener(new NavigationButtonListener(drawerLayout));

    }
}