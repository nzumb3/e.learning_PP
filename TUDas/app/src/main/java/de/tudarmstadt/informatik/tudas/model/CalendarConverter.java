package de.tudarmstadt.informatik.tudas.model;

import android.annotation.SuppressLint;
import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarConverter {

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat inputFormat = new SimpleDateFormat("dd.MM.yy hh:mm");

    @TypeConverter
    public static Calendar fromString(String date) {
        Calendar output = Calendar.getInstance();
        try {
            output.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }

    @TypeConverter
    public static String fromCalendar(Calendar date) {
        return format.format(date.getTime());
    }

    public static Calendar fromInputString(String date) {
        Calendar output = Calendar.getInstance();
        try {
            output.setTime(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
    }
}
