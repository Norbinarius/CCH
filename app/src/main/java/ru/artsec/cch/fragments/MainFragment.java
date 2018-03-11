package ru.artsec.cch.fragments;

/**
 * Created by Norbinarius on 07.02.2018.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.Event;
import ru.artsec.cch.util.Formatter;
import ru.artsec.cch.util.ServerProviderHelper;
import ru.artsec.cch.util.SharedPrefsUtil;
import ru.artsec.cch.util.StringWithTag;


public class MainFragment extends Fragment {

    private ProgressDialog pd;
    private LayoutInflater inflaterr;
    private LinearLayout cont;

    TextView eventId;
    TextView eventName;
    TextView eventStartTime;
    TextView eventEndTime;
    TextView attributes;
    TextView comment;

    public static ArrayList<Event> event;
    public static String actionID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.events_main, container, false);

        actionID =  SharedPrefsUtil.LoadString(getActivity(),"ActionID", null);

        cont = (LinearLayout)root.findViewById(R.id.layout_event);
        inflaterr = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        event = new ArrayList<Event>();
        try{
            MainFragment.this.pd = ProgressDialog.show(getActivity(), "Загрузка мероприятий..", "Пожалуйста, подождите...", true, false);
            new MainFragment.AsyncLoad().execute();
        } catch (Exception e) {
            Log.wtf("MYTAG", String.valueOf(e));
        }
        return root;
    }


    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            event = ServerProvider.getActiveEvents(getActivity());
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            MainFragment.this.pd.dismiss();
            ServerProviderHelper.errorException();
            if (ServerProviderHelper.getErrorMsg() == null) {
                if (event.size() > 0){
                    if (event.size() == 1) {
                        Toast.makeText(getActivity(), "Загрузка единственного мероприятия: " + event.get(0).getId(), Toast.LENGTH_SHORT).show();
                        saveActionAndTime(getActivity(),event.get(0).getId(), event.get(0).getTimeEnd());
                        try{
                            MainFragment.this.pd = ProgressDialog.show(getActivity(), "Загрузка..", "Пожалуйста, подождите...", true, false);
                            new MainFragment.LoadAction().execute();
                        } catch (Exception e) {
                            Log.wtf("MYTAG", String.valueOf(e));
                        }
                    } else {
                        if (actionID == null) {
                            alertDialogSelect(getActivity());
                        } else {
                            if(Formatter.isEventActive(Formatter.timeToStr(SharedPrefsUtil.LoadString(getActivity(), "TimeEvent", null)))){
                                try {
                                    MainFragment.this.pd = ProgressDialog.show(getActivity(), "Загрузка..", "Пожалуйста, подождите...", true, false);
                                    new MainFragment.LoadAction().execute();
                                } catch (Exception e) {
                                    Log.wtf("MYTAG", String.valueOf(e));
                                }
                            } else {
                                alertDialogSelect(getActivity());
                            }
                        }
                    }
                }
            } else {
                Log.wtf("MYTAG",ServerProviderHelper.getErrorMsg());
                Toast.makeText(getActivity(), ServerProviderHelper.getErrorMsg(), Toast.LENGTH_LONG).show();
                ErrorFragment.setErrorMsgToFragment(getActivity(), "Произошла ошибка", ServerProviderHelper.getErrorMsg());
            }
        }
    }

    private class LoadAction extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            event = ServerProvider.getEventProps(getActivity(), Integer.parseInt(SharedPrefsUtil.LoadString(getActivity(),"ActionID", null)));
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            MainFragment.this.pd.dismiss();
            ServerProviderHelper.errorException();
            if (ServerProviderHelper.getErrorMsg() == null) {
                loadAction();
            } else {
                Log.wtf("MYTAG",ServerProviderHelper.getErrorMsg());
                Toast.makeText(getActivity(), ServerProviderHelper.getErrorMsg(), Toast.LENGTH_LONG).show();
                ErrorFragment.setErrorMsgToFragment(getActivity(), "Произошла ошибка", ServerProviderHelper.getErrorMsg());
            }
        }
    }

    private void loadAction(){
        final View child = inflaterr.inflate(R.layout.main_page, null, false);

        eventId = (TextView) child.findViewById(R.id.event_id_main);
        eventName = (TextView) child.findViewById(R.id.event_name_main);
        eventStartTime = (TextView) child.findViewById(R.id.event_start_main);
        eventEndTime = (TextView) child.findViewById(R.id.event_end_main);
        attributes = (TextView) child.findViewById(R.id.event_attr_main);
        comment = (TextView) child.findViewById(R.id.event_comment_main);

        for (int i = 0; i < event.size(); i++) {
            eventId.setText("ID: " + event.get(i).getId());
            eventName.setText("Название: " + event.get(i).getName());
            eventStartTime.setText("Дата начала: " + Formatter.timeToStr(event.get(i).getTimeStart()));
            eventEndTime.setText("Дата окончания: " + Formatter.timeToStr(event.get(i).getTimeEnd()));
            attributes.setText("Атрибуты: " + Arrays.toString(event.get(i).getAttributes()));
            comment.setText("Комментарий: " + event.get(i).getComment());
        }
        cont.addView(child);
    }

    private void alertDialogSelect(Activity act){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setCancelable(false);
        builder.setTitle("Выберите мероприятие: ");

        List<StringWithTag> list = new ArrayList<StringWithTag>();
        for (int i = 0; i < event.size(); i++) {
            list.add(new StringWithTag(event.get(i).getName(), event.get(i).getId(), event.get(i).getTimeEnd()));
        }

        ArrayAdapter<StringWithTag> adap = new ArrayAdapter<StringWithTag>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);

        final Spinner sp = new Spinner(act);
        sp.setAdapter(adap);
        FrameLayout container = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 30;
        sp.setLayoutParams(params);
        container.addView(sp);
        builder.setView(container);

        builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringWithTag s = (StringWithTag) sp.getSelectedItem();
                Toast.makeText(getActivity(), "Загрузка мероприятия: " + s.id, Toast.LENGTH_SHORT).show();
                saveActionAndTime(getActivity(),s.id, s.time);
                try{
                    MainFragment.this.pd = ProgressDialog.show(getActivity(), "Загрузка..", "Пожалуйста, подождите...", true, false);
                    new MainFragment.LoadAction().execute();
                } catch (Exception e) {
                    Log.wtf("MYTAG", String.valueOf(e));
                }
            }
        });

        builder.show();
    }

    public static void saveActionAndTime(Activity act,String action, String time) {
        SharedPrefsUtil.SaveString(act,"ActionID", action);
        SharedPrefsUtil.SaveString(act,"TimeEvent", time);
    }
}
