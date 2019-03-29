package de.tudarmstadt.informatik.tudas.localdatabase.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.model.AppointmentContentWithAppointments;

/**
 * This class defines the database operations for the appointments and appointments content table.
 *
 * There are the following operations:
 * insert: insert an appointment content with appointments
 * getAppointmentsInPeriod: Returns all appointments in the given period
 * getAppointmentsForDay: Returns all appointments at the given date
 * delete: delete the given appointment resp. the given appointment content
 */
@Dao
public abstract class AppointmentDao {

    public void insert(AppointmentContentWithAppointments data) {
        long id = insert(data.getContent());
        if(data.getAppointments() != null)
            insertAppointmentsForContent(id, data.getContent(), data.getAppointments());
    }

    @Insert
    protected abstract void _insertAll(List<Appointment> appointments);

    @Insert
    protected abstract long insert(AppointmentContent appointmentContent);

    /**
     * Private method that inserts all appointments for a given appointment content.
     * @param contentId     primary key of the appointment content
     * @param content       appointment content of the appointments
     * @param appointments  appointments that should be inserted
     */
    private void insertAppointmentsForContent(long contentId, AppointmentContent content, List<Appointment> appointments) {
        for(Appointment appointment : appointments) {
            appointment.setAppointmentContentId(contentId);
            appointment.setAppointmentContent(content);
        }

        _insertAll(appointments);
    }

    /**
     * Start- and enddate should be the output of CalendarConverter.fromCalendar().
     */
    @Query("SELECT * FROM appointments WHERE (DATE(start_date) >= DATE(:startDate) AND DATE(start_date) <= DATE(:endDate)) OR (DATE(end_date) >= DATE(:startDate) AND DATE(end_date) <= DATE(:endDate))")
    public abstract LiveData<List<Appointment>> getAppointmentsInPeriod(String startDate, String endDate);

    @Query("SELECT * FROM appointments WHERE DATE(start_date) == DATE(:date) OR DATE(end_date) == DATE(:date) OR (DATE(start_date) < DATE(:date) AND DATE(end_date) > DATE(:date)) ORDER BY start_date ASC, end_date DESC")
    public abstract LiveData<List<Appointment>> getAppointmentsForDay(String date);

    @Delete
    public abstract void delete(Appointment appointment);

    @Delete
    public abstract void delete(AppointmentContent appointmentContent);
}