package com.androidtutorialpoint.googlemapsnearbyplaces;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Anooj on 18-Oct-16.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    int pageCount;
    ArrayList<Markerpoints> kr=new ArrayList<>();

    public PagerAdapter(FragmentManager fragmentManager,ArrayList<Markerpoints> ar) {
        super(fragmentManager);
        this.kr=ar;
        this.pageCount = ar.size();
    }

    @Override
    public Fragment getItem(int position) {
        return PagerItemFragment.newInstance(position,kr);
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}