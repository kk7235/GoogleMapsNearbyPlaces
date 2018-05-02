package com.androidtutorialpoint.googlemapsnearbyplaces;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Anooj on 18-Oct-16.
 */

public class PagerAdapter2 extends FragmentPagerAdapter {

    int pageCount;
  String kr;

    public PagerAdapter2(FragmentManager fragmentManager,String ar) {
        super(fragmentManager);
        this.kr=ar;
        this.pageCount = 4;
    }

    @Override
    public Fragment getItem(int position) {
        return PagerItemFragment2.newInstance(position,kr);
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}