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
import com.kaineras.pilliadventuremobile.custom.CustomImageView;
import com.kaineras.pilliadventuremobile.pojo.ImagesProperties;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.net.MalformedURLException;
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
    private String dateImage;
    private View rootView;
    private boolean flagLastName = false;
    private List<String> comicsList;
    private Calendar calendar;
    private Calendar calendarLeft;
    private Calendar calendarRight;
    private int a;
    private static final int INT_BASE = 77777;
    private static final String LOG_TAG = ComicFragment.class.getSimpleName();
    private static final  int PAGERS = 3;
    private static final  int FIRST_PAGE = 0;
    private static final  int LAST_PAGE = 4;
    private static final  int REAL_PAGERS = 5;


    public ComicFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_comic, container, false);
        calendar = Calendar.getInstance();
        calendarLeft = calendarRight = calendar;
        settings = tools.getPreferences(getActivity());
        preparePager();
        boolean lastPage = !getArguments().getBoolean("PAGE");
        if (lastPage) {
            new UpdateComic().execute("last");
        }else{
            new UpdateComic().execute("update");
        }
        return rootView;
    }

    void preparePager() {
        adapter = new MyFragmentPagerAdapter(getFragmentManager());

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(REAL_PAGERS - 1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //FUNCTIONALITY IN NEXT FEATURES
            }
            @Override
            public void onPageSelected(int position) {
                if(PAGERS == position && lastPageName.equals(comicsList.get(PAGERS))){
                    Toast.makeText(getActivity(), getString(R.string.text_last_page_comic), Toast.LENGTH_SHORT).show();
                }else {
                    checkComicUpdates(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //FUNCTIONALITY IN NEXT FEATURES
            }
        });
    }

    private void checkComicUpdates(int position) {
        if (position == FIRST_PAGE) {
            new UpdateComic().execute("updateLeft");
        }else {
            if (position == LAST_PAGE && lastPageName.equals(comicsList.get(PAGERS))) {
                Toast.makeText(getActivity(), getString(R.string.text_last_page_comic), Toast.LENGTH_SHORT).show();
                pager.setCurrentItem(PAGERS, false);
            } else {
                if (position == LAST_PAGE) {
                    new UpdateComic().execute("updateRight");
                }
            }
        }
    }

    class UpdateComic extends AsyncTask<String, Void, Void> {

        List<String> resultComics = new ArrayList<>();
        ProgressDialog dialog;
        String language = getLanguage();
        int mode=0;

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
                    getLastPagesFrom(calendar,0);
                    break;
                case "updateLeft":
                    getLastPagesFrom(calendarLeft,0);
                    break;
                case "updateRight":
                    getLastPagesFrom(calendarRight,1);
                    break;
                default:
                    break;
            }
            return null;
        }

        private void getLastPagesFrom(Calendar tempCalendar,int mode) {
            this.mode=mode;
            a=0;
            dateImage = tools.calendarToString(tempCalendar);
            resultComics.clear();
            resultComics.add("FakePageAfter");
            try {
                if(mode == 1){
                    updateRightComics(tempCalendar);
                }else{
                    updateLeftComics(tempCalendar);
                }
            } catch (MalformedURLException e) {
                Log.w(LOG_TAG, e.toString());
                Logger.getLogger(ComicFragment.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        private void updateLeftComics(Calendar tempCalendar) throws MalformedURLException {
            calendarRight = tempCalendar;
            do {
                int statusImage = tools.existImageDB(getActivity(), dateImage, language);
                switch (statusImage) {
                    case -1:
                        addComicToDB(dateImage);
                        break;
                    case 1:
                        addComicToList(dateImage);
                        break;
                    default:
                        break;
                }
                if(a<=PAGERS) {
                    dateImage = tools.getYesterday(tempCalendar);
                }
            } while (a < PAGERS);
            calendarLeft = tempCalendar;
            resultComics.add("FakePageBefore");
            comicsList = Lists.reverse(resultComics);
        }

        private void updateRightComics(Calendar tempCalendar) throws MalformedURLException {
            calendarLeft = tempCalendar;
            do {
                int statusImage = tools.existImageDB(getActivity(), dateImage, language);
                switch (statusImage) {
                    case -1:
                        addComicToDB(dateImage);
                        break;
                    case 1:
                        addComicToList(dateImage);
                        break;
                    default:
                        break;
                }
                if(a<=PAGERS) {
                    dateImage = tools.getTomorrow(tempCalendar);
                }
            } while (a < PAGERS);
            calendarRight = tempCalendar;
            resultComics.add("FakePageBefore");
            comicsList = resultComics;
        }

        private void addComicToDB(String dateImage) throws MalformedURLException {
            boolean imageExist = tools.existImageInUrl(tools.constructURLIma(language, dateImage + ".jpg"));
            if (imageExist) {
                addComicToList(dateImage);
            }
            ImagesProperties imagesProperties = new ImagesProperties();
            imagesProperties.setName(dateImage);
            imagesProperties.setLang(language);
            imagesProperties.setExist(imageExist ? "1" : "0");
            tools.saveImagePropertiesDB(getActivity(), imagesProperties);
        }

        private void addComicToList(String dateImage) {
            resultComics.add(dateImage);
            saveLastPageName(dateImage);
            a++;
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
            if(pager.getAdapter()== null) {
                for (String name : comicsList) {
                    try {
                        String url=name.contains("Fake")?"":tools.constructURLIma(language, name + ".jpg").toString();
                        adapter.addFragment(ImageComicsViewFragment.newInstance(url, comicsList.indexOf(name)));
                    } catch (MalformedURLException e) {
                        Log.w(LOG_TAG, e.toString());
                    }
                }
                pager.setAdapter(adapter);
            }else{
                int index=0;
                for (String name : comicsList) {
                    if(!name.contains("Fake")){
                        updateImagesInViewPager(index, name);
                    }
                    index++;
                }
            }
            if(mode==0) {
                pager.setCurrentItem(PAGERS, false);
            }else{
                pager.setCurrentItem(1, false);
            }

            dialog.dismiss();
        }

        private void updateImagesInViewPager(int index, String name) {
            try {
                CustomImageView customImageView= (CustomImageView) rootView.findViewById(INT_BASE+index);
                String url=tools.constructURLIma(language, name + ".jpg").toString();
                tools.loadImageFromInternet(customImageView, url);
            } catch (MalformedURLException e) {
                Log.d(LOG_TAG, e.toString());
                Logger.getLogger(ComicFragment.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    public String getLanguage() {
        return settings.get("language");
    }
}