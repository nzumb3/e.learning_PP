package de.tudarmstadt.informatik.tudas.localdatabase.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import de.tudarmstadt.informatik.tudas.localdatabase.daos.AppointmentDao;
import de.tudarmstadt.informatik.tudas.localdatabase.daos.LabelDao;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.model.Label;
import de.tudarmstadt.informatik.tudas.utils.CalendarConverter;

/**
 * This class contains the room database object. It uses the singleton pattern, so there could only
 * be one database per runtime.
 *
 * The database refers to the following classes, i.e. tables:
 * Appointment
 * AppointmentContent
 * Label
 *
 * This class contains also the daos for querying the database
 */
@Database(entities = {Appointment.class, AppointmentContent.class, Label.class}, version = 1, exportSchema = false)
@TypeConverters({CalendarConverter.class})
public abstract class TheDatabase extends RoomDatabase {

    /**
     * Dao for querying appointments
     *
     * @return  AppointmentDao
     */
    public abstract AppointmentDao appointmentDao();

    /**
     * Dao for querying labels
     *
     * @return LabelDao
     */
    public abstract LabelDao labelDao();

    /**
     * Singleton instance of the database
     */
    private static volatile TheDatabase INSTANCE;

    /**
     * Returns the room database. If there doesn't exist one, a new instance is created.
     *
     * @param context       application's context
     * @return TheDatabase  a room database
     */
    public static TheDatabase getDatabase(final Context context) {
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


