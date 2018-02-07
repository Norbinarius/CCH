package ru.artsec.cch.util;

import android.content.Context;
import android.content.SharedPreferences;

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
}
