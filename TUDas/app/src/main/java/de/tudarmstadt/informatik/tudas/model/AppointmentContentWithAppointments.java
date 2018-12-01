package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class AppointmentContentWithAppointments {

    @Embedded
    private AppointmentContent content;

    @Relation(parentColumn = "id", entityColumn = "appointment_content_id", entity = Appointment.class)
    private List<Appointment> appointments;

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
