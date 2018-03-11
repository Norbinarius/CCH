package ru.artsec.cch.fragments;

/**
 * Created by Norbinarius on 07.02.2018.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import ru.artsec.cch.Nav;
import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.FailPassLog;
import ru.artsec.cch.model.PassLogTicket;
import ru.artsec.cch.util.Formatter;
import ru.artsec.cch.util.ServerProviderHelper;


public class FailPassLogFragment extends Fragment {

    private ProgressDialog pd;
    private TableLayout cont;
    public View root;
    private boolean alreadyUpdated;
    private ArrayList<FailPassLog> failLogList;
    private LayoutInflater inflaterr;
    private static int failCount = 5;
    private int getFailFromId = 1;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fail_pass_log_main, container, false);
        getFailFromId = 1;
        failCount = 5;
        alreadyUpdated = false;
        failLogList = new ArrayList<FailPassLog>();
        cont =  (TableLayout)root.findViewById(R.id.tablelayout);
        inflaterr = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        cont.removeAllViews();
        timer = new Timer();
        callAsyncTaskEveryInterval();

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.wtf("MYTAG", "kek");
        timer.cancel();
    }

    private void setDataToLayoutFirstTime(){
        cont.removeAllViews();
        Log.wtf("MYTAG", String.valueOf(failLogList.size()));
        TableRow header = (TableRow) inflaterr.inflate(R.layout.fail_pass_table_header, null, false);
        cont.addView(header);
        for (int i = failLogList.size() - 1; i > failLogList.size() - failCount - 1; i--) {

            //Last cycle record will be last id
            getFailFromId = Integer.parseInt(failLogList.get(i).getID());

            TableRow child = (TableRow) inflaterr.inflate(R.layout.fail_pass_fragment, null, false);

            TextView placeName = (TextView) child.findViewById(R.id.fail_log_door_place);
            TextView doorName = (TextView) child.findViewById(R.id.fail_log_door_name);
            TextView time = (TextView) child.findViewById(R.id.fail_log_time);
            TextView key = (TextView) child.findViewById(R.id.fail_log_key);
            TextView reason = (TextView) child.findViewById(R.id.fail_log_reason);

            placeName.setText(failLogList.get(i).getPlaceName());
            doorName.setText(failLogList.get(i).getDoorName());
            time.setText(Formatter.timeToStr(failLogList.get(i).getTime()));
            key.setText(failLogList.get(i).getKey());
            reason.setText(failLogList.get(i).getReason());

            cont.addView(child);
        }
    }

    private void setDataToLayoutOnUpdate(){
        cont.removeAllViews();
        Log.wtf("MYTAG", String.valueOf(failLogList.size()));
        TableRow header = (TableRow) inflaterr.inflate(R.layout.fail_pass_table_header, null, false);
        cont.addView(header);
        for (int i = failLogList.size() - 1; i >= 0; i--) {
            TableRow child = (TableRow) inflaterr.inflate(R.layout.fail_pass_fragment, null, false);

            TextView placeName = (TextView) child.findViewById(R.id.fail_log_door_place);
            TextView doorName = (TextView) child.findViewById(R.id.fail_log_door_name);
            TextView time = (TextView) child.findViewById(R.id.fail_log_time);
            TextView key = (TextView) child.findViewById(R.id.fail_log_key);
            TextView reason = (TextView) child.findViewById(R.id.fail_log_reason);

            placeName.setText(failLogList.get(i).getPlaceName());
            doorName.setText(failLogList.get(i).getDoorName());
            time.setText(Formatter.timeToStr(failLogList.get(i).getTime()));
            key.setText(failLogList.get(i).getKey());
            reason.setText(failLogList.get(i).getReason());

            cont.addView(child);
        }
    }

    private void callAsyncTaskEveryInterval() {
        final Handler handler = new Handler();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (getFailFromId == 1) {
                                FailPassLogFragment.this.pd = ProgressDialog.show(getActivity(), "Загружка журнала..", "Пожалуйста, подождите...", true, false);
                            }
                            new AsyncLoad().execute();
                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 5*1000);  // interval of 5 sec
    }

    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            if (getFailFromId == 1) {
                failLogList = ServerProvider.getFailPassLog(getActivity(), 0);
            } else {
                failLogList = ServerProvider.getFailPassLog(getActivity(), getFailFromId);
            }
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            if (FailPassLogFragment.this.pd.isShowing()){
                FailPassLogFragment.this.pd.dismiss();
            }
            ServerProviderHelper.errorException();
            if (failLogList.size() > 0) {
                if (ServerProviderHelper.getErrorMsg() == null) {
                    if (getFailFromId == 1) {
                        setDataToLayoutFirstTime();
                    } else {
                        setDataToLayoutOnUpdate();
                        Toast.makeText(getActivity(), "Журнал отказов обновлен.", Toast.LENGTH_SHORT).show();
                    }
                    Log.wtf("MYTAG", "kek");
                } else {
                    Log.wtf("MYTAG", ServerProviderHelper.getErrorMsg());
                    Toast.makeText(getActivity(), ServerProviderHelper.getErrorMsg(), Toast.LENGTH_LONG).show();
                    ErrorFragment.setErrorMsgToFragment(getActivity(), "Произошла ошибка", ServerProviderHelper.getErrorMsg());
                }
            } else {
                Toast.makeText(getActivity(), "Отказы не обнаружены.", Toast.LENGTH_LONG).show();
                ErrorFragment.setErrorMsgToFragment(getActivity(), "Журнал пуст", "Отказы не обнаружены.");
            }
        }
    }
}
