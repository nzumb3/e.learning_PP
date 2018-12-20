package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Calendar;

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
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

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
            AppointmentContent test1 = new AppointmentContent();
            test1.setTitle("Appointment 1");
            test1.setDescription("Hier mache ich etwas. Wühüüü");
            test1.setAbbreviation("App1");
            test1.setRoom("S202/C205");
            Appointment app1 = new Appointment();
            Calendar start1 = Calendar.getInstance();
            start1.set(2018,12,24,11,40);
            Calendar end1 = Calendar.getInstance();
            end1.set(2018,12,24,13, 10);
            app1.setStartDate(start1);
            app1.setEndDate(end1);
            AppointmentContentWithAppointments input1 = new AppointmentContentWithAppointments();
            input1.setContent(test1);
            input1.setAppointments(Arrays.asList(new Appointment[]{app1}));
            mDao.insert(input1);

            AppointmentContent test2 = new AppointmentContent();
            test2.setTitle("Appointment 2");
            test2.setDescription("Hier mache ich etwas anderes. Wühüüü!?!");
            test2.setAbbreviation("App2");
            test2.setRoom("S103/220");
            Appointment app2 = new Appointment();
            Calendar start2 = Calendar.getInstance();
            start2.set(2018,12,24,17,15);
            Calendar end2 = Calendar.getInstance();
            end2.set(2018, 12, 24, 18, 55);
            app2.setStartDate(start2);
            app2.setEndDate(end2);
            AppointmentContentWithAppointments input2 = new AppointmentContentWithAppointments();
            input2.setContent(test2);
            input2.setAppointments(Arrays.asList(new Appointment[]{app2}));
            mDao.insert(input2);

            AppointmentContent test3 = new AppointmentContent();
            test3.setTitle("Appointment 3");
            test3.setDescription("Was gibts zu essen?!!!!!?");
            test3.setAbbreviation("App3");
            test3.setRoom("S101/A101");
            Appointment app3 = new Appointment();
            Calendar start3 = Calendar.getInstance();
            start3.set(2018,12,25,11,40);
            Calendar end3 = Calendar.getInstance();
            end3.set(2018,12,25,12, 15);
            app3.setStartDate(start3);
            app3.setEndDate(end3);
            AppointmentContentWithAppointments input3 = new AppointmentContentWithAppointments();
            input3.setContent(test3);
            input3.setAppointments(Arrays.asList(new Appointment[]{app3}));
            mDao.insert(input3);

            AppointmentContent test4 = new AppointmentContent();
            test4.setTitle("Appointment 4");
            test4.setDescription("Essen hat geschmeckt! ;)");
            test4.setAbbreviation("App4");
            test4.setRoom("S308/311");
            Appointment app4= new Appointment();
            Calendar start4 = Calendar.getInstance();
            start4.set(2018,12,25,19,0);
            Calendar end4 = Calendar.getInstance();
            end4.set(2018,12,26,8,0);
            app4.setStartDate(start4);
            app4.setEndDate(end4);
            AppointmentContentWithAppointments input4 = new AppointmentContentWithAppointments();
            input4.setContent(test4);
            input4.setAppointments(Arrays.asList(new Appointment[]{app4}));
            mDao.insert(input4);
            return null;
        }
    }
}


