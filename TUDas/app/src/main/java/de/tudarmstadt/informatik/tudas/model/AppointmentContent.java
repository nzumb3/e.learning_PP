package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.List;

@Entity(tableName = "appointment_contents")
public class AppointmentContent {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @Relation(parentColumn = "id", entityColumn = "appointment_content_id")
    private List<Appointment> realAppointments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Appointment> getRealAppointments() {
        return realAppointments;
    }

    public void setRealAppointments(List<Appointment> realAppointments) {
        this.realAppointments = realAppointments;
    }

    @Override
    public String toString() {
        return title + " - " + (description.length() > 30 ? description.substring(0, 29) + "..." : description);
    }
}
