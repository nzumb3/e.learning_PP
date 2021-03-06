package de.tudarmstadt.informatik.tudas.utils;

import android.annotation.SuppressLint;
import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class converts a calendar object into a string representation and vice versa.
 */
public class CalendarConverter {

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @TypeConverter
    public static Calendar fromString(String date) {
        if(date == null)
            return null;

        Calendar output = Calendar.getInstance();
        try {
            output.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static String toDateString(Calendar date) {
        return dateFormat.format(date.getTime());
    }

    @TypeConverter
    public static String fromCalendar(Calendar date) {
        return format.format(date.getTime());
    }
}
