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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.artsec.cch.Nav;
import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.Event;
import ru.artsec.cch.model.FailPassLog;
import ru.artsec.cch.model.PassLogTicket;
import ru.artsec.cch.util.Formatter;
import ru.artsec.cch.util.ServerProviderHelper;


public class TicketPassLogFragment extends Fragment {

    private ProgressDialog pd;
    private TableLayout cont;
    public View root;
    private ArrayList<PassLogTicket> passLogList;
    private LayoutInflater inflaterr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.ticket_pass_log_main, container, false);
        passLogList = new ArrayList<PassLogTicket>();

        cont =  (TableLayout)root.findViewById(R.id.tablelayout);
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
        cont.removeAllViews();
        TableRow header = (TableRow) inflaterr.inflate(R.layout.ticket_pass_log_table_header, null, false);
        cont.addView(header);
        for (int i = 0; i < passLogList.size(); i++) {
            TableRow child = (TableRow) inflaterr.inflate(R.layout.ticket_pass_log_fragment, null, false);

            TextView doorName = (TextView) child.findViewById(R.id.pass_log_door_name);
            TextView placeName = (TextView) child.findViewById(R.id.pass_log_place_name);
            TextView passTime = (TextView) child.findViewById(R.id.pass_log_time);
            ImageView passState = (ImageView) child.findViewById(R.id.pass_log_state);

            doorName.setText(passLogList.get(i).getDoorName());
            passTime.setText(Formatter.timeToStr(passLogList.get(i).getPassTime()));
            placeName.setText(passLogList.get(i).getPlaceName());

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
            TicketPassLogFragment.this.pd.dismiss();
            ServerProviderHelper.errorException();
            if (ServerProviderHelper.getErrorMsg() == null) {
                setDataToLayout();
            } else {
                Log.wtf("MYTAG",ServerProviderHelper.getErrorMsg());
                Toast.makeText(getActivity(), ServerProviderHelper.getErrorMsg(), Toast.LENGTH_LONG).show();
                ErrorFragment.setErrorMsgToFragment(getActivity(), "Произошла ошибка", ServerProviderHelper.getErrorMsg());
            }
        }
    }
}
