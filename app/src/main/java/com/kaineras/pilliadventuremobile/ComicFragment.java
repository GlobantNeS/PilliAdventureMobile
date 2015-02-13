package com.kaineras.pilliadventuremobile;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.toolbox.NetworkImageView;
import com.kaineras.pilliadventuremobile.adapter.ImageComicsViewFragment;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import com.google.common.collect.Lists;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComicFragment extends Fragment {

    private View v;
    private HashMap settings = new HashMap();
    Tools tools=new Tools();
    ViewPager pager = null;
    MyFragmentPagerAdapter adapter;


    public ComicFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_comic, container, false);
        settings = tools.getPreferences(getActivity());
        preparePager();
        adapter = new MyFragmentPagerAdapter(getFragmentManager());
        boolean lastPage=!getArguments().getBoolean("PAGE");
        if(lastPage)
        {
           new updateComic().execute();
        }
        /*else
        {
            String year="2015";
            String month="02";
            List<String> urls= null;
            try {
                urls = tools.getUrlsByMonth(getActivity(),year,month);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            int index=0;
            for(String nameImage:urls)
            {
                try {
                    adapter.addFragment(ImageComicsViewFragment.newInstance(tools.constructURLIma(" ", nameImage).toString(), index));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }*/

        //pager.setAdapter(adapter);
        return v;
    }

    public void preparePager()
    {
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0)
                {

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class updateComic extends AsyncTask<Void,Void,Void> {

        List<String> resultComics=new ArrayList<>();
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
            String dateImage=tools.calendarToString(calendar);
            try {

                for(int a=20;a>0;)
                {
                    if(tools.existImage(tools.constructURLIma(" ",dateImage+ ".jpg")))
                    {
                        resultComics.add(dateImage);
                        a--;
                    }
                    dateImage=tools.getYesterday(calendar);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            List<String> reverseList=Lists.reverse(resultComics);
            for(String name:reverseList)
                try {
                    adapter.addFragment(ImageComicsViewFragment.newInstance(tools.constructURLIma(" ", name+ ".jpg").toString(), reverseList.indexOf(name)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            pager.setAdapter(adapter);
            dialog.dismiss();
        }
    }
}