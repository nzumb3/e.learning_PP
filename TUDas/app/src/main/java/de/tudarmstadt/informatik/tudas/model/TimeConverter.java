package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeConverter {

    @TypeConverter
    public static int toInt(Calendar time) {
        if(time == null)
            return 0;
        int hour = time.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int minute = time.get(Calendar.MINUTE);
        return hour * 60 + minute;
    }

    @TypeConverter
    public static Calendar toDate(int time) {
        return new GregorianCalendar(0, 0, 0, time / 60, time % 60);
    }
}
