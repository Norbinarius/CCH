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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.PairTicketProps;
import ru.artsec.cch.util.ServerProviderHelper;


public class TicketSearchFragment extends Fragment {

    private ProgressDialog pd;
    private LinearLayout cont;
    private static String ticketID;
    public static ArrayList<PairTicketProps> main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.events_main, container, false);

        main = new ArrayList<PairTicketProps>();

        createAlertDialog(getActivity());

        return root;
    }


    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            main = ServerProvider.getTicketProps(getActivity(), ticketID);
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            TicketSearchFragment.this.pd.dismiss();
            ServerProviderHelper.errorException();
            if (ServerProviderHelper.getErrorMsg() == null) {
                if (main.get(0).getTicketValues().size() > 0){
                    Toast.makeText(getActivity(), "Найден билет с идентификатором: " + main.get(0).getTicketValues().get(0).getKeyValue(), Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame
                                    , new TicketFragment())
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Билет с такими данными не найден", Toast.LENGTH_SHORT).show();
                    ErrorFragment.setErrorMsgToFragment(getActivity(), "Билет не найден", "Билет с такими данными не найден");
                }
            } else {
                Log.wtf("MYTAG",ServerProviderHelper.getErrorMsg());
                Toast.makeText(getActivity(), ServerProviderHelper.getErrorMsg(), Toast.LENGTH_LONG).show();
                ErrorFragment.setErrorMsgToFragment(getActivity(), "Произошла ошибка", ServerProviderHelper.getErrorMsg());
            }
        }
    }

    private void createAlertDialog(Activity act){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Введите идентификатор билета: ");
        final EditText input = new EditText(act);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Искать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    ticketID = input.getText().toString();
                    TicketSearchFragment.this.pd = ProgressDialog.show(getActivity(), "Поиск билета..", "Пожалуйста, подождите...", true, false);
                    new TicketSearchFragment.AsyncLoad().execute();
                } catch (Exception e) {
                    Log.wtf("MYTAG", String.valueOf(e));
                }

            }
        });

        builder.show();
    }
}
