package de.tudarmstadt.informatik.tudas.listeners;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import de.tudarmstadt.informatik.tudas.DailyAppointmentsActivity;
import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.SettingsActivity;
import de.tudarmstadt.informatik.tudas.TimeTableActivity;
import timber.log.Timber;

public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private DrawerLayout drawerLayout;

    public NavigationListener(Context context, DrawerLayout drawerLayout){
        this.context = context;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        drawerLayout.closeDrawers();
        switch (menuItem.getItemId()){
            case R.id.nav_dailyAppointments:
                if (!(context instanceof DailyAppointmentsActivity)){
                    Intent intent = new Intent(this.context, DailyAppointmentsActivity.class);
                    context.startActivity(intent);
                    //((Activity)context).finish();
                }
                break;
            case R.id.nav_settings:
                if (!(context instanceof SettingsActivity)){
                    Intent intent = new Intent(this.context, SettingsActivity.class);
                    context.startActivity(intent);
                    //((Activity)context).finish();
                }
                break;
            case R.id.nav_timeTable:
                if (!(context instanceof TimeTableActivity)){
                    Intent intent = new Intent(this.context, TimeTableActivity.class);
                    context.startActivity(intent);
                    //((Activity)context).finish();
                }
        }
        return false;
    }
}
