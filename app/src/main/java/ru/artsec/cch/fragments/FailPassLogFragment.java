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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.FailPassLog;
import ru.artsec.cch.model.PassLogTicket;
import ru.artsec.cch.util.Formatter;
import ru.artsec.cch.util.ServerProviderHelper;


public class FailPassLogFragment extends Fragment {

    private ProgressDialog pd;
    private LinearLayout cont;
    private ArrayList<FailPassLog> failLogList;
    private LayoutInflater inflaterr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fail_pass_log_main, container, false);

        failLogList = new ArrayList<FailPassLog>();
        cont = (LinearLayout)root.findViewById(R.id.layout_fail_pass_log);
        inflaterr = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        cont.removeAllViews();

        try{
            this.pd = ProgressDialog.show(getActivity(), "Загрузка данных..", "Пожалуйста, подождите...", true, false);
            new FailPassLogFragment.AsyncLoad().execute();
        } catch (Exception e) {
            Log.wtf("MYTAG", String.valueOf(e));
        }

        return root;
    }


    private void setDataToLayout(){
        Log.wtf("MYTAG", String.valueOf(failLogList.size()));
        for (int i = failLogList.size() - 1; i > failLogList.size() - 4; i--) {
            View child = inflaterr.inflate(R.layout.fail_pass_fragment, null, false);

            TextView doorID = (TextView) child.findViewById(R.id.fail_log_door_id);
            TextView doorName = (TextView) child.findViewById(R.id.fail_log_door_name);
            TextView time = (TextView) child.findViewById(R.id.fail_log_time);
            TextView id = (TextView) child.findViewById(R.id.fail_log_id);
            TextView key = (TextView) child.findViewById(R.id.fail_log_key);
            TextView reason = (TextView) child.findViewById(R.id.fail_log_reason);

            doorID.setText("Номер точки прохода: " + failLogList.get(i).getDoorID());
            doorName.setText("Точка прохода: " + failLogList.get(i).getDoorName());
            time.setText("Метка времени: " + Formatter.timeToStr(failLogList.get(i).getTime()));
            id.setText("Номер отказа: " + failLogList.get(i).getID());
            key.setText("Номер билета: " + failLogList.get(i).getKey());
            reason.setText("Причина: " + failLogList.get(i).getReason());

            cont.addView(child);
        }
    }

    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            failLogList = ServerProvider.getFailPassLog(getActivity(), 1, 1);
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            FailPassLogFragment.this.pd.dismiss();
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
