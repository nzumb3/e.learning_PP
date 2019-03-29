package de.tudarmstadt.informatik.tudas.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import de.tudarmstadt.informatik.tudas.localdatabase.daos.AppointmentDao;
import de.tudarmstadt.informatik.tudas.localdatabase.daos.LabelDao;
import de.tudarmstadt.informatik.tudas.localdatabase.database.TheDatabase;
import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.model.AppointmentContentWithAppointments;
import de.tudarmstadt.informatik.tudas.model.AppointmentService;
import de.tudarmstadt.informatik.tudas.model.Label;
import de.tudarmstadt.informatik.tudas.model.TudasServiceGenerator;
import de.tudarmstadt.informatik.tudas.utils.LiveDataTransformations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * This class combines the different datasources and provides methods for querying the data.
 * There are the local room database of this application and the shared database on the server via
 * a REST API.
 */
public class DataRepository {

    private AppointmentDao appointmentDao;

    private LabelDao labelDao;

    private AppointmentService appointmentService;

    public DataRepository(Application application) {
        Timber.plant(new Timber.DebugTree());
        TheDatabase database = TheDatabase.getDatabase(application);
        appointmentDao = database.appointmentDao();
        labelDao = database.labelDao();
        appointmentService = TudasServiceGenerator.createService(AppointmentService.class);
    }

    /**
     * Returns the appointments in the given period from the database.
     */
    public LiveData<List<Appointment>> getAppointmentsInPeriod(String startDate, String endDate) {
        return appointmentDao.getAppointmentsInPeriod(startDate, endDate);
    }

    /**
     * Returns the appointments at the given date from the database.
     */
    private LiveData<List<Appointment>> getAppointmentsForDay(String date) {
        return appointmentDao.getAppointmentsForDay(date);
    }

    /**
     * Returns the labels from the database.
     */
    public LiveData<List<String>> getLabels() {
        return labelDao.getLabels();
    }

    /**
     * Combines the appointments at the given date from the database with the appointments at the
     * given date with the given label string from the server.
     */
    private LiveData<List<Appointment>> getDailyAppointmentsForLabel(String labelURL, String date) {
        LiveData<List<Appointment>> appointmentsFromServer = new MutableLiveData<>();
        appointmentService.getAppointments(date, labelURL).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                ((MutableLiveData<List<Appointment>>) appointmentsFromServer).setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Timber.d("MyLog: Error on requesting appointments from server");
            }
        });
        LiveData<List<Appointment>> appointmentsFromDatabase = getAppointmentsForDay(date);

        LiveData<LiveDataTransformations.Tuple2<List<Appointment>, List<Appointment>>> intermediate = LiveDataTransformations.ifNotNull(appointmentsFromServer, appointmentsFromDatabase);

        return Transformations.map(intermediate, (tuple) -> {
            List<Appointment> _appointmentsFromServer = tuple.first;
            List<Appointment> _appointmentsFromDatabase = tuple.second;

            List<Appointment> output = new ArrayList<>();

            output.addAll(_appointmentsFromServer);
            output.addAll(_appointmentsFromDatabase);

            Collections.sort(output, (appointment1, appointment2) -> {
                if(appointment1.getStartDate().compareTo(appointment2.getStartDate()) == 0)
                    return appointment1.getEndDate().compareTo(appointment2.getEndDate());
                return appointment1.getStartDate().compareTo(appointment2.getStartDate());
            });

            return output;
        });
    }

    /**
     * Returns the appointments at the given date from the database and the server.
     */
    public LiveData<List<Appointment>> getDailyAppointments(String date) {
        return Transformations.switchMap(getLabels(), (labels) -> {
            String labelURL = getLabelParameterURL(labels);

            return getDailyAppointmentsForLabel(labelURL, date);
        });
    }

    /**
     * Inserts a appointment content with some appointments to this content.
     */
    public void insert(final AppointmentContent appointmentContent, final Appointment... appointments) {
        AppointmentContentWithAppointments data = new AppointmentContentWithAppointments();
        data.setContent(appointmentContent);
        data.setAppointments(Arrays.asList(appointments));
        new insertAsyncTask(appointmentDao).execute(data);
    }

    /**
     * Private class for handling the asynchronous insertion of an appointment content.
     */
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

    /**
     * Inserts a label.
     */
    public void insert(final Label label){
        new insertLabelAsyncTask(labelDao).execute(label);
    }

    /**
     * Private class for handling the asynchronous insertion of a label.
     */
    private static class insertLabelAsyncTask extends AsyncTask<Label, Void, Void>{

        private LabelDao mLabelDao;

        insertLabelAsyncTask(LabelDao dao) {
            mLabelDao = dao;
        }

        @Override
        protected Void doInBackground(final Label... params) {
            mLabelDao.insertLabel(params[0]);
            return null;
        }
    }

    /**
     * Returns a label string for querying the REST API depending on the saved labels in the
     * database.
     */
    private static String getLabelParameterURL(List<String> labels) {
        if(labels == null || labels.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        builder.append(labels.get(0));

        for(int i = 1; i < labels.size(); i++)
            builder.append('/').append(labels.get(i));

        return builder.toString();
    }

    /**
     * Checks if the given label exists on the server.
     */
    public LiveData<Boolean> labelExists(String label) {
        MutableLiveData<Boolean> output = new MutableLiveData<>();
        appointmentService.labelExists(label).enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                output.setValue(response.body().getExists());
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {
                output.setValue(null);
            }
        });
        return output;
    }

    /**
     * Returns the labels with the given name from the database.
     */
    public LiveData<List<String>> getLabelsWithName(String label) {
        return labelDao.getLabelsWithName(label);
    }

    /**
     * Deletes the label in the database with the given name.
     */
    public void deleteLabel(String label) {
        new deleteLabelAsyncTask(labelDao).execute(label);
    }

    /**
     * Wrapper class for a boolean value if the label exists.
     * It is used in combination with the REST API call.
     */
    public class BooleanResult {
        private boolean exists;

        boolean getExists() {
            return exists;
        }

        public void setExists(boolean exists) {
            this.exists = exists;
        }

        public String toString() {
            return "exists: " + exists;
        }
    }

    /**
     * Private class for handling the asynchronous deletion of a label in the database.
     */
    private static class deleteLabelAsyncTask extends AsyncTask<String, Void, Void> {
        private LabelDao labelDao;

        deleteLabelAsyncTask(LabelDao labelDao) {
            this.labelDao = labelDao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            labelDao.delete(params[0]);
            return null;
        }
    }

    /**
     * Deletes a single appointment.
     */
    public void deleteAppointment(Appointment appointment) {
        new deleteAppointmentAsyncTask(appointmentDao).execute(appointment);
    }

    /**
     * Private class for handling the asynchronous deletion of an appointment.
     */
    private static class deleteAppointmentAsyncTask extends AsyncTask<Appointment, Void, Void> {
        private AppointmentDao appointmentDao;

        deleteAppointmentAsyncTask(AppointmentDao appointmentDao) {
            this.appointmentDao = appointmentDao;
        }

        @Override
        protected Void doInBackground(final Appointment... params) {
            AppointmentContent content = params[0].getAppointmentContent();
            appointmentDao.delete(params[0]);
            appointmentDao.delete(content);
            return null;
        }
    }
}