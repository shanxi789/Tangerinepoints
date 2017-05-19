package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    protected Context context;
    protected List<Fragment> list;
    protected FragmentManager fm;

    public FragmentAdapter(FragmentManager fm ,Context context, List<Fragment> list) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
