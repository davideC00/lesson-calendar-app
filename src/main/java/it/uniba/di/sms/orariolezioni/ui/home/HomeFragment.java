package it.uniba.di.sms.orariolezioni.ui.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import it.uniba.di.sms.orariolezioni.R;

public class HomeFragment extends Fragment implements OnPreDrawDaySlidePage, ViewPager.OnPageChangeListener {

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

    private Switch switchType;
    private TextView tvDate;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(getActivity()!= null){
            homeViewModel =
                    ViewModelProviders.of(getActivity()).get(PagerViewModel.class);
            if(getArguments() != null && getArguments().getLong("date") != 0L){
                homeViewModel.setCentralDate(new Date(getArguments().getLong("date")));
                getArguments().remove("date"); // remove the argument because when rotating the activity reads this value
            }
            mCurrentDate = homeViewModel.getCentralDate();
        }

        lastPosition = pagerAdapter.LOOPS_COUNT/2;
        leftLastPosition = pagerAdapter.LOOPS_COUNT/2; // The max left position reached
        rightLastPosition = pagerAdapter.LOOPS_COUNT/2; // The max right position reached

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        tvDate = root.findViewById(R.id.tvCurrentDate);
        switchType = root.findViewById(R.id.switchType);
        mPager = root.findViewById(R.id.vp_days);

        // Don't allow to save the state when screen rotation or configuration change
        mPager.setSaveEnabled(false); // if set to true the position is stored and give index out of bound

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

        mPager.addOnPageChangeListener(this);


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

        switchType.setChecked(homeViewModel.getSwitchState());
        switchType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchVisibility(isChecked);
                homeViewModel.setSwitchState(isChecked);
            }
        });

        return root;
    }

    private void switchVisibility(boolean state){
        if(state){
            // lesson
            showViewsWithTag("lesson");
            hideViewsWithTag("unavailability");
        }else{
            // unavailability
            showViewsWithTag("unavailability");
            hideViewsWithTag("lesson");
        }
    }

    @Override
    public void onPreDrawFragmentPage() {
        switchVisibility(switchType.isChecked());
    }

    private void showViewsWithTag(String tag) {

        ArrayList<View> views = getViewsWithTag(tag);
        for(View v : views){
            v.setVisibility(View.VISIBLE);
        }
    }

    private void hideViewsWithTag(String tag){
        ArrayList<View> views = getViewsWithTag(tag);
        for(View v : views){
            v.setVisibility(View.INVISIBLE);
        }
    }

    private ArrayList<View> getViewsWithTag(String tag){
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        ArrayList<View> views = new ArrayList<>();

        for (Fragment fragment : fragments) {
            if(fragment instanceof DaySlidePageFragment){
                views.addAll(((DaySlidePageFragment) fragment).getViewsWithTag(tag));
            }
        }
        return views;
    }

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
    public void onPageScrollStateChanged(int state) {
    }
}

