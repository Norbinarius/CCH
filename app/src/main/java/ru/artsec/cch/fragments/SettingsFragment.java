package ru.artsec.cch.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.artsec.cch.R;
import ru.artsec.cch.util.SharedPrefsUtil;

/**
 * Created by Norbinarius on 07.02.2018.
 */

public class SettingsFragment extends Fragment {

    private EditText inputDoor;
    private Button apply;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_fragment, container, false);

        inputDoor = (EditText) root.findViewById(R.id.settings_door_input);
        apply = (Button) root.findViewById(R.id.settings_apply);

        inputDoor.setText(String.valueOf(SharedPrefsUtil.LoadInt(getActivity(), "DoorID", 0)));

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDoor.setInputType(InputType.TYPE_NULL);
                if(inputDoor.getText() != null){
                    try {
                        SharedPrefsUtil.SaveInt(getActivity(), "DoorID", Integer.parseInt(inputDoor.getText().toString()));
                        Toast.makeText(getActivity(), "Настройки обновлены", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Данные введены не верном формате", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Поле ввода точки прохода не содержит символов", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

}
