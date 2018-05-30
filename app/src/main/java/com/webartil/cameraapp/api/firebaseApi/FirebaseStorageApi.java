package com.webartil.cameraapp.api.firebaseApi;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FirebaseStorageApi {

    private static final String IMAGES_FOLDER = "images/";
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FirebaseStorageApi() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void uploadImage(File uploadFile, UploadListener listener) {
        Uri filePath = Uri.fromFile(uploadFile);
        if (filePath != null) {
            StorageReference ref = storageReference.child(IMAGES_FOLDER + uploadFile.getName());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        listener.addOnSuccessListener();
                    })
                    .addOnFailureListener(e -> {
                        listener.addOnFailureListener(e);
                    });
        }
    }

    public interface UploadListener{
        void addOnSuccessListener();
        void addOnFailureListener(Exception e);
    }
}
