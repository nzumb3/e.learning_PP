package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.time.DayOfWeek;
import java.util.GregorianCalendar;

@Database(entities = {Appointment.class}, version = 1, exportSchema = false)
@TypeConverters({DayOfWeekConverter.class, TimeConverter.class})
public abstract class TheDatabase extends RoomDatabase {

    public abstract AppointmentDao appointmentDao();

    private static volatile TheDatabase INSTANCE;

    static TheDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TheDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TheDatabase.class, "database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static TheDatabase.Callback sRoomDatabaseCallback =
            new TheDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppointmentDao mDao;

        PopulateDbAsync(TheDatabase db) {
            mDao = db.appointmentDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Appointment word = new Appointment();
            /*word.setStartDay(null);
            word.setEndDay(null);*/
            word.setStartTime(new GregorianCalendar(0, 0, 0, 11, 40));
            word.setEndTime(new GregorianCalendar(0, 0, 0, 13, 20));
            mDao.insert(word);
            return null;
        }
    }
}
