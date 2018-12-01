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

    @TypeConverter
    public Calendar fromString(String date) throws ParseException {
        Calendar output = Calendar.getInstance();
        output.setTime(format.parse(date));
        return output;
    }

    @TypeConverter
    public String fromCalendar(Calendar date) {
        return format.format(date.getTime());
    }
}
