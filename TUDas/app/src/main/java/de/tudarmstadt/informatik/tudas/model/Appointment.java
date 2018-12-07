package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

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

    //TODO
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
}
