package com.webartil.cameraapp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.webartil.cameraapp.database.ImageModel;
import com.webartil.cameraapp.repository.Repository;

public class ViewModel extends AndroidViewModel {

    private Repository repository;

    public ViewModel(@NonNull final Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void insert(ImageModel imageModel) { repository.insert(imageModel); }

    public void addComment (String fileName, String comment) {
        repository.addComment(fileName, comment);
    }

    public void setImageUploaded(String fileName) {
        repository.setImageUploaded(fileName);
    }

    public ImageModel getImageModelByFileName(String fileName){
        return repository.getImageModelByFileName(fileName);
    }

    public LiveData<ImageModel> getLiveDataImageModelByFileName(String fileName){
        return repository.getLiveDataImageModelByFileName(fileName);
    }
}
