package ru.artsec.cch.util;

import android.app.FragmentManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import ru.artsec.cch.R;

/**
 * Created by Norbinarius on 31.01.2018.
 */

public class Formatter {

    public static String[] findNumeric(String str){
        str = str.replaceAll("[^-?0-9]+", " ");
        String[] stringArray = str.trim().split(" ");
        return stringArray;
    }

    public static String timeToStr(String str){
        str = str.replaceAll("T", " ");
        return str;
    }

    public static Date getCurrentTime(){
        return Calendar.getInstance().getTime();
    }

    public static boolean isEventActive(String dateStart){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateStart);
            if(getCurrentTime().before(date)){
                return true;
            }
        } catch(ParseException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public static void removeContainer(FragmentManager fm){
        fm.beginTransaction().remove(fm.findFragmentById(R.id.content_frame)).commit();
    }
}
