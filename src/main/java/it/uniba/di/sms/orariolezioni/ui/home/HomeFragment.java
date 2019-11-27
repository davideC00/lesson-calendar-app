package it.uniba.di.sms.orariolezioni.ui.home;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.uniba.di.sms.orariolezioni.R;

public class HomeFragment extends Fragment {

    private ViewPager mPager;

    private DaySlidePageAdapter pagerAdapter;

    // Initialized to the central page
    private int lastPosition = pagerAdapter.LOOPS_COUNT/2;
    private int rightLastPosition = pagerAdapter.LOOPS_COUNT/2; // The max right position reached
    private int leftLastPosition = pagerAdapter.LOOPS_COUNT/2; // The max left position reached

    // The current date of the page viewed
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

        // Put the correct date for the central page (First page viewed)
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
        final TextView tvDate = root.findViewById(R.id.tvCurrentDate);
        tvDate.setText(formatter.format(mCurrentDate));

        mPager = root.findViewById(R.id.vp_days);
        pagerAdapter = new DaySlidePageAdapter(this.getChildFragmentManager(), mCurrentDate);

        Calendar c = Calendar.getInstance();
        c.setTime(mCurrentDate);

        // Add the date for the central's left page of central
        c.add(Calendar.DATE, -1);
        pagerAdapter.addLeftDate(c.getTime());

        // Add the date for the central's right page of central
        c.add(Calendar.DATE, 2);
        pagerAdapter.addRightDate(c.getTime());

        mPager.setAdapter(pagerAdapter);
        // Set central page
        mPager.setCurrentItem(pagerAdapter.getCount()/2, false);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                Calendar c = Calendar.getInstance();
                c.setTime(mCurrentDate);

                // Animation
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f );
                fadeIn.setDuration(400);
                fadeIn.setFillAfter(true);

                if(lastPosition > position){
                    // Swipe left
                    // Change the date of the left page
                    c.add(Calendar.DATE, -1);
                    tvDate.startAnimation(fadeIn);
                    tvDate.setText(formatter.format(c.getTime()));
                }else if(lastPosition < position ){
                    // Swipe right
                    // Change the date of the right page
                    c.add(Calendar.DATE, 1);
                    tvDate.startAnimation(fadeIn);
                    tvDate.setText(formatter.format(c.getTime()));
                }
                lastPosition = position;
                mCurrentDate = c.getTime();

                if(leftLastPosition > position ){
                    // Subtract another -1 because this refers to the second left page from the current one
                    c.add(Calendar.DATE, -1);
                    // Add the date to te adapter's list
                    pagerAdapter.addLeftDate(c.getTime());
                    leftLastPosition = position;
                }else if(rightLastPosition < position ){
                    // Add another 1 because this refers to the second right page from the current one
                    c.add(Calendar.DATE, 1);
                    // Add the date to te adapter's list
                    pagerAdapter.addRightDate(c.getTime());
                    rightLastPosition = position;
                }
                pagerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return root;
    }

}