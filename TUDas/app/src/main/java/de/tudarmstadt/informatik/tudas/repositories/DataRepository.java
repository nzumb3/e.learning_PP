package de.tudarmstadt.informatik.tudas.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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

public class DataRepository {

    private AppointmentDao appointmentDao;

    private LabelDao labelDao;

    private LiveData<List<AppointmentContent>> allAppointments;

    private AppointmentService appointmentService;

    public DataRepository(Application application) {
        Timber.plant(new Timber.DebugTree());
        TheDatabase database = TheDatabase.getDatabase(application);
        appointmentDao = database.appointmentDao();
        labelDao = database.labelDao();
        allAppointments = appointmentDao.getAll();
        appointmentService = TudasServiceGenerator.createService(AppointmentService.class);
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

    public LiveData<List<String>> getLabels() {
        return labelDao.getLabels();
    }

    public LiveData<List<Appointment>> getDailyAppointmentsForLabel(String labelURL, String date) {
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

    public LiveData<List<Appointment>> getDailyAppointments(String date) {
        return Transformations.switchMap(getLabels(), (labels) -> {
            String labelURL = getLabelParameterURL(labels);

            return getDailyAppointmentsForLabel(labelURL, date);
        });


        /*LiveData<List<Appointment>> appointmentsFromServer = new MutableLiveData<>();
        appointmentService.getAppointments("test", date).enqueue(new Callback<List<Appointment>>() {
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
        });*/
        //Timber.d("MyLog: " + appointmentsFromServer);

        //return appointmentsFromServer;
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

    public void insert(final Label label){
        new insertLabelAsyncTask(labelDao).execute(label);
    }

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

    private static String getLabelParameterURL(List<String> labels) {
        if(labels == null || labels.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        builder.append(labels.get(0));

        for(int i = 1; i < labels.size(); i++)
            builder.append('/').append(labels.get(i));

        return builder.toString();
    }

    public LiveData<Boolean> labelExists(String label) {
        MutableLiveData<Boolean> output = new MutableLiveData<>();
        appointmentService.labelExists(label).enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                Timber.d("MyLog: " + response.body());
                output.setValue(response.body().getExists());
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {
                output.setValue(null);
            }
        });
        return output;
    }

    public LiveData<List<String>> getLabelsWithName(String label) {
        return labelDao.getLabelsWithName(label);
    }

    public class BooleanResult {
        private boolean exists;

        public boolean getExists() {
            return exists;
        }

        public void setExists(boolean exists) {
            this.exists = exists;
        }

        public String toString() {
            return "exists: " + exists;
        }
    }
}