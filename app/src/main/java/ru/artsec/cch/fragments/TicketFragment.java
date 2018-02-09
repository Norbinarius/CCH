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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

import ru.artsec.cch.Nav;
import ru.artsec.cch.R;
import ru.artsec.cch.model.Event;
import ru.artsec.cch.model.SaleTicket;
import ru.artsec.cch.model.Ticket;


public class TicketFragment extends Fragment {

    private ProgressDialog pd;
    private LinearLayout cont;
    private LayoutInflater inflaterr;
    private ArrayList<Ticket> ticketList;
    private ArrayList<SaleTicket> salesList;

    private TextView keyVal;
    private TextView keyEvent;
    private TextView keyData;
    private TextView keyAttributes;
    private TextView keyState;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ticket_fragment, container, false);
        cont = (LinearLayout)root.findViewById(R.id.sales_list);
        inflaterr = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (!Nav.isIsSearchViaCam()) {
            ticketList = TicketSearchFragment.main.get(0).getTicketValues();
            salesList = TicketSearchFragment.main.get(0).getTicketSales();
        } else {
            ticketList = Nav.main.get(0).getTicketValues();
            salesList = Nav.main.get(0).getTicketSales();
        }

        keyVal = (TextView)root.findViewById(R.id.key_value);
        keyData = (TextView)root.findViewById(R.id.key_data);
        keyAttributes = (TextView)root.findViewById(R.id.key_attributes);
        keyState = (TextView)root.findViewById(R.id.key_status);
        keyEvent = (TextView)root.findViewById(R.id.key_action);

        setKeyDataToLayout();
        setKeySalesToLayout();
        return root;
    }

    private void setKeySalesToLayout(){
        for (int i = 0; i < salesList.size(); i++) {
            View child = inflaterr.inflate(R.layout.sale_fragment, null, false);

            TextView saleId = (TextView) child.findViewById(R.id.sale_id);
            TextView saleType = (TextView) child.findViewById(R.id.sale_type);
            TextView saleComment = (TextView) child.findViewById(R.id.sale_comment);
            TextView saleDate = (TextView) child.findViewById(R.id.sale_time);

            saleId.setText("Номер транзакции: " + salesList.get(i).getTransactionID());
            saleType.setText("Тип транзакции: " + salesList.get(i).getTransactionType());
            saleComment.setText("Комментарий: " + salesList.get(i).getTransactionComment());
            saleDate.setText("Дата транзакции: " + ru.artsec.cch.util.Formatter.timeToStr(salesList.get(i).getTransactionTime()));

            cont.addView(child);
        }
    }

    private void setKeyDataToLayout(){
        for (int i = 0; i < ticketList.size(); i++) {
            keyVal.setText("Номер билета: " + ticketList.get(i).getKeyValue());
            keyEvent.setText("Мероприятие билета: " + ticketList.get(i).getNameAction());
            keyData.setText("Информация: " + ticketList.get(i).getKeyData());
            keyAttributes.setText("Атрибуты: " + Arrays.toString(ticketList.get(i).getKeyAttributes()));
            keyState.setText("Статус прохода: " + ticketList.get(i).getKeyEnabled());
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
