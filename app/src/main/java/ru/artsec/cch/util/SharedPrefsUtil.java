package ru.artsec.cch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.artsec.cch.model.Settings;

/**
 * Created by Norbinarius on 03.04.2017.
 */

public class SharedPrefsUtil {

    private static SharedPreferences shdPrefs;
    private static SharedPreferences.Editor shdPrefsEdit;

    public static void SaveInt(Context context, String name, int saveValue){
        shdPrefs = context.getSharedPreferences("SettingsUtil", 0);
        shdPrefsEdit = shdPrefs.edit();
        shdPrefsEdit.putInt(name, saveValue);
        shdPrefsEdit.commit();
    }

    public static int LoadInt(Context context, String name, int defaultValue){
        shdPrefs = context.getSharedPreferences("SettingsUtil", 0);
        return shdPrefs.getInt(name, defaultValue);
    }

    public static void SaveBool(Context context, String name, boolean saveValue){
        shdPrefs = context.getSharedPreferences("SettingsUtil", 0);
        shdPrefsEdit = shdPrefs.edit();
        shdPrefsEdit.putBoolean(name, saveValue);
        shdPrefsEdit.commit();
    }

    public static boolean LoadBool(Context context, String name, boolean defaultValue){
        shdPrefs = context.getSharedPreferences("SettingsUtil", 0);
        return shdPrefs.getBoolean(name, defaultValue);
    }

    public static void SaveString(Context context, String name, String saveValue){
        shdPrefs = context.getSharedPreferences("SettingsUtil", 0);
        shdPrefsEdit = shdPrefs.edit();
        shdPrefsEdit.putString(name, saveValue);
        shdPrefsEdit.commit();
    }

    public static String LoadString(Context context, String name, String defaultValue){
        shdPrefs = context.getSharedPreferences("SettingsUtil", 0);
        return shdPrefs.getString(name, defaultValue);
    }

    public static void SaveArrayList(Context context, String name, ArrayList array){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(array);
        editor.putString(name, json);
        editor.commit();
    }

    public static ArrayList<Settings> LoadArrayList(Context context, String name){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(name, null);
        Type type = new TypeToken<ArrayList<Settings>>() {}.getType();
        ArrayList<Settings> arrayList = gson.fromJson(json, type);
        return  arrayList;
    }
}
