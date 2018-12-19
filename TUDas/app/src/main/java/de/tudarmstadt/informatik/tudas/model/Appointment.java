package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

//TODO Take into account, that an appointment could go over multiple days -> duration = whole day
@Entity(tableName = "appointments", foreignKeys = @ForeignKey(entity = AppointmentContent.class, parentColumns = "id", childColumns = "appointment_content_id"), indices = {@Index("appointment_content_id")})
public class Appointment {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "start_date")
    private Calendar startDate;

    @ColumnInfo(name = "end_date")
    private Calendar endDate;

    @ColumnInfo(name = "appointment_content_id")
    private long appointmentContentId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    //TODO Use DateFormat
    @Override
    public String toString() {
        return startDate.get(Calendar.DAY_OF_MONTH) + "." + startDate.get(Calendar.MONTH) + "." + startDate.get(Calendar.YEAR) + " " + startDate.get(Calendar.HOUR_OF_DAY) + ":" + startDate.get(Calendar.MINUTE) + " - " + endDate.get(Calendar.HOUR_OF_DAY) + ":" + endDate.get(Calendar.MINUTE);
    }

    public long getAppointmentContentId() {
        return appointmentContentId;
    }

    public void setAppointmentContentId(long appointmentContentId) {
        this.appointmentContentId = appointmentContentId;
    }

    /**
     * Returns the duration of this appointment in minutes before midnight.
     * @return
     */
    public int getDurationBeforeMidnight() {
        long millis;
        if(atSameDay())
            millis = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        else {
            Calendar midnight = (Calendar) startDate.clone();
            midnight.add(Calendar.DAY_OF_MONTH, 1);
            midnight.set(Calendar.HOUR, 0);
            midnight.set(Calendar.MINUTE, 0);
            millis = midnight.getTimeInMillis() - startDate.getTimeInMillis();
        }

        return millisToMinute(millis);
    }

    public int getDurationAfterMidnight() {
        long millis;
        if(atSameDay())
            millis = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        else {
            Calendar midnight = (Calendar) endDate.clone();
            midnight.set(Calendar.HOUR, 0);
            midnight.set(Calendar.MINUTE, 0);
            millis = endDate.getTimeInMillis() - midnight.getTimeInMillis();
        }

        return millisToMinute(millis);
    }

    private boolean atSameDay() {
        return  startDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR) &&
                startDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH) &&
                startDate.get(Calendar.DAY_OF_MONTH) == endDate.get(Calendar.DAY_OF_MONTH);
    }

    private static int millisToMinute(long millis) {
        return (int) (millis / 1000 / 60);
    }
}
