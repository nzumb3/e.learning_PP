package de.tudarmstadt.informatik.tudas.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Label;
import de.tudarmstadt.informatik.tudas.repositories.DataRepository;
import de.tudarmstadt.informatik.tudas.utils.LiveDataTransformations;
import timber.log.Timber;


public class ManageLabelsViewModel extends AndroidViewModel {

    private DataRepository repository;

    public ManageLabelsViewModel(Application application){
        super (application);
        repository = new DataRepository(application);
    }

    /*
    * Return the labels the user subscribed to.
    */
    public LiveData<List<String>> getLabels(){
        return repository.getLabels();
    }

    /*
    * Subscribe to a label.
    */
    public void insertLabel(Label label){
        repository.insert(label);
    }

    /*
    * Check if the subscription to a label was successful
    */
    public LiveData<Boolean> validateLabel(Label label) {
        LiveData<LiveDataTransformations.Tuple2<Boolean, List<String>>> intermediate = LiveDataTransformations.ifNotNull(repository.labelExists(label.getName()), repository.getLabelsWithName(label.getName()));
        return Transformations.map(intermediate, (tuple) -> tuple.first && tuple.second.isEmpty());
    }

    /*
    * Unsubscribe from a label and delete the label from the local database.
    */
    public void deleteLabel(String label) {
        repository.deleteLabel(label);
    }
}
