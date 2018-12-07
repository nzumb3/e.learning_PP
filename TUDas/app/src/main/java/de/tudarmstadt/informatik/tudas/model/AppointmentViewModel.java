package de.tudarmstadt.informatik.tudas.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.Calendar;
import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {

    private AppRepository repository;

    private LiveData<List<AppointmentContent>> appointments;

    public AppointmentViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        appointments = repository.getAllAppointsments();
    }

    public LiveData<List<AppointmentContent>> getAppointments() {
        return appointments;
    }

    public LiveData<List<AppointmentContentWithAppointments>> getAppointmentsInPeriod(String startDate, String endDate) {
        return repository.getAppointmentsInPeriod(startDate, endDate);
    }

    public void insert(AppointmentContent content, Appointment... appointments) {
        repository.insert(content, appointments);
    }
}
