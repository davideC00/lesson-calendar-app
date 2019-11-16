package it.uniba.di.sms.orariolezioni.ui.requests;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Request;

public class RequestsAdapter extends ArrayAdapter<Request>{

    List<Request> requests;

    public RequestsAdapter(Context context, List<Request> requests){
        super(context, 0, requests);
        this.requests = requests;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Request request = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_request, parent, false);
        }

        EditText etFromTime = (EditText) convertView.findViewById(R.id.etFromTime);
        EditText etToTime = (EditText) convertView.findViewById(R.id.etToTime);
        TextView tvFromTeacher = (TextView) convertView.findViewById(R.id.tvFromTeacher);
        TextView tvToTeacher = (TextView) convertView.findViewById(R.id.tvToTeacher);
        ImageButton btnDelete = convertView.findViewById(R.id.btn_delete);

        etFromTime.setText(request.fromTime);
        etToTime.setText(request.toTime);
        tvFromTeacher.setText(request.fromTeacher);
        tvToTeacher.setText(request.toTeacher);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHandler db = new DbHandler(getContext());
                db.deleteRequest(request);
                requests.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

}
