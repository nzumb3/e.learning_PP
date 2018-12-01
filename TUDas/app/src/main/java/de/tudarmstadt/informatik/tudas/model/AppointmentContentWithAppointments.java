package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class AppointmentContentWithAppointments {

    @Embedded
    private AppointmentContent content;

    @Relation(parentColumn = "id", entityColumn = "appointment_content_id", entity = Appointment.class)
    private List<Appointment> appointments;
}
