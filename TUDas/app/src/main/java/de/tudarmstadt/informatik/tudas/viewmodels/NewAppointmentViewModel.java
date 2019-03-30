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

    /*
    * Is called to insert an appointment into the local database. An appointment consists
    * of the appointment itself and an appointmentcontent, which holds the description and other
    * related fields.
    */
    public void insert(AppointmentContent content, Appointment... appointments){
        repository.insert(content, appointments);
    }
}
