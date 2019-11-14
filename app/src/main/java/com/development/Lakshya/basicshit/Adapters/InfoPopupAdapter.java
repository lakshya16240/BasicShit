package com.development.Lakshya.basicshit.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class InfoPopupAdapter extends FragmentStatePagerAdapter{

    private List<Fragment> fragmentList;

    public InfoPopupAdapter(FragmentManager fm, List<Fragment> fragmentList) {

        super(fm);
        this.fragmentList = fragmentList;
    }



    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
