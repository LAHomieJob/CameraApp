package com.webartil.cameraapp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.webartil.cameraapp.api.firebaseApi.FirebaseStorageApi;
import com.webartil.cameraapp.database.ImageModel;
import com.webartil.cameraapp.repository.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ViewModel extends AndroidViewModel {

    private Repository repository;

    public ViewModel(@NonNull final Application application) {
        super(application);
        repository = new Repository(application);
    }

    public File createTemporaryImageFile() throws IOException {
        return repository.createTemporaryImageFile();
    }

    public void uploadImage(File uploadFile, FirebaseStorageApi.UploadListener listener) {
        repository.uploadImage(uploadFile, listener);
    }

    public void uploadCommentInformation(ImageModel imageModel) {
        repository.uploadCommentInformation(imageModel);
    }

    public LiveData<List<ImageModel>> getAllImageModels() {
        return repository.getAllImageModels();
    }

    public void insert(ImageModel imageModel) {
        repository.insert(imageModel);
    }

    public void addComment(String filePath, String comment) {
        repository.addComment(filePath, comment);
    }

    public void setImageUploaded(String filePath) {
        repository.setImageUploaded(filePath);
    }

    public ImageModel getImageModelByFilePath(String filePath) {
        return repository.getImageModelByFilePath(filePath);
    }

    public LiveData<ImageModel> getLiveDataImageModelByFilePath(String filePath) {
        return repository.getLiveDataImageModelByFilePath(filePath);
    }
}
