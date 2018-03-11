package ru.artsec.cch.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import ru.artsec.cch.Nav;
import ru.artsec.cch.R;
import ru.artsec.cch.ServerProvider;
import ru.artsec.cch.model.Event;
import ru.artsec.cch.util.Formatter;
import ru.artsec.cch.util.ServerProviderHelper;

/**
 * Created by Norbinarius on 07.02.2018.
 */

public class ErrorFragment extends Fragment {

    public static TextView error;
    private static String errorMsg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.error_fragment, container, false);
        error = (TextView)root.findViewById(R.id.error_text);
        error.setText(errorMsg);
        return root;
    }

    public static void setErrorMsgToFragment(Activity act, String title, String errorText){
        act.setTitle(title);
        act.getFragmentManager().beginTransaction()
                .replace(R.id.content_frame
                        , new ErrorFragment())
                .commit();
        errorMsg = errorText;
    }
}
