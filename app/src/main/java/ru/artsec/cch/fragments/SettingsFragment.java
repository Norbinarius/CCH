package ru.artsec.cch.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.artsec.cch.Config;
import ru.artsec.cch.R;
import ru.artsec.cch.util.SharedPrefsUtil;

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

        inputDoor.setText(String.valueOf(SharedPrefsUtil.LoadInt(getActivity(), "DoorID", 101)));
        inputSKUD.setText(SharedPrefsUtil.LoadString(getActivity(), "SourceSKUD", Config.URL_SKUD));
        inputGate.setText(SharedPrefsUtil.LoadString(getActivity(), "SourceGate", Config.URL_GATES));
        inputNSKUD.setText(SharedPrefsUtil.LoadString(getActivity(), "NamespaceSKUD", Config.NAMESPACE_SKUD));
        inputNGate.setText(SharedPrefsUtil.LoadString(getActivity(), "NamespaceGate", Config.NAMESPACE_GATES));

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

}
