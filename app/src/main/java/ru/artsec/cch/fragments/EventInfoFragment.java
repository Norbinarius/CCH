package ru.artsec.cch.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.Event;
import ru.artsec.cch.util.Formatter;
import ru.artsec.cch.util.ServerProviderHelper;

/**
 * Created by Norbinarius on 07.02.2018.
 */

public class EventInfoFragment extends Fragment {

    private ProgressDialog pd;
    private ArrayList<Event> eventList;
    private LinearLayout cont;
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
        View root = inflater.inflate(R.layout.events_main, container, false);

        cont = (LinearLayout)root.findViewById(R.id.layout_event);
        inflaterr = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

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
        final View child = inflaterr.inflate(R.layout.event_info_fragment, null, false);

        eventId = (TextView) child.findViewById(R.id.event_info_id);
        eventName = (TextView) child.findViewById(R.id.event_info_name);
        eventStartTime = (TextView) child.findViewById(R.id.event_info_start_time);
        eventEndTime = (TextView) child.findViewById(R.id.event_info_end_time);
        attributes = (TextView) child.findViewById(R.id.event_info_attr);
        comment = (TextView) child.findViewById(R.id.event_info_comment);

        for (int i = 0; i < eventList.size(); i++) {
            eventId.setText(eventList.get(i).getId());
            eventName.setText(eventList.get(i).getName());
            eventStartTime.setText(Formatter.timeToStr(eventList.get(i).getTimeStart()));
            eventEndTime.setText(Formatter.timeToStr(eventList.get(i).getTimeEnd()));
            attributes.setText(Arrays.toString(eventList.get(i).getAttributes()));
            comment.setText(eventList.get(i).getComment());
        }

        cont.addView(child);
    }


    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            eventList = ServerProvider.getEventProps(getActivity(), EventFragment.actionID);
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            EventInfoFragment.this.pd.dismiss();
            ServerProviderHelper.errorException();
            if (ServerProviderHelper.getErrorMsg() == null) {
                setDataToLayout();
            } else {
                Log.wtf("MYTAG",ServerProviderHelper.getErrorMsg());
                Toast.makeText(getActivity(), ServerProviderHelper.getErrorMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
