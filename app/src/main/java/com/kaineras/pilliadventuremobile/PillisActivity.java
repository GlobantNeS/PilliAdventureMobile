package com.kaineras.pilliadventuremobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kaineras.pilliadventuremobile.custom.PagerEnabledSlidingPaneLayout;
import com.kaineras.pilliadventuremobile.services.AlarmReceiver;
import com.kaineras.pilliadventuremobile.settings.SettingsActivity;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.util.Map;


public class PillisActivity extends ActionBarActivity implements MenuFragment.OptionsMenuListener {


    private PagerEnabledSlidingPaneLayout slidingPaneLayout;
    private Tools tools = new Tools();
    private Map<String, String> settings;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cleanDB();
        stateOfConnectivity();
        setContentView(R.layout.activity_pillis);
        settings = tools.getPreferences(this);
        setAlarm();
        prepareToolbar();
        prepareSlide();
        loadNewsFragment(networkInfo);
    }

    private void cleanDB() {
        tools.cleanDB(this);
    }

    private void setAlarm() {
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        if("1".equals(settings.get("notif"))) {
            alarmReceiver.setAlarm(this);
        }else{
            alarmReceiver.cancelAlarm(this);
        }

    }

    private void stateOfConnectivity() {
        connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
    }

    public void createAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.text_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void prepareSlide() {
        slidingPaneLayout = (PagerEnabledSlidingPaneLayout) findViewById(R.id.sp);
        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Next Version add functionality
            }

            @Override
            public void onPanelOpened(View panel) {
                openPane();
            }

            @Override
            public void onPanelClosed(View panel) {
                closePane();
            }
        });
    }

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_image_dehaze);
        getSupportActionBar().setHomeButtonEnabled(true);
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
        boolean handler;

        switch (id) {
            case R.id.action_settings:
                loadSettings();
                handler = true;
                break;
            case android.R.id.home:
                updatePane();
                handler = true;
                break;
            default:
                handler = super.onOptionsItemSelected(item);
                break;
        }
        return handler;
    }

    private void updatePane() {
        if (slidingPaneLayout.isOpen()) {
            closePane();
        } else {
            openPane();
        }
    }

    private void loadSettings() {
        Intent intent = new Intent(PillisActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openPane() {
        slidingPaneLayout.openPane();
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.arrow_down_float);
    }

    private void closePane() {
        slidingPaneLayout.closePane();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_image_dehaze);
    }

    @Override
    public void optionsMenuListener(String optionMenu) {
        switch (optionMenu) {
            case "NEWS":
                loadNewsFragment(networkInfo);
                break;
            case "COMIC":
                loadComicFragment(networkInfo);
                break;
            case "ABOUT":
                loadAboutFragment(networkInfo);
                break;
            case "CONTACT":
                sendMail();
                break;
            default:
                break;
        }
    }

    private void loadAboutFragment(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isConnected()) {
            tools.loadFragment(getSupportFragmentManager(),R.id.rightpane, new AboutFragment());
        } else {
            createAlert(getString(R.string.text_check_connection));
        }
    }



    private void loadComicFragment(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isConnected()) {
            Intent intent = new Intent(PillisActivity.this,ComicsViewFullScreenActivity.class);
            startActivity(intent);
        } else {
            createAlert(getString(R.string.text_check_connection));
        }
    }

    public boolean getSaveState(){
        return !"0".equals(settings.get("save"));
    }


    private void loadNewsFragment(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isConnected()) {
            NewsFragment fragment = new NewsFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("PAGE", false);
            fragment.setArguments(bundle);
            tools.loadFragment(getSupportFragmentManager(),R.id.rightpane, fragment);
        } else {
            createAlert(getString(R.string.text_check_connection));
        }
    }

    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pilliadv@hotmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_subject_email));
        intent.putExtra(Intent.EXTRA_TEXT, settings.get("username")+":\n"+getString(R.string.text_body_mail));
        startActivity(Intent.createChooser(intent, getString(R.string.text_send_email)));
    }
}
