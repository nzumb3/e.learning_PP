package de.tudarmstadt.informatik.tudas.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {

    private AppRepository repository;

    private LiveData<List<Appointment>> appointments;

    public AppointmentViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        appointments = repository.getAllAppointsments();
    }

    public LiveData<List<Appointment>> getAppointments() {
        return appointments;
    }

    public void insert(Appointment appointment) {
        repository.insert(appointment);
    }
}
