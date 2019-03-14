package de.tudarmstadt.informatik.tudas.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "labels", indices = {@Index(value = {"name"}, unique = true)})
public class Label {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

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
