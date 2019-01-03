package de.tudarmstadt.informatik.tudas.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

public class AppRepository {

    private AppointmentDao appointmentDao;

    private LiveData<List<AppointmentContent>> allAppointments;

    AppRepository(Application application) {
        TheDatabase database = TheDatabase.getDatabase(application);
        appointmentDao = database.appointmentDao();
        allAppointments = appointmentDao.getAll();
    }

    LiveData<List<AppointmentContent>> getAllAppointments() {
        return allAppointments;
    }

    LiveData<List<Appointment>> getAppointmentsInPeriod(String startDate, String endDate) {
        return appointmentDao.getAppointmentsInPeriod(startDate, endDate);
    }

    LiveData<String> getEarliestBeginningInPeriod(String startDate, String endDate) {
        return appointmentDao.getEarliestBeginningInPeriod(startDate, endDate);
    }

    LiveData<String> getLatestEndingInPeriod(String startDate, String endDate) {
        return appointmentDao.getLatestEndingInPeriod(startDate, endDate);
    }

    LiveData<List<Appointment>> getAppointmentsForDay(String date) {
        return appointmentDao.getAppointmentsForDay(date);
    }

    void insert(final AppointmentContent appointmentContent, final Appointment... appointments) {
        AppointmentContentWithAppointments data = new AppointmentContentWithAppointments();
        data.setContent(appointmentContent);
        data.setAppointments(Arrays.asList(appointments));
        new insertAsyncTask(appointmentDao).execute(data);
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
