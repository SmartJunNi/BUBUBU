package cn.edu.nini.bububu.modules.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nini on 2016/12/11.
 */
public class MyViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments=new ArrayList<>();
    //保存title
    private  List<String> mTitles=new ArrayList<>();

    public MyViewPageAdapter(FragmentManager fm) {
        super(fm);
    }



    public void addFragment(Fragment fragment,String title) {
        mFragments.add(fragment);
        mTitles.add(title);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
