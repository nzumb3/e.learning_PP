package de.tudarmstadt.informatik.tudas.listeners;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import de.tudarmstadt.informatik.tudas.DailyAppointmentsActivity;
import de.tudarmstadt.informatik.tudas.ManageLabelsActivity;
import de.tudarmstadt.informatik.tudas.R;
import de.tudarmstadt.informatik.tudas.SettingsActivity;
import de.tudarmstadt.informatik.tudas.TimeTableActivity;

/*
* Navigationbar Listener. Handles the navigation between the activities.
* @param context: used to start the next activity and is an instance of the currently active activity
* @param drawerLayout: the layout of the navigationbar, which should close when navigation starts
*/
public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private DrawerLayout drawerLayout;

    public NavigationListener(Context context, DrawerLayout drawerLayout){
        this.context = context;
        this.drawerLayout = drawerLayout;
    }

    /*
    * Handles the navigation. First it is checked, to where the user wants to navigate and also prevents
    * the user from navigation to the same activity, from which he comes from by just closing the navigation
    * bar.
    * @param menuItem: The clicked item in the menu. Used to determine the destination of the navigation.
    */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        drawerLayout.closeDrawers();
        switch (menuItem.getItemId()){
            case R.id.nav_dailyAppointments:
                if (!(context instanceof DailyAppointmentsActivity)){
                    Intent intent = new Intent(this.context, DailyAppointmentsActivity.class);
                    context.startActivity(intent);
                }
                break;
            case R.id.nav_settings:
                if (!(context instanceof SettingsActivity)){
                    Intent intent = new Intent(this.context, SettingsActivity.class);
                    context.startActivity(intent);
                }
                break;
            case R.id.nav_timeTable:
                if (!(context instanceof TimeTableActivity)){
                    Intent intent = new Intent(this.context, TimeTableActivity.class);
                    /*Flag is needed to let the user navigate to the timetable via the BACK button of the phone*/
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
                break;
            case R.id.nav_manageLabels:
                if (!(context instanceof ManageLabelsActivity)){
                    Intent intent = new Intent(this.context, ManageLabelsActivity.class);
                    context.startActivity(intent);
                }
                break;
        }
        return false;
    }
}
