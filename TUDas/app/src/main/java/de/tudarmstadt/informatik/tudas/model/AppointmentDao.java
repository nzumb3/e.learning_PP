package de.tudarmstadt.informatik.tudas.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public abstract class AppointmentDao {

    @Insert
    void insert(AppointmentContent content, Appointment... appointments) {

    }

    @Insert
    abstract void _insertAll(List<Appointment> appointments);

    @Insert
    abstract void insert(AppointmentContent appointmentContent);

    void insertAppointmentsForContent(AppointmentContent content, List<Appointment> appointments) {
        for(Appointment appointment : appointments)
            appointment.setAppointmentContentId(content.getId());

        _insertAll(appointments);
    }

    @Query("SELECT * FROM appointment_contents")
    abstract LiveData<List<AppointmentContent>> getAll();
}
