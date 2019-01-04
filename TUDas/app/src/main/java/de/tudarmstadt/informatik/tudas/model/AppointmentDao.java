package de.tudarmstadt.informatik.tudas.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
abstract class AppointmentDao {

    void insert(AppointmentContentWithAppointments data) {
        long id = insert(data.getContent());
        if(data.getAppointments() != null)
            insertAppointmentsForContent(id, data.getContent(), data.getAppointments());
    }

    @Insert
    abstract void _insertAll(List<Appointment> appointments);

    @Insert
    abstract long insert(AppointmentContent appointmentContent);

    private void insertAppointmentsForContent(long contentId, AppointmentContent content, List<Appointment> appointments) {
        for(Appointment appointment : appointments) {
            appointment.setAppointmentContentId(contentId);
            appointment.setAppointmentContent(content);
        }

        _insertAll(appointments);
    }

    @Query("SELECT * FROM appointment_contents")
    abstract LiveData<List<AppointmentContent>> getAll();

    @Query("SELECT * FROM appointments WHERE start_date <= :endDate AND end_date >= :startDate")
    abstract LiveData<List<Appointment>> getAppointmentsInPeriod(String startDate, String endDate);

    @Query("SELECT MIN(TIME(start_date)) FROM appointments WHERE DATE(start_date) <= DATE(:endDate) AND end_date >= DATE(:startDate)")
    abstract LiveData<String> getEarliestBeginningInPeriod(String startDate, String endDate);

    @Query("SELECT MAX(TIME(end_date)) FROM appointments WHERE DATE(start_date) <= DATE(:endDate) AND end_date >= DATE(:startDate)")
    abstract LiveData<String> getLatestEndingInPeriod(String startDate, String endDate);

    @Query("SELECT * FROM appointments WHERE DATE(start_date) == :date OR DATE(end_date) == :date OR (DATE(start_date) < :date AND DATE(end_date) > :date) ORDER BY start_date ASC, end_date DESC")
    abstract LiveData<List<Appointment>> getAppointmentsForDay(String date);
}
