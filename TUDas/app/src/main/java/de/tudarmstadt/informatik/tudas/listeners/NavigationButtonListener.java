package de.tudarmstadt.informatik.tudas.listeners;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;


/*
* Listener responsible for opening and closing the navigation bar which can be used instead of
* swiping. It gets the drawer layout and open/closes the menu on press.
*/
public class NavigationButtonListener implements View.OnClickListener {

    private boolean drawerOpen;
    private DrawerLayout drawerLayout;

    public NavigationButtonListener(DrawerLayout drawerLayout){
        this.drawerOpen = false;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onClick(View v){
        if (drawerOpen){
            drawerOpen = false;
            drawerLayout.closeDrawers();
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
            drawerOpen = true;
        }
    }
}
