package de.tudarmstadt.informatik.tudas.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.informatik.tudas.localdatabase.daos.AppointmentDao;
import de.tudarmstadt.informatik.tudas.localdatabase.database.TheDatabase;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.model.AppointmentContentWithAppointments;

public class DataRepository {

    private AppointmentDao appointmentDao;

    private LiveData<List<AppointmentContent>> allAppointments;

    public DataRepository(Application application) {
        TheDatabase database = TheDatabase.getDatabase(application);
        appointmentDao = database.appointmentDao();
        allAppointments = appointmentDao.getAll();
    }

    LiveData<List<AppointmentContent>> getAllAppointments() {
        return allAppointments;
    }

    public LiveData<List<Appointment>> getAppointmentsInPeriod(String startDate, String endDate) {
        return appointmentDao.getAppointmentsInPeriod(startDate, endDate);
    }

    LiveData<String> getEarliestBeginningInPeriod(String startDate, String endDate) {
        return appointmentDao.getEarliestBeginningInPeriod(startDate, endDate);
    }

    LiveData<String> getLatestEndingInPeriod(String startDate, String endDate) {
        return appointmentDao.getLatestEndingInPeriod(startDate, endDate);
    }

    public LiveData<List<Appointment>> getAppointmentsForDay(String date) {
        return appointmentDao.getAppointmentsForDay(date);
    }

    public void insert(final AppointmentContent appointmentContent, final Appointment... appointments) {
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
