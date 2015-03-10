package com.kaineras.pilliadventuremobile.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created the first version by kaineras on 12/02/15.
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{

    // List of fragments which are going to set in the view pager widget
    private List<Fragment> fragments;
    private static final String LOG_TAG = MyFragmentPagerAdapter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param fm interface for interacting with Fragment objects inside of an
     *           Activity
     */
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
    }
    /**
     * Add a new fragment in the list.
     *
     * @param fragment a new fragment
     */
    public void addFragment(Fragment fragment) { this.fragments.add(fragment); }

    @Override
    public int getItemPosition(Object object) { return POSITION_NONE; }

    @Override
    public Fragment getItem(int arg0) {
        return this.fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Página " + (position + 1);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
