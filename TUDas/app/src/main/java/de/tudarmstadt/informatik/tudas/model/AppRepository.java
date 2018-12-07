package de.tudarmstadt.informatik.tudas.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.Arrays;
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

    LiveData<List<AppointmentContentWithAppointments>> getAppointmentsInPeriod(String startDate, String endDate) {
        return appointmentDao.getAppointmentsInPeriod(startDate, endDate);
    }

    public void insert(final AppointmentContent appointmentContent, final Appointment... appointments) {
        AppointmentContentWithAppointments data = new AppointmentContentWithAppointments();
        data.setContent(appointmentContent);
        data.setAppointments(Arrays.asList(appointments));
        new insertAsyncTask(appointmentDao).execute(data);
    }

    private static class InsertParameters {
        AppointmentContent content;

        Appointment[] appointments;

        InsertParameters(AppointmentContent content, Appointment... appointments) {
            this.content = content;
            this.appointments = appointments;
        }
    }

    private static class insertAsyncTask extends AsyncTask<AppointmentContentWithAppointments, Void, Void> {

        private AppointmentDao mAsyncTaskDao;

        insertAsyncTask(AppointmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AppointmentContentWithAppointments... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
