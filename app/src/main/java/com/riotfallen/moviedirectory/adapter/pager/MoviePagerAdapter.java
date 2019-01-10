package com.riotfallen.moviedirectory.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.riotfallen.moviedirectory.fragment.MovieListFragment;

public class MoviePagerAdapter extends FragmentStatePagerAdapter {

    private int noOfTabs;

    public MoviePagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return MovieListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
