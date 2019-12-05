package it.uniba.di.sms.orariolezioni.ui.home;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import it.uniba.di.sms.orariolezioni.R;

public class HomeFragment extends Fragment {

    private ViewPager mPager;

    private DaySlidePageAdapter pagerAdapter;

    // Initialized to the central page
    private int lastPosition;
    private int leftLastPosition; // The max left position reached
    private int rightLastPosition; // The max right position reached

    // The current date of the page viewed
    private Date mCurrentDate = new Date();

    private SimpleDateFormat formatter;

    private PagerViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(getActivity()!= null){
            homeViewModel =
                    ViewModelProviders.of(getActivity()).get(PagerViewModel.class);
            mCurrentDate = homeViewModel.getCentralDate();
        }

        lastPosition = pagerAdapter.LOOPS_COUNT/2;
        leftLastPosition = pagerAdapter.LOOPS_COUNT/2; // The max left position reached
        rightLastPosition = pagerAdapter.LOOPS_COUNT/2; // The max right position reached

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final TextView tvDate = root.findViewById(R.id.tvCurrentDate);
        mPager = root.findViewById(R.id.vp_days);
        // Don't allow to save the state when screen rotation
        mPager.setSaveEnabled(false); // if set to true the position is stored and let to an index out of bound
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        // Put the correct date for the central page (First page viewed)
        formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
        tvDate.setText(formatter.format(mCurrentDate));

        pagerAdapter = new DaySlidePageAdapter(this.getChildFragmentManager(), mCurrentDate);
        // Add the date for the central's left page of central
        pagerAdapter.addLeftDate(homeViewModel.getLeftDate());
        // Add the date for the central's right page of central
        pagerAdapter.addRightDate(homeViewModel.getRightDate());
        mPager.setAdapter(pagerAdapter);
        // Set central page
        mPager.setCurrentItem(pagerAdapter.getCount()/2, false);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {

                // Animation
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f );
                fadeIn.setDuration(400);
                fadeIn.setFillAfter(true);

                if(lastPosition > position){
                    // Swipe left
                    // Change the date of the left page
                    tvDate.startAnimation(fadeIn);
                    tvDate.setText(formatter.format(homeViewModel.getLeftDate().getTime())); // leftDate returns the first left pag from the centre
                    homeViewModel.setCentralDate(homeViewModel.getLeftDate()); // move central date
                }else if(lastPosition < position ){
                    // Swipe right
                    // Change the date of the right page
                    tvDate.startAnimation(fadeIn);
                    tvDate.setText(formatter.format(homeViewModel.getRightDate().getTime()));
                    homeViewModel.setCentralDate(homeViewModel.getRightDate()); // move central date
                }
                lastPosition = position;
                mCurrentDate = homeViewModel.getCentralDate();

                if(leftLastPosition > position ){
                    // Add the date to te adapter's list
                    pagerAdapter.addLeftDate(homeViewModel.getLeftDate());
                    leftLastPosition = position; // this avoids to activate addRightDate every time there is a swipe to left
                }else if(rightLastPosition < position ){
                    // Add the date to te adapter's list
                    pagerAdapter.addRightDate(homeViewModel.getRightDate());
                    rightLastPosition = position; // this avoids to activate addRightDate every time there is a swipe to right
                }
                pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null){
                    Bundle bundle = new Bundle();
                    bundle.putLong("currentDate", mCurrentDate.getTime());
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.action_nav_home_to_nav_add_request, bundle);
                }
            }
        });

        return root;
    }

}