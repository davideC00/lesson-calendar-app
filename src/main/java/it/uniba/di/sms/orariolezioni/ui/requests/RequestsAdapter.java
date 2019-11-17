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

import java.util.ArrayList;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
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

        requestHolder.etFromTime.setText(request.fromTime);
        requestHolder.etToTime.setText(request.toTime);
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

        EditText etFromTime;
        EditText etToTime;
        TextView tvFromTeacher;
        TextView tvToTeacher;
        ImageButton btnDelete;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            etFromTime = (EditText) itemView.findViewById(R.id.etFromTime);
            etToTime = (EditText) itemView.findViewById(R.id.etToTime);
            tvFromTeacher = (TextView) itemView.findViewById(R.id.tvFromTeacher);
            tvToTeacher = (TextView) itemView.findViewById(R.id.tvToTeacher);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btn_delete);

        }
    }
}
