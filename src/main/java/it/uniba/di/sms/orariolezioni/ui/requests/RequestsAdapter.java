package it.uniba.di.sms.orariolezioni.ui.requests;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;
import it.uniba.di.sms.orariolezioni.data.model.Request;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<Request> mRequests;

    private DbHandler db;

    RequestsAdapter(Context context, ArrayList<Request> requests){
        this.mInflater = LayoutInflater.from(context);
        this.mRequests = requests;
        this.db = new DbHandler(context);
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
        Lesson lesson = db.getLesson(request.lesson);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        requestHolder.etFromTime.setText(dateFormat.format(lesson.fromTime));
        requestHolder.etToTime.setText(dateFormat.format(lesson.toTime));

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

    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder{

        TextView tvDay;
        EditText etFromTime;
        EditText etToTime;
        TextView tvFromTeacher;
        TextView tvToTeacher;
        ImageButton btnDelete;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            etFromTime = itemView.findViewById(R.id.etFromTime);
            etToTime = itemView.findViewById(R.id.etToTime);
            tvFromTeacher = itemView.findViewById(R.id.tvFromTeacher);
            tvToTeacher = itemView.findViewById(R.id.tvToTeacher);
            btnDelete = itemView.findViewById(R.id.btn_delete);

        }
    }
}
