package de.tudarmstadt.informatik.tudas.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.repositories.DataRepository;

public class DailyAppointmentsViewModel extends AndroidViewModel {
    private DataRepository repository;

    public DailyAppointmentsViewModel(Application application) {
        super(application);

        repository = new DataRepository(application);
    }

    public LiveData<List<Appointment>> getAppointmentsForDay(String day) {
        //return repository.getAppointmentsForDay(day);
        return repository.getDailyAppointments(day);
    }

    public LiveData<List<String>> getLabels() {
        return repository.getLabels();
    }

    public void insert(AppointmentContent content, Appointment... appointments) {
        repository.insert(content, appointments);
    }
}