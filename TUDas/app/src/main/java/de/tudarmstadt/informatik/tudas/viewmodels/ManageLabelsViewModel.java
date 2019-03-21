package de.tudarmstadt.informatik.tudas.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Label;
import de.tudarmstadt.informatik.tudas.repositories.DataRepository;


public class ManageLabelsViewModel extends AndroidViewModel {

    private DataRepository repository;

    public ManageLabelsViewModel(Application application){
        super (application);

        repository = new DataRepository(application);
    }

    public LiveData<List<String>> getLabels(){
        return repository.getLabels();
    }

    public void insertLabel(Label label){
        //TODO: Validation
        repository.insert(label);
    }
}
