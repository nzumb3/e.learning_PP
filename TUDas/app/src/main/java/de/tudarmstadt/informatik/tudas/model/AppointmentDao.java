package de.tudarmstadt.informatik.tudas.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AppointmentDao {

    @Insert
    void insert(Appointment... appointments);

    @Query("SELECT * FROM appointments")
    LiveData<List<Appointment>> getAll();

    @Query("DELETE FROM appointments")
    void deleteAll();
}
