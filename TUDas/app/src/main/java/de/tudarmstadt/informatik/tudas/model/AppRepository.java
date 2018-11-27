package de.tudarmstadt.informatik.tudas.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class AppRepository {

    private AppointmentDao appointmentDao;

    private LiveData<List<Appointment>> allAppointsments;

    AppRepository(Application application) {
        TheDatabase database = TheDatabase.getDatabase(application);
        appointmentDao = database.appointmentDao();
        allAppointsments = appointmentDao.getAll();
    }

    LiveData<List<Appointment>> getAllAppointsments() {
        return allAppointsments;
    }

    public void insert(Appointment appointment) {
        new insertAsyncTask(appointmentDao).execute(appointment);
    }

    private static class insertAsyncTask extends AsyncTask<Appointment, Void, Void> {

        private AppointmentDao mAsyncTaskDao;

        insertAsyncTask(AppointmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Appointment... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
