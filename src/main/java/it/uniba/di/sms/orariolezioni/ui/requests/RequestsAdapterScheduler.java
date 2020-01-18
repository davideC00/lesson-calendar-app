package it.uniba.di.sms.orariolezioni.ui.requests;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.navigation.NavController;
import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;
import it.uniba.di.sms.orariolezioni.data.model.Request;

public class RequestsAdapterScheduler extends RecyclerView.Adapter<RequestsAdapterScheduler.RequestViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<Request> mRequests;
    private NavController navController;
    private DbHandler db;

    RequestsAdapterScheduler(Context context, ArrayList<Request> requests, NavController navController){
        this.mInflater = LayoutInflater.from(context);
        this.mRequests = requests;
        this.db = new DbHandler(context);
        this.navController = navController;
    }


    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_request, viewGroup, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestViewHolder requestHolder, int position) {
        final Request request = mRequests.get(position);
        final Lesson lesson = db.getLesson(request.lesson);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        requestHolder.tvFromTime.setText(dateFormat.format(lesson.fromTime));
        requestHolder.tvToTime.setText(dateFormat.format(lesson.toTime));

        dateFormat =  new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
        requestHolder.tvDay.setText(dateFormat.format(lesson.fromTime));
        requestHolder.tvFromTeacher.setText(request.fromTeacher);
        requestHolder.tvToTeacher.setText(request.toTeacher);

        requestHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteRequest(request);
                mRequests.remove(requestHolder.getAdapterPosition());
                notifyItemRemoved(requestHolder.getAdapterPosition());
            }
        });

        requestHolder.tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("date", lesson.fromTime.getTime());
                navController.navigate(R.id.action_nav_change_requests_to_nav_home, bundle);
            }
        });

        requestHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.acceptRequest(request);
                mRequests.remove(requestHolder.getAdapterPosition());
                notifyItemRemoved(requestHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder{

        TextView tvDay;
        TextView tvFromTime;
        TextView tvToTime;
        TextView tvFromTeacher;
        TextView tvToTeacher;
        TextView tvShow;
        ImageButton btnDelete;
        ImageButton btnAccept;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvFromTime = itemView.findViewById(R.id.etFromTime);
            tvToTime = itemView.findViewById(R.id.etToTime);
            tvFromTeacher = itemView.findViewById(R.id.tvFromTeacher);
            tvToTeacher = itemView.findViewById(R.id.tvToTeacher);
            tvShow = itemView.findViewById(R.id.tvShow);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnAccept = itemView.findViewById(R.id.btn_accept);
        }
    }
}
