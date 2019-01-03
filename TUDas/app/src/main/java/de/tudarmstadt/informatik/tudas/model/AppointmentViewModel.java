package de.tudarmstadt.informatik.tudas.model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {

    private AppRepository repository;

    private LiveData<Calendar> earliestBeginning;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'");

    public static final int pixelPerMinute = 3;

    public AppointmentViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        earliestBeginning = new MutableLiveData<>();
    }

    public void setEarliestBeginning(String startDate, String endDate) {
        earliestBeginning = Transformations.map(repository.getEarliestBeginningInPeriod(startDate, endDate), (time -> CalendarConverter.fromString(dateFormat.format(CalendarConverter.fromString(startDate).getTime()) + time)));
    }

    /*public LiveData<List<Appointment>> getAppointmentsInPeriod(String startDate, String endDate) {
        appointmentsFromDatabase = repository.getAppointmentsInPeriod(startDate, endDate);
        earliestBeginning = repository.getEarliestBeginningInPeriod(startDate, endDate);*/

        /*appointmentsForView = Transformations.switchMap(appointmentsFromDatabase, new Function<List<Appointment>, MutableLiveData<List<Appointment>>>() {
            @Override
            public MutableLiveData<List<Appointment>> apply(List<Appointment> input) {
                Timber.d("MyLog: In Transformations.map()");
                appointmentsForView.setValue(input);
                return appointmentsForView;
            }
        });*/
        /*appointmentsForView.addSource(appointmentsFromDatabase, (appointments -> {
            if(earliestBeginning != null && earliestBeginning.getValue() != null && appointmentsFromDatabase != null && appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty()) {
                appointmentsForView.setValue(fillList(appointmentsFromDatabase.getValue(), earliestBeginning.getValue()));
            }
        }));
        appointmentsForView.addSource(earliestBeginning, (calendar -> {
            if(earliestBeginning != null && earliestBeginning.getValue() != null && appointmentsFromDatabase != null && appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty()) {
                appointmentsForView.setValue(fillList(appointmentsFromDatabase.getValue(), earliestBeginning.getValue()));
            }
        }));*/
        /*return Transformations.switchMap(appointmentsFromDatabase, (appointments) -> {
            if(appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty())
                appointmentsForView.setValue(fillList(appointments, null));
            return appointmentsForView;
        });*/
        /*return appointmentsForView;
    }*/

    public LiveData<List<Appointment>> getAppointmentsForDay(String day) {
        LiveData<List<Appointment>> appointmentsFromDatabase = repository.getAppointmentsForDay(day);

        MediatorLiveData<List<Appointment>> appointmentsForView = new MediatorLiveData<>();
        appointmentsForView.addSource(appointmentsFromDatabase, (appointments -> {
            if(earliestBeginning != null && earliestBeginning.getValue() != null && appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty()) {
                appointmentsForView.setValue(fillList(appointmentsFromDatabase.getValue(), earliestBeginning.getValue()));
            }
        }));
        appointmentsForView.addSource(earliestBeginning, (calendar -> {
            if(earliestBeginning != null && earliestBeginning.getValue() != null && appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty()) {
                appointmentsForView.setValue(fillList(appointmentsFromDatabase.getValue(), earliestBeginning.getValue()));
            }
        }));
        return appointmentsForView;
    }

    private static List<Appointment> fillList(List<Appointment> appointments, Calendar earliestBeginning) {
        AppointmentContent emptyContent = new AppointmentContent();
        emptyContent.setRoom("");
        emptyContent.setAbbreviation("");
        emptyContent.setDescription("");
        emptyContent.setTitle("");
        emptyContent.setColor("#FFFFFF");

        List<Appointment> outputAppointments = new LinkedList<>();

        earliestBeginning = getMaxTimeBeforeAppointment(earliestBeginning);
        earliestBeginning.set(Calendar.DATE, appointments.get(0).getStartDate().get(Calendar.DATE));

        int minutes = Appointment.millisToMinute(diff(earliestBeginning, appointments.get(0).getStartDate()));
        if(minutes > 0) {
            outputAppointments.add(getAppointment(earliestBeginning, appointments.get(0).getStartDate(), emptyContent));
        }

        outputAppointments.add(appointments.get(0));

        for(int i = 1; i < appointments.size(); i++) {
            minutes = Appointment.millisToMinute(diff(appointments.get(i - 1).getEndDate(), appointments.get(i).getStartDate()));
            if(minutes > 0) {
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

    public void insert(AppointmentContent content, Appointment... appointments) {
        repository.insert(content, appointments);
    }

    private static long diff(Calendar first, Calendar second) {
        return second.getTimeInMillis() - first.getTimeInMillis();
    }

    private static Calendar getMaxTimeBeforeAppointment(Calendar earliestBeginning) {
        Calendar output = (Calendar) earliestBeginning.clone();
        output.set(Calendar.MINUTE, 0);
        output.set(Calendar.HOUR, Math.max(0, earliestBeginning.get(Calendar.HOUR) - 1));
        return output;
    }
}
