package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {Appointment.class, AppointmentContent.class}, version = 1, exportSchema = false)
@TypeConverters({CalendarConverter.class})
public abstract class TheDatabase extends RoomDatabase {

    public abstract AppointmentDao appointmentDao();

    private static volatile TheDatabase INSTANCE;

    static TheDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TheDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TheDatabase.class, "tudas_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
