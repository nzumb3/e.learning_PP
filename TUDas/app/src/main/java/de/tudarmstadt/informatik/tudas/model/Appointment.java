package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.time.DayOfWeek;
import java.util.Calendar;

@Entity(tableName = "appointments")
public class Appointment {

    @PrimaryKey(autoGenerate = true)
    private int id;

    /*@ColumnInfo(name = "start_day")
    private DayOfWeek startDay;

    @ColumnInfo(name = "end_day")
    private DayOfWeek endDay;*/

    @ColumnInfo(name = "start_time")
    private Calendar startTime;

    @ColumnInfo(name = "end_time")
    private Calendar endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public DayOfWeek getStartDay() {
        return startDay;
    }

    public void setStartDay(DayOfWeek startDay) {
        this.startDay = startDay;
    }

    public DayOfWeek getEndDay() {
        return endDay;
    }

    public void setEndDay(DayOfWeek endDay) {
        this.endDay = endDay;
    }*/

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return startTime.get(Calendar.HOUR_OF_DAY) + ":" + startTime.get(Calendar.MINUTE) + " - " + endTime.get(Calendar.HOUR_OF_DAY) + ":" + endTime.get(Calendar.MINUTE);
    }
}
