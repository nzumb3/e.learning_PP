package de.tudarmstadt.informatik.tudas.localdatabase.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Label;

/**
 * This interface defines the database operations for the labels table.
 *
 * There are the following operations
 * insertLabel: insert a single label
 * getLabels: returns all labels in the database
 * getLabelsWithName: returns all labels with the given name
 * delete: deletes a label with the given name
 */
@Dao
public interface LabelDao {

    @Insert
    void insertLabel(Label label);

    @Query("SELECT name FROM labels")
    LiveData<List<String>> getLabels();

    @Query("SELECT name FROM labels WHERE name == :label")
    LiveData<List<String>> getLabelsWithName(String label);

    @Query("DELETE FROM labels WHERE name == :label")
    void delete(String label);
}
