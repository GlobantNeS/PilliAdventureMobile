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
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.kaineras.pilliadventuremobile.adapter.MyFragmentPagerAdapter;
import com.kaineras.pilliadventuremobile.pojo.ImagesProperties;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 */
public class
        ComicFragment extends Fragment {

    private Tools tools = new Tools();
    private ViewPager pager = null;
    private MyFragmentPagerAdapter adapter;
    private Map<String, String> settings;
    private String lastPageName;
    private View rootView;
    private boolean flagLastName = false;
    private List<String> comicsList;
    private Calendar calendar;
    private Calendar calendarLeft;
    private Calendar calendarRight;
    private static final String LOG_TAG = ComicFragment.class.getSimpleName();
    private static final  int PAGERS = 3;


    public ComicFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_comic, container, false);
        calendar = Calendar.getInstance();
        settings = tools.getPreferences(getActivity());
        hideToolbar();
        preparePager();
        boolean lastPage = !getArguments().getBoolean("PAGE");
        if (lastPage) {
            new UpdateComic().execute("last");
        }else{
            new UpdateComic().execute("update");
        }
        return rootView;
    }

    private void hideToolbar() {
    }

    void preparePager() {
        adapter = new MyFragmentPagerAdapter(getFragmentManager());

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(PAGERS - 1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(PAGERS-1==position && lastPageName.equals(comicsList.get(PAGERS-1))){
                    Toast.makeText(getActivity(), getString(R.string.text_last_page_comic), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    class UpdateComic extends AsyncTask<String, Void, Void> {

        List<String> resultComics = new ArrayList<>();
        ProgressDialog dialog;
        String language = getLanguage();

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
        protected Void doInBackground(String... params) {
            String varOpt=params[0];
            switch (varOpt){
                case "last":
                    getLastPagesFrom();
                    break;
                case "update":
                    break;
                default:
                    break;
            }
            return null;
        }

        private void getLastPagesFrom() {
            calendarRight = calendar;
            String dateImage = tools.calendarToString(calendar);
            try {
                int a = 0;
                do {
                    int statusImage = tools.existImageDB(getActivity(), dateImage, language);
                    switch (statusImage) {
                        case -1:
                            a = addComicToDB(dateImage, a);
                            break;
                        case 1:
                            a = addComicToList(dateImage, a);
                            break;
                        default:
                            break;
                    }
                    dateImage = tools.getYesterday(calendar);
                } while (a < PAGERS);
                calendarLeft = calendar;
            } catch (MalformedURLException e) {
                Log.w(LOG_TAG, e.toString());
                Logger.getLogger(ComicFragment.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        private int addComicToDB(String dateImage, int a) throws MalformedURLException {
            boolean imageExist = tools.existImageInUrl(tools.constructURLIma(language, dateImage + ".jpg"));
            if (imageExist) {
                a = addComicToList(dateImage, a);
            }
            ImagesProperties imagesProperties = new ImagesProperties();
            imagesProperties.setName(dateImage);
            imagesProperties.setLang(language);
            imagesProperties.setExist(imageExist ? "1" : "0");
            tools.saveImagePropertiesDB(getActivity(), imagesProperties);
            return a;
        }

        private int addComicToList(String dateImage, int a) {
            resultComics.add(dateImage);
            saveLastPageName(dateImage);
            a++;
            return a;
        }

        private void saveLastPageName(String dateImage) {
            if(!flagLastName) {
                lastPageName = dateImage;
                flagLastName=true;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            comicsList = Lists.reverse(resultComics);
            for (String name : comicsList) {
                try {
                    adapter.addFragment(ImageComicsViewFragment.newInstance(tools.constructURLIma(language, name + ".jpg").toString(), comicsList.indexOf(name)));
                } catch (MalformedURLException e) {
                    Log.w(LOG_TAG, e.toString());
                }
            }
            pager.setAdapter(adapter);
            pager.setCurrentItem(PAGERS - 1, true);
            dialog.dismiss();
        }
    }


    public String getLanguage() {
        return settings.get("language");
    }
}