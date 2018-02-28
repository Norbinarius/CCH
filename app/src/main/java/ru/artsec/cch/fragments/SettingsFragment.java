package ru.artsec.cch.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.artsec.cch.Config;
import ru.artsec.cch.R;
import ru.artsec.cch.model.Settings;
import ru.artsec.cch.util.AdapterForSettings;
import ru.artsec.cch.util.SharedPrefsUtil;
import ru.artsec.cch.util.StringWithTag;

/**
 * Created by Norbinarius on 07.02.2018.
 */

public class SettingsFragment extends Fragment {

    private EditText inputDoor;
    private EditText inputSKUD;
    private EditText inputGate;
    private EditText inputNGate;
    private  EditText inputNSKUD;

    private Button apply;
    private Button makePreload;
    private Button loadPreload;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_fragment, container, false);

        inputDoor = (EditText) root.findViewById(R.id.settings_door_input);
        inputGate = (EditText) root.findViewById(R.id.settings_source_gate);
        inputSKUD = (EditText) root.findViewById(R.id.settings_source_skud);
        inputNGate = (EditText) root.findViewById(R.id.settings_namespace_gate);
        inputNSKUD = (EditText) root.findViewById(R.id.settings_namespace_skud);
        apply = (Button) root.findViewById(R.id.settings_apply);
        makePreload = (Button) root.findViewById(R.id.settings_create_preload);
        loadPreload = (Button) root.findViewById(R.id.settings_apply_preload);

        inputDoor.setText(String.valueOf(SharedPrefsUtil.LoadInt(getActivity(), "DoorID", 101)));
        inputSKUD.setText(SharedPrefsUtil.LoadString(getActivity(), "SourceSKUD", Config.URL_SKUD));
        inputGate.setText(SharedPrefsUtil.LoadString(getActivity(), "SourceGate", Config.URL_GATES));
        inputNSKUD.setText(SharedPrefsUtil.LoadString(getActivity(), "NamespaceSKUD", Config.NAMESPACE_SKUD));
        inputNGate.setText(SharedPrefsUtil.LoadString(getActivity(), "NamespaceGate", Config.NAMESPACE_GATES));

        makePreload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCreatePreload(getActivity());
            }
        });

        loadPreload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogLoadPreload(getActivity());
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if(inputDoor.getText() != null && inputGate.getText() != null && inputSKUD.getText() != null  && inputNGate.getText() != null && inputNSKUD.getText() != null){
                    try {
                        SharedPrefsUtil.SaveInt(getActivity(), "DoorID", Integer.parseInt(inputDoor.getText().toString()));
                        SharedPrefsUtil.SaveString(getActivity(), "SourceSKUD", inputSKUD.getText().toString());
                        SharedPrefsUtil.SaveString(getActivity(), "SourceGate", inputGate.getText().toString());
                        SharedPrefsUtil.SaveString(getActivity(), "NamespaceSKUD", inputNSKUD.getText().toString());
                        SharedPrefsUtil.SaveString(getActivity(), "NamespaceGate", inputNGate.getText().toString());
                        Toast.makeText(getActivity(), "Настройки обновлены", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Данные введены не верном формате", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Одно из полей не заполнено", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void alertDialogLoadPreload(Activity act){
        ArrayList<Settings> array = new ArrayList<Settings>();
        array = SharedPrefsUtil.LoadArrayList(getActivity(), "SettingsPreload");
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(act);
            builder.setTitle("Выберите предустановку: ");
            List<AdapterForSettings> list = new ArrayList<AdapterForSettings>();
            for (int i = 0; i < array.size(); i++) {
                list.add(new AdapterForSettings(array.get(i).getNamePreload(), array.get(i).getIpGatePreload(), array.get(i).getIpSKUDPreload(),
                        array.get(i).getNamespaceGatePreload(), array.get(i).getNamespaceSKUDPreload()));
            }
            ArrayAdapter<AdapterForSettings> adap = new ArrayAdapter<AdapterForSettings>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
            final Spinner sp = new Spinner(act);
            sp.setAdapter(adap);
            FrameLayout container = new FrameLayout(getActivity());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 30;
            sp.setLayoutParams(params);
            container.addView(sp);
            builder.setView(container);

            builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    AdapterForSettings ad = (AdapterForSettings) sp.getSelectedItem();
                    inputSKUD.setText(ad.ipSKUDPreload);
                    inputGate.setText(ad.ipGatePreload);
                    inputNSKUD.setText(ad.namespaceSKUDPreload);
                    inputNGate.setText(ad.namespaceGatePreload);
                    Toast.makeText(getActivity(), "Предустановка загружена, для применения сохраните настройки", Toast.LENGTH_SHORT).show();
                }
            });

            builder.show();
        } catch (Exception e){
            Toast.makeText(getActivity(), "Предустановок нет", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertDialogCreatePreload(Activity act){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Введите название предустановки: ");
        final EditText input = new EditText(act);
        builder.setView(input);

        builder.setPositiveButton("Создать предустановку", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (input.getText() != null){
                    ArrayList<Settings> array = new ArrayList<Settings>();
                    array = SharedPrefsUtil.LoadArrayList(getActivity(), "SettingsPreload");
                    Settings setts = new Settings();
                    setts.setNamePreload(input.getText().toString());
                    setts.setIpGatePreload(inputGate.getText().toString());
                    setts.setIpSKUDPreload(inputSKUD.getText().toString());
                    setts.setNamespaceGatePreload(inputNGate.getText().toString());
                    setts.setNamespaceSKUDPreload(inputNSKUD.getText().toString());
                    array.add(setts);
                    SharedPrefsUtil.SaveArrayList(getActivity(), "SettingsPreload", array);
                } else {
                    Toast.makeText(getActivity(), "Поле не заполнено", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.show();
    }

}
