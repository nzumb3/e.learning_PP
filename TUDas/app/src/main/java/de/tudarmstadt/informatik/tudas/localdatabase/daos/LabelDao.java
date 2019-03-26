package de.tudarmstadt.informatik.tudas.localdatabase.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Label;

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
