package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * This class represents a label. A label has a name.
 *
 * This class is persisted in a room database.
 */
@Entity(tableName = "labels", indices = {@Index(value = {"name"}, unique = true)})
public class Label {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    //Standard setters and getters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
