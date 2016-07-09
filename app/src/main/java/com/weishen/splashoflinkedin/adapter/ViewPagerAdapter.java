package com.weishen.splashoflinkedin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wyw on 2016/6/28.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mDatas;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> mDatas) {
        super(fm);
        this.mDatas = mDatas;
    }

    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
