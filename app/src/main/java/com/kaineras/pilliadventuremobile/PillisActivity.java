package com.kaineras.pilliadventuremobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.HashMap;


public class PillisActivity extends ActionBarActivity implements menuFragment.OptionsMenuListener{


    PagerEnabledSlidingPaneLayout slidingPaneLayout;
    Tools tools=new Tools();
    HashMap<String,String > settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pillis);
        prepareToolbar();
        prepareSlide();
        settings=tools.getPreferences(this);
    }

    public void createAlert(String message)
    {
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
        slidingPaneLayout=(PagerEnabledSlidingPaneLayout)findViewById(R.id.sp);
        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

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

        switch (id)
        {
            case R.id.action_settings:
                Intent intent=new Intent(PillisActivity.this,SettingsActivity.class);
                startActivity(intent);
                handler=true;
                break;
            case android.R.id.home:
                if(slidingPaneLayout.isOpen()) {
                    closePane();
                }
                else
                {
                    openPane();
                }
                handler=true;
                break;
            default:
                handler=super.onOptionsItemSelected(item);
                break;
        }
        return handler;
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
    public void OptionsMenuListener(String optionMenu) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        switch (optionMenu) {
            case "PAGE":
                if (networkInfo != null && networkInfo.isConnected()) {
                    ComicFragment fragment=new ComicFragment();
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("PAGE",false);
                    fragment.setArguments(bundle);
                    tools.loadFragment(getSupportFragmentManager(),fragment,R.id.rightpane,"PAGE");
                } else {
                    createAlert(getString(R.string.text_check_connection));
                }
                break;
            case "COMIC":
                if (networkInfo != null && networkInfo.isConnected()) {
                    ComicFragment fragment=new ComicFragment();
                    Bundle bundle=new Bundle();
                    if(settings.get("save").equals("0")) {
                        bundle.putBoolean("PAGE",false);
                        fragment.setArguments(bundle);
                        tools.loadFragment(getSupportFragmentManager(), fragment, R.id.rightpane, "COMIC");
                    }
                    else {
                        bundle.putBoolean("PAGE",true);
                        fragment.setArguments(bundle);
                        tools.loadFragment(getSupportFragmentManager(), fragment, R.id.rightpane, "COMIC");
                    }
                } else {
                    createAlert(getString(R.string.text_check_connection));
                }
                break;
            case "ABOUT":
                if (networkInfo != null && networkInfo.isConnected()) {
                    tools.loadFragment(getSupportFragmentManager(), new AboutFragment(), R.id.rightpane, "ABOUT");
                } else {
                    createAlert(getString(R.string.text_check_connection));
                }

                break;
            case "CONTACT":
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pilliadv@hotmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_subject_email));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_body_mail));
                startActivity(Intent.createChooser(intent, getString(R.string.text_send_email)));
                break;
            default:
                break;
        }
    }

}
