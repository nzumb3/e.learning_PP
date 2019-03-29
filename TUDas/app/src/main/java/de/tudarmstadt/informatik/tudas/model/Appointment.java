package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

/**
 * This class represents an appointment. An appointment has a start- and an enddate and is belonngs
 * to an appointment content.
 *
 * This class is persisted in a room database. It includes a foreign key to the appointment contents.
 */
@Entity(tableName = "appointments", foreignKeys = @ForeignKey(entity = AppointmentContent.class, parentColumns = "id", childColumns = "appointment_content_id"), indices = {@Index("appointment_content_id")})
public class Appointment {

    /**
     * The primary key in the database table
     */
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "start_date")
    private Calendar startDate;

    @ColumnInfo(name = "end_date")
    private Calendar endDate;

    @ColumnInfo(name = "appointment_content_id")
    private long appointmentContentId;

    /**
     * The appointment content loaded from the appointment content entity
     */
    @Embedded(prefix = "content_")
    private AppointmentContent appointmentContent;

    /**
     * This flag is used for the daily appointments view and denotes, if this appointment overlaps
     * with an event, that is saved in another calendar app of the user's phone
     */
    @Ignore
    private boolean overlap;

    // Standard setters and getters

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

    public long getAppointmentContentId() {
        return appointmentContentId;
    }

    public void setAppointmentContentId(long appointmentContentId) {
        this.appointmentContentId = appointmentContentId;
    }

    public AppointmentContent getAppointmentContent() {
        return appointmentContent;
    }

    public void setAppointmentContent(AppointmentContent appointmentContent) {
        this.appointmentContent = appointmentContent;
    }

    public boolean overlap() {
        return overlap;
    }

    public void setOverlap(boolean overlap) {
        this.overlap = overlap;
    }

    /**
     * Returns the duration of this appointment in minutes before midnight.
     *
     * @return int  the duration before midnight in minutes
     */
    public int getDurationBeforeMidnight() {
        long millis;
        if(atSameDay())
            millis = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        else {
            Calendar midnight = (Calendar) startDate.clone();
            midnight.add(Calendar.DAY_OF_MONTH, 1);
            midnight.set(Calendar.HOUR_OF_DAY, 0);
            midnight.set(Calendar.MINUTE, 0);
            millis = midnight.getTimeInMillis() - startDate.getTimeInMillis();
        }

        return millisToMinute(millis);
    }

    /**
     * This method returns true, if this appointment ends at the same day as it starts.
     *
     * @return boolean  true, if start- and enddate are on the same day
     */
    public boolean atSameDay() {
        return  startDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR) &&
                startDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH) &&
                startDate.get(Calendar.DAY_OF_MONTH) == endDate.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * This method converts milliseconds to minutes.
     *
     * @param millis    The milliseconds, that shall be converted into minutes
     * @return int      Amount of minutes calculated from the milliseconds
     */
    public static int millisToMinute(long millis) {
        return (int) (millis / 1000 / 60);
    }
}
