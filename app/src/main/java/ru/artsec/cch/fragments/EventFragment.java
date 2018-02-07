package ru.artsec.cch.fragments;

/**
 * Created by Norbinarius on 07.02.2018.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.artsec.cch.Nav;
import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.Event;
import ru.artsec.cch.util.Formatter;


public class EventFragment extends Fragment {

    private ProgressDialog pd;
    private LinearLayout cont;
    private ArrayList<Event> eventList;
    private LayoutInflater inflaterr;
    private static int contID = 0;

    TextView eventId;
    TextView eventName;
    TextView eventStartTime;
    TextView eventEndTime;

    public static int getContID() {
        return contID;
    }

    public static void setContID(int contID) {
        EventFragment.contID = contID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.events_main, container, false);

        eventList = new ArrayList<Event>();
        cont = (LinearLayout)root.findViewById(R.id.layout_event);
        inflaterr = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        cont.removeAllViews();

        try{
            this.pd = ProgressDialog.show(getActivity(), "Загрузка данных..", "Пожалуйста, подождите...", true, false);
            new EventFragment.AsyncLoad().execute();
        } catch (Exception e) {
            Log.wtf("MYTAG", String.valueOf(e));
        }

        return root;
    }


    private void setDataToLayout(){

        for (int i = 0; i < eventList.size(); i++) {
            View child = inflaterr.inflate(R.layout.event_fragment, null, false);

            TextView eventId = (TextView) child.findViewById(R.id.event_id);
            TextView eventName = (TextView) child.findViewById(R.id.event_name);
            TextView eventStartTime = (TextView) child.findViewById(R.id.event_start_time);
            TextView eventEndTime = (TextView) child.findViewById(R.id.event_end_time);

            eventId.setText("ID: " + eventList.get(i).getId());
            eventName.setText(eventList.get(i).getName());
            eventStartTime.setText(getString(R.string.start) + ": " + Formatter.timeToStr(eventList.get(i).getTimeStart()));
            eventEndTime.setText(getString(R.string.end) + ": " + Formatter.timeToStr(eventList.get(i).getTimeEnd()));
            eventName.setTag(Integer.parseInt(eventList.get(i).getId()));
            //Проблема с установкой ИД для листнера
            child.findViewById(R.id.event_name).setTag(eventList.get(i).getId());
            cont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), String.valueOf(v.findViewById(R.id.event_name).getTag()), Toast.LENGTH_LONG).show();
                }
            });
            cont.addView(child);
        }
    }




    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            if(Nav.isEventActive()) {
                eventList = ServerProvider.getActiveEvents();
            } else {
                eventList = ServerProvider.getPassedEvents();
            }
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            setDataToLayout();
            EventFragment.this.pd.dismiss();
        }
    }
}
