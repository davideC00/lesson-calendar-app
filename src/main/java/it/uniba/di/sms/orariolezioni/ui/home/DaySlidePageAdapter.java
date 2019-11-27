package it.uniba.di.sms.orariolezioni.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class DaySlidePageAdapter extends  FragmentStatePagerAdapter
{
    public static int LOOPS_COUNT = 1000;
    private ArrayList<Date> mLeftDates = new ArrayList<>(); // Dates from the central date to the left
    private ArrayList<Date> mRightDates = new ArrayList<>(); // Dates from central date to the right (central included)

    public DaySlidePageAdapter(FragmentManager manager, Date centralDate)
    {
        super(manager);
        mRightDates.add(centralDate);
    }


    @Override
    public Fragment getItem(int position)
    {
        Log.i("qwerty", "position "+ position);

        if(position < LOOPS_COUNT/2){
            // it is the left from the central page
            position = LOOPS_COUNT / 2 - position - 1; // e.g. 1000/2 - 498 - 1 = 2 give the corresponding position in the array mLeftDates
            return DaySlidePageFragment.newInstance(mLeftDates.get(position));
        }
        else if( position >= LOOPS_COUNT/2){
            // it is the right from the central page
            position = position - LOOPS_COUNT / 2; // There isnt -1 in the formula because the is the central value in mRightDates
            return DaySlidePageFragment.newInstance(mRightDates.get(position));
        }
        return DaySlidePageFragment.newInstance(new Date());
    }


    @Override
    public int getCount()
    {
        if (mRightDates!= null)
        {
            return LOOPS_COUNT; // simulate infinite by big number of products
        }
        return 1;
    }

    public void addLeftDate(Date d){
        mLeftDates.add(d);
    }

    public void addRightDate(Date d){
        mRightDates.add(d);
    }

} 