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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import ru.artsec.cch.Nav;
import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.Event;
import ru.artsec.cch.util.Formatter;
import ru.artsec.cch.util.ServerProviderHelper;


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

    public static int actionID;

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
            final View child = inflaterr.inflate(R.layout.event_fragment, null, false);

            TextView eventId = (TextView) child.findViewById(R.id.event_id);
            TextView eventName = (TextView) child.findViewById(R.id.event_name);
            TextView eventStartTime = (TextView) child.findViewById(R.id.event_start_time);
            TextView eventEndTime = (TextView) child.findViewById(R.id.event_end_time);

            eventId.setText("ID: " + eventList.get(i).getId());
            eventName.setText(eventList.get(i).getName());
            eventStartTime.setText(getString(R.string.start) + ": " + Formatter.timeToStr(eventList.get(i).getTimeStart()));
            eventEndTime.setText(getString(R.string.end) + ": " + Formatter.timeToStr(eventList.get(i).getTimeEnd()));

            child.setTag(Integer.valueOf(eventList.get(i).getId()));
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionID = (Integer) v.getTag();
                    getActivity().setTitle("Информация о мероприятии");
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.content_frame
                                    , new EventInfoFragment())
                            .commit();
                }
            });

            cont.addView(child);
        }
    }




    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            if (Nav.isEventActive()) {
                eventList = ServerProvider.getActiveEvents(getActivity());
            } else {
                eventList = ServerProvider.getPassedEvents(getActivity());
            }
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            EventFragment.this.pd.dismiss();
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
