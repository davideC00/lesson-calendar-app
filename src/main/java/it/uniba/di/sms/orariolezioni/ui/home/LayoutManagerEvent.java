package it.uniba.di.sms.orariolezioni.ui.home;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import it.uniba.di.sms.orariolezioni.R;

public class LayoutManagerEvent extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int currentHeight = 0;
        int i = 0;
        Log.i("qwerty", "height " +  getHeight());
        while (currentHeight < getHeight()){
            if(state.getItemCount() == 0) break;
            Log.i("qwerty", "execution num " + i);
            View view = recycler.getViewForPosition(i++);
            addView(view);

            int width = getDurationFrom(view);
            int offsetY = getStartPositionFrom(view);

            measureChildWithMargins(view, 0, 0);
            int itemWidth = getDecoratedMeasuredWidth(view);
            int itemHeight = getDecoratedMeasuredHeight(view);
            Rect rect = new Rect(0, currentHeight, itemWidth, currentHeight+itemHeight);
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom);
            currentHeight += itemHeight;
        }
    }

    private int getStartPositionFrom(View view) {
        Calendar calendar = GregorianCalendar.getInstance();

        // the minutes and hours have three 0s more so later there are less round errors
        int minutes = (calendar.get(Calendar.MINUTE)*1000)/60;
        int hours = calendar.get(Calendar.HOUR_OF_DAY)*1000;

        // Distance between prev and actual item in frameLayout
        return getPositionFromTime(getHeight(), hours + minutes);
    }

    private int getDurationFrom(View view){
        return 0;
    }

    // @param int time has three 0's more
    private int getPositionFromTime(int totalLength, int time){
        return ((totalLength/24)*time)/1000;
    }


}
