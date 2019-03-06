package de.tudarmstadt.informatik.tudas.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.repositories.DataRepository;

public class NewAppointmentViewModel extends AndroidViewModel {
    private DataRepository repository;

    public NewAppointmentViewModel(Application application){
        super(application);

        repository = new DataRepository(application);
    }

    public void insert(AppointmentContent content, Appointment... appointments){
        repository.insert(content, appointments);
    }
}
