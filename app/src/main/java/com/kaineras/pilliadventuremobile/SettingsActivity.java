package com.kaineras.pilliadventuremobile;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created the first version by kaineras on 3/02/15.
 */
public class SettingsActivity extends ActionBarActivity {
    Tools t=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        t=new Tools();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        AppSettingsFragment appSettingsFragment=new AppSettingsFragment();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack("SETTINGS");
        fragmentTransaction.replace(R.id.pref_container, appSettingsFragment);
        fragmentTransaction.commit();
        //t.loadFragment(getSupportFragmentManager(),new AppSettingsFragment(),,"SETTINGS");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
