package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(String title, Fragment fragment) {
        titles.add(title);
        fragments.add(fragment);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("LOG", getClass().getSimpleName()
                + ":destroyItem():start:viewgroup:" + container
                + ":position:" + position
                + ":object:" + object);
        super.destroyItem(container, position, object);
        Log.d("LOG", getClass().getSimpleName()+":destroyItem():finish:");
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
