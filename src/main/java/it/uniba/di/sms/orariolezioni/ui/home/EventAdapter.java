package it.uniba.di.sms.orariolezioni.ui.home;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.model.Event;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;
import it.uniba.di.sms.orariolezioni.data.model.Unavailability;

public final class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {

    private final ArrayList<Event> mEvents;

    EventAdapter(ArrayList<Event> events){
        mEvents = events;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View layout;

        if (viewType == 0){
            layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, viewGroup, false);
            layout.setTag(0, "lesson");
            layout.setBackground(ContextCompat.getDrawable(viewGroup.getContext(), R.drawable.selector_lesson));
        }else {
            layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, viewGroup, false);
            layout.setTag(0,"unavailability");
            layout.setBackground(ContextCompat.getDrawable(viewGroup.getContext(), R.drawable.selector_unavailability));
        }

        return new EventHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder eventHolder, int i) {
        eventHolder.bind(mEvents.get(i));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mEvents.get(position) instanceof Lesson) return 0;
        else return 1;
    }


    public final static class EventHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView tvEventTeacher;
        TextView tvEventTime;

        public EventHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvEventTeacher = itemView.findViewById(R.id.tvLessonTeacher);
            tvEventTime = itemView.findViewById(R.id.tvLessonTime);
        }

        public final void bind(Event e){
            if(e instanceof Lesson){
                tvEventTeacher.setText(((Lesson) e).teacher);
            }else{
                tvEventTeacher.setText(((Unavailability) e).teacher);
            }

            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm", Locale.ITALY);
            String time = localDateFormat.format(e.fromTime) + "-"
                    + localDateFormat.format(e.toTime);
            tvEventTime.setText(time);

            // the duration(hours) is with 000 more so later there are less round errors
            // the real formula should have been (60*60*1000)
            long durationHours = (e.toTime.getTime() - e.fromTime.getTime())/(60 * 60);

            //Check if the duration of a lesson is greater than 24h
            if(durationHours > 24000){
                durationHours = 24000;
            }
            itemView.setTag(String.valueOf(durationHours));
            itemView.setTag(String.valueOf(e.fromTime.getTime()));
        }

    }


}
