package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * This class is only used for inserting appointments with a corresponding appointment content into
 * the room database via the appointments dao.
 */
public class AppointmentContentWithAppointments {

    @Embedded
    private AppointmentContent content;

    /**
     * 1:n association from appointment content to appointments
     */
    @Relation(parentColumn = "id", entityColumn = "appointment_content_id", entity = Appointment.class)
    private List<Appointment> appointments;

    //Standard setters and getters

    public AppointmentContent getContent() {
        return content;
    }

    public void setContent(AppointmentContent content) {
        this.content = content;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
