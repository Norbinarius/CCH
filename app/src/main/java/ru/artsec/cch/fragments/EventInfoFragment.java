package ru.artsec.cch.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.Event;
import ru.artsec.cch.util.Formatter;

/**
 * Created by Norbinarius on 07.02.2018.
 */

public class EventInfoFragment extends Fragment {

    private ProgressDialog pd;
    private ArrayList<Event> eventList;
    private LayoutInflater inflaterr;
    TextView eventId;
    TextView eventName;
    TextView eventStartTime;
    TextView eventEndTime;
    TextView attributes;
    TextView comment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_info_fragment, container, false);

        eventId = (TextView) root.findViewById(R.id.event_info_id);
        eventName = (TextView) root.findViewById(R.id.event_info_name);
        eventStartTime = (TextView) root.findViewById(R.id.event_info_start_time);
        eventEndTime = (TextView) root.findViewById(R.id.event_info_end_time);
        attributes = (TextView) root.findViewById(R.id.event_info_attr);
        comment = (TextView) root.findViewById(R.id.event_info_comment);

        eventList = new ArrayList<Event>();

        try{
            this.pd = ProgressDialog.show(getActivity(), "Загрузка данных..", "Пожалуйста, подождите...", true, false);
            new EventInfoFragment.AsyncLoad().execute();
        } catch (Exception e) {
            Log.wtf("MYTAG", String.valueOf(e));
        }
        return root;
    }

    private void setDataToLayout(){
        for (int i = 0; i < eventList.size(); i++) {
            eventId.setText("ID: " + eventList.get(i).getId());
            eventName.setText("Название: " + eventList.get(i).getName());
            eventStartTime.setText(getString(R.string.start) + ": " + Formatter.timeToStr(eventList.get(i).getTimeStart()));
            eventEndTime.setText(getString(R.string.end) + ": " + Formatter.timeToStr(eventList.get(i).getTimeEnd()));
            attributes.setText("Аттрибуты: " + Arrays.toString(eventList.get(i).getAttributes()));
            comment.setText("Комментарий: " + eventList.get(i).getComment());
        }
    }


    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            eventList = ServerProvider.getEventProps();
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            setDataToLayout();
            EventInfoFragment.this.pd.dismiss();
        }
    }
}
