package com.kaineras.pilliadventuremobile;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;
import com.kaineras.pilliadventuremobile.adapter.MyFragmentPagerAdapter;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 */
public class
        ComicFragment extends Fragment {

    private View v;
    private Map settings = new HashMap();
    Tools tools = new Tools();
    ViewPager pager = null;
    MyFragmentPagerAdapter adapter;
    final static private int PAGERS = 20;
    private final static String LOG_TAG = ComicFragment.class.getSimpleName();


    public ComicFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_comic, container, false);
        settings = tools.getPreferences(getActivity());
        preparePager();
        adapter = new MyFragmentPagerAdapter(getFragmentManager());
        boolean lastPage = !getArguments().getBoolean("PAGE");
        if (lastPage) {
            new UpdateComic().execute();
        }
        return v;
    }

    public void preparePager() {
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class UpdateComic extends AsyncTask<Void, Void, Void> {

        List<String> resultComics = new ArrayList<String>();
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.text_loading));
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Calendar calendar = Calendar.getInstance();
            String dateImage = tools.calendarToString(calendar);
            try {

                for (int a = PAGERS; a > 0; ) {
                    if (tools.existImage(tools.constructURLIma(" ", dateImage + ".jpg"))) {
                        resultComics.add(dateImage);
                        a--;
                    }
                    dateImage = tools.getYesterday(calendar);

                }
            } catch (MalformedURLException e) {
                Log.w(LOG_TAG, e.toString());
                Logger.getLogger(ComicFragment.class.getName()).log(Level.SEVERE, null, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            List<String> reverseList = Lists.reverse(resultComics);
            for (String name : reverseList)
                try {
                    adapter.addFragment(ImageComicsViewFragment.newInstance(tools.constructURLIma(" ", name + ".jpg").toString(), reverseList.indexOf(name)));
                } catch (MalformedURLException e) {
                    Log.w(LOG_TAG, e.toString());
                }
            pager.setAdapter(adapter);
            pager.setCurrentItem(PAGERS - 1, true);
            dialog.dismiss();
        }
    }
}