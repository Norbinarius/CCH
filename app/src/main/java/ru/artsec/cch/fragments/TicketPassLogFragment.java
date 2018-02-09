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

import ru.artsec.cch.Nav;
import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.Event;
import ru.artsec.cch.model.PassLogTicket;
import ru.artsec.cch.util.Formatter;


public class TicketPassLogFragment extends Fragment {

    private ProgressDialog pd;
    private LinearLayout cont;
    private ArrayList<PassLogTicket> passLogList;
    private LayoutInflater inflaterr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ticket_pass_log_main, container, false);

        passLogList = new ArrayList<PassLogTicket>();
        cont = (LinearLayout)root.findViewById(R.id.layout_pass_log_ticket);
        inflaterr = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        cont.removeAllViews();

        try{
            this.pd = ProgressDialog.show(getActivity(), "Загрузка данных..", "Пожалуйста, подождите...", true, false);
            new TicketPassLogFragment.AsyncLoad().execute();
        } catch (Exception e) {
            Log.wtf("MYTAG", String.valueOf(e));
        }

        return root;
    }


    private void setDataToLayout(){

        for (int i = 0; i < passLogList.size(); i++) {
            View child = inflaterr.inflate(R.layout.ticket_pass_log_fragment, null, false);

            TextView doorID = (TextView) child.findViewById(R.id.pass_log_door_id);
            TextView doorName = (TextView) child.findViewById(R.id.pass_log_door_name);
            TextView placeName = (TextView) child.findViewById(R.id.pass_log_place_name);
            TextView passTime = (TextView) child.findViewById(R.id.pass_log_time);
            ImageView passState = (ImageView) child.findViewById(R.id.pass_log_state);

            doorID.setText("Номер точки прохода: " + passLogList.get(i).getDoorID());
            doorName.setText("Точка прохода: " + passLogList.get(i).getDoorName());
            passTime.setText("Метка времени: " + Formatter.timeToStr(passLogList.get(i).getPassTime()));
            placeName.setText("Название площадки: " + passLogList.get(i).getPlaceName());

            if (passLogList.get(i).getIsEnter().replaceAll("\\s+","").equals("true")){
                passState.setImageResource(R.drawable.check);
            } else {
                passState.setImageResource(R.drawable.forbidden);
            }
            cont.addView(child);
        }
    }




    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            passLogList = ServerProvider.getTicketPassLog(getActivity(), TicketFragment.actionID, TicketFragment.keyValue);
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            setDataToLayout();
            TicketPassLogFragment.this.pd.dismiss();
        }
    }
}
