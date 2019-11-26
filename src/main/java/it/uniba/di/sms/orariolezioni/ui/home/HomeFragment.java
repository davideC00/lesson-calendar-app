package it.uniba.di.sms.orariolezioni.ui.home;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;

public class HomeFragment extends Fragment {

    private ViewPager mPager;

    private DaySlidePageAdapter pagerAdapter;

    private int lastPosition = pagerAdapter.LOOPS_COUNT/2;

    private Date mCurrentDate = new Date();

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
        final TextView tvDate = root.findViewById(R.id.tvCurrentDate);
        tvDate.setText(formatter.format(mCurrentDate));

        mPager = root.findViewById(R.id.vp_days);
        ArrayList<Date> dates = new ArrayList<>();
        dates.add(new Date());
        dates.add(new Date());
        dates.add(new Date());
        pagerAdapter = new DaySlidePageAdapter(this.getChildFragmentManager(), dates);
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(lastPosition, false);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                Calendar c = Calendar.getInstance();
                c.setTime(mCurrentDate);
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
                fadeIn.setDuration(400);
                fadeIn.setFillAfter(true);
                if(lastPosition > position){
                    c.add(Calendar.DATE, -1);
                    tvDate.startAnimation(fadeIn);
                    tvDate.setText(formatter.format(c.getTime()));
                }
                if(lastPosition < position){
                    c.add(Calendar.DATE, 1);
                    tvDate.startAnimation(fadeIn);
                    tvDate.setText(formatter.format(c.getTime()));
                }
                mCurrentDate = c.getTime();
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return root;
    }

}