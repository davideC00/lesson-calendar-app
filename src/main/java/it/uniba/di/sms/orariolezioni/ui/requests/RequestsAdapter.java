package it.uniba.di.sms.orariolezioni.ui.requests;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.model.Request;

public class RequestsAdapter extends ArrayAdapter<Request>{

    public RequestsAdapter(Context context, ArrayList<Request> requests){
        super(context, 0, requests);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Request request = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_request, parent, false);
        }

        EditText etFromTime = (EditText) convertView.findViewById(R.id.etFromTime);
        EditText etToTime = (EditText) convertView.findViewById(R.id.etToTime);
        TextView tvFromTeacher = (TextView) convertView.findViewById(R.id.tvFromTeacher);
        TextView tvToTeacher = (TextView) convertView.findViewById(R.id.tvToTeacher);

        etFromTime.setText(request.fromTime);
        etToTime.setText(request.toTime);
        tvFromTeacher.setText(request.fromTeacher);
        tvToTeacher.setText(request.toTeacher);

        return convertView;
    }

}
