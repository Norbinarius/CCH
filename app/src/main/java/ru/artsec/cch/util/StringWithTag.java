package ru.artsec.cch.util;

/**
 * Created by Norbinarius on 28.02.2018.
 */

public class StringWithTag {
    public String name;
    public String id;
    public String time;

    public StringWithTag(String stringPart, String tagPart, String timePart) {
        name = stringPart;
        id = tagPart;
        time = timePart;
    }

    @Override
    public String toString() {
        return name;
    }
}