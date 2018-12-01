package de.tudarmstadt.informatik.tudas.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.util.Log;

import java.util.List;

@Dao
public abstract class AppointmentDao {

    void insert(AppointmentContentWithAppointments data) {
        long id = insert(data.getContent());
        if(data.getAppointments() != null)
            insertAppointmentsForContent(id, data.getAppointments());
    }

    @Insert
    abstract void _insertAll(List<Appointment> appointments);

    @Insert
    abstract long insert(AppointmentContent appointmentContent);

    void insertAppointmentsForContent(long contentId, List<Appointment> appointments) {
        for(Appointment appointment : appointments) {
            Log.d("ADebugTag", "Value: " + Long.toString(contentId));
            appointment.setAppointmentContentId(contentId);
        }

        _insertAll(appointments);
    }

    @Query("SELECT * FROM appointment_contents")
    abstract LiveData<List<AppointmentContent>> getAll();
}
