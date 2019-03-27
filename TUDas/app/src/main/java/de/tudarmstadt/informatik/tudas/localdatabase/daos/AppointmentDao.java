package de.tudarmstadt.informatik.tudas.localdatabase.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.model.AppointmentContentWithAppointments;

@Dao
public abstract class AppointmentDao {

    public void insert(AppointmentContentWithAppointments data) {
        long id = insert(data.getContent());
        if(data.getAppointments() != null)
            insertAppointmentsForContent(id, data.getContent(), data.getAppointments());
    }

    @Insert
    public abstract void _insertAll(List<Appointment> appointments);

    @Insert
    public abstract long insert(AppointmentContent appointmentContent);

    private void insertAppointmentsForContent(long contentId, AppointmentContent content, List<Appointment> appointments) {
        for(Appointment appointment : appointments) {
            appointment.setAppointmentContentId(contentId);
            appointment.setAppointmentContent(content);
        }

        _insertAll(appointments);
    }

    @Query("SELECT * FROM appointment_contents")
    public abstract LiveData<List<AppointmentContent>> getAll();

    @Query("SELECT * FROM appointments WHERE (DATE(start_date) >= DATE(:startDate) AND DATE(start_date) <= DATE(:endDate)) OR (DATE(end_date) >= DATE(:startDate) AND DATE(end_date) <= DATE(:endDate))")
    public abstract LiveData<List<Appointment>> getAppointmentsInPeriod(String startDate, String endDate);

    @Query("SELECT MIN(TIME(start_date)) FROM appointments WHERE DATE(start_date) <= DATE(:endDate) AND end_date >= DATE(:startDate)")
    public abstract LiveData<String> getEarliestBeginningInPeriod(String startDate, String endDate);

    @Query("SELECT MAX(TIME(end_date)) FROM appointments WHERE DATE(start_date) <= DATE(:endDate) AND end_date >= DATE(:startDate)")
    public abstract LiveData<String> getLatestEndingInPeriod(String startDate, String endDate);

    @Query("SELECT * FROM appointments WHERE DATE(start_date) == DATE(:date) OR DATE(end_date) == DATE(:date) OR (DATE(start_date) < DATE(:date) AND DATE(end_date) > DATE(:date)) ORDER BY start_date ASC, end_date DESC")
    public abstract LiveData<List<Appointment>> getAppointmentsForDay(String date);
}