package de.tudarmstadt.informatik.tudas.model;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class AppointmentViewModel extends AndroidViewModel {

    private AppRepository repository;

    private MutableLiveData<List<Appointment>> appointmentsForView;

    private LiveData<List<Appointment>> appointmentsFromDatabase;

    private LiveData<Calendar> earliestBeginning;

    public static final int pixelPerMinute = 3;

    public AppointmentViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        appointmentsForView = new MutableLiveData<>();
    }

    public MutableLiveData<List<Appointment>> getAppointmentsForView() {
        return appointmentsForView;
    }

    public LiveData<List<Appointment>> getAppointmentsInPeriod(String startDate, String endDate) {
        /*((MediatorLiveData<List<Appointment>>) appointmentsForView).removeSource(appointmentsFromDatabase);
        ((MediatorLiveData<List<Appointment>>) appointmentsForView).removeSource(earliestBeginning);*/
        appointmentsFromDatabase = repository.getAppointmentsInPeriod(startDate, endDate);
        earliestBeginning = repository.getEarliestBeginningInPeriod(startDate, endDate);
        Timber.d("MyLog: Is" + (appointmentsFromDatabase.getValue() == null));

        /*appointmentsForView = Transformations.switchMap(appointmentsFromDatabase, new Function<List<Appointment>, MutableLiveData<List<Appointment>>>() {
            @Override
            public MutableLiveData<List<Appointment>> apply(List<Appointment> input) {
                Timber.d("MyLog: In Transformations.map()");
                appointmentsForView.setValue(input);
                return appointmentsForView;
            }
        });*/
        return Transformations.switchMap(appointmentsFromDatabase, (appointments) -> {
            if(appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty())
                appointmentsForView.setValue(fillList(appointments, null));
            return appointmentsForView;
        });
        /*if(appointmentsFromDatabase.getValue() != null)
            appointmentsForView.setValue(fillList(appointmentsFromDatabase.getValue(), null));*/

        //earliestBeginning = repository.getEarliestBeginningInPeriod(startDate, endDate);
        //addSourceToMediatorLiveData();

        //return appointmentsFromDatabase;//appointmentsForView;
    }

    /*private void addSourceToMediatorLiveData() {
        ((MediatorLiveData<List<Appointment>>) appointmentsForView).addSource(appointmentsFromDatabase, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {
                if(appointments != null && !appointments.isEmpty() && earliestBeginning != null && earliestBeginning.getValue() != null)
                    ((MediatorLiveData<List<Appointment>>) appointmentsForView).setValue(fillList(appointments, earliestBeginning.getValue()));
            }
        });
        ((MediatorLiveData<List<Appointment>>) appointmentsForView).addSource(earliestBeginning, new Observer<Calendar>() {
            @Override
            public void onChanged(@Nullable Calendar calendar) {
                if(calendar != null && appointmentsFromDatabase != null && appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty())
                    ((MediatorLiveData<List<Appointment>>) appointmentsForView).setValue(fillList(appointmentsFromDatabase.getValue(), calendar));
            }
        });
    }*/

    private static List<Appointment> fillList(List<Appointment> appointments, Calendar earliestBeginning) {
        AppointmentContent emptyContent = new AppointmentContent();
        emptyContent.setRoom("");
        emptyContent.setAbbreviation("");
        emptyContent.setDescription("");
        emptyContent.setTitle("");

        List<Appointment> outputAppointments = new LinkedList<>();

        /*int mins = Appointment.millisToMinute(diff(earliestBeginning, appointments.get(0).getStartDate()));
        if(mins > 0) {
            outputAppointments.add(getAppointment(earliestBeginning, appointments.get(0).getStartDate(), emptyContent));
        }*/
        int mins;

        outputAppointments.add(appointments.get(0));

        for(int i = 1; i < appointments.size(); i++) {
            mins = Appointment.millisToMinute(diff(appointments.get(i - 1).getEndDate(), appointments.get(i).getStartDate()));
            if(mins > 0) {
                outputAppointments.add(getAppointment(appointments.get(i - 1).getEndDate(), appointments.get(i).getStartDate(), emptyContent));
            }

            outputAppointments.add(appointments.get(i));
        }

        return outputAppointments;
    }

    private static Appointment getAppointment(Calendar startDate, Calendar endDate, AppointmentContent content) {
        Appointment output = new Appointment();
        output.setStartDate(startDate);
        output.setEndDate(endDate);
        output.setAppointmentContent(content);
        return output;
    }

    public LiveData<Calendar> getEarliestBeginningInPeriod(String startDate, String endDate) {
        return repository.getEarliestBeginningInPeriod(startDate, endDate);
    }

    public void insert(AppointmentContent content, Appointment... appointments) {
        repository.insert(content, appointments);
    }

    private static long diff(Calendar first, Calendar second) {
        return second.getTimeInMillis() - first.getTimeInMillis();
    }
}
