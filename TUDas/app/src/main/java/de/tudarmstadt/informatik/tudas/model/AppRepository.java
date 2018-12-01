package de.tudarmstadt.informatik.tudas.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class AppRepository {

    private AppointmentDao appointmentDao;

    private LiveData<List<AppointmentContent>> allAppointsments;

    AppRepository(Application application) {
        TheDatabase database = TheDatabase.getDatabase(application);
        appointmentDao = database.appointmentDao();
        allAppointsments = appointmentDao.getAll();
    }

    LiveData<List<AppointmentContent>> getAllAppointsments() {
        return allAppointsments;
    }

    public void insert(final AppointmentContent appointmentContent, final Appointment... appointments) {
        new insertAsyncTask(appointmentDao).execute(new InsertParameters(appointmentContent, appointments));
    }

    private static class InsertParameters {
        AppointmentContent content;

        Appointment[] appointments;

        InsertParameters(AppointmentContent content, Appointment... appointments) {
            this.content = content;
            this.appointments = appointments;
        }
    }

    private static class insertAsyncTask extends AsyncTask<InsertParameters, Void, Void> {

        private AppointmentDao mAsyncTaskDao;

        insertAsyncTask(AppointmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final InsertParameters... params) {
            mAsyncTaskDao.insert(params[0].content, params[0].appointments);
            return null;
        }
    }
}
