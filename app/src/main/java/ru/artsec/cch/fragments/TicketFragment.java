package ru.artsec.cch.fragments;

/**
 * Created by Norbinarius on 07.02.2018.
 */

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.artsec.cch.R;
import ru.artsec.cch.model.Ticket;


public class TicketFragment extends Fragment {

    private ProgressDialog pd;
    private LinearLayout cont;
    private ArrayList<Ticket> ticketList;
    private TextView keyVal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ticket_fragment, container, false);
        keyVal = (TextView)root.findViewById(R.id.key_value);
        ticketList = TicketSearchFragment.ticketList;
        setDataToLayout();

        return root;
    }

    private void setDataToLayout(){
        for (int i = 0; i < ticketList.size(); i++) {
            keyVal.setText("ID: " + ticketList.get(i).getKeyValue());
        }
    }

    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            //ticketList = ServerProvider.getTicketProps(getActivity());
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            TicketFragment.this.pd.dismiss();
        }
    }
}
