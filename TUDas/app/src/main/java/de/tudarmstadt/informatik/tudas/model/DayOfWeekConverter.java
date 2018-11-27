package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.TypeConverter;

import java.time.DayOfWeek;

public class DayOfWeekConverter {

    @TypeConverter
    public static DayOfWeek toDayOfWeek(String dayOfWeek) {
        return dayOfWeek == null ? null : DayOfWeek.valueOf(dayOfWeek);
    }

    @TypeConverter
    public static String toString(DayOfWeek dayOfWeek) {
        return dayOfWeek == null ? null : dayOfWeek.toString();
    }
}
