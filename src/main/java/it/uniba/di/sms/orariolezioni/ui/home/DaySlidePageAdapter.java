package it.uniba.di.sms.orariolezioni.ui.home;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Date;

public class DaySlidePageAdapter extends  FragmentStatePagerAdapter
{
    public static int LOOPS_COUNT = 1000;
    private ArrayList<Date> mDates;


    public DaySlidePageAdapter(FragmentManager manager, ArrayList<Date> dates)
    {
        super(manager);
        mDates = dates;
    }


    @Override
    public Fragment getItem(int position)
    {
        if (mDates != null && mDates.size() > 0)
        {
            position = position % mDates.size(); // use modulo for infinite cycling
            return DaySlidePageFragment.newInstance(position);
        }
        else
        {
            return DaySlidePageFragment.newInstance(null);
        }
    }


    @Override
    public int getCount()
    {
        if (mDates != null && mDates.size() > 0)
        {
            return mDates.size()*LOOPS_COUNT; // simulate infinite by big number of products
        }
        else
        {
            return 1;
        }
    }
} 