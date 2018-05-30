package com.webartil.cameraapp.api;

import android.app.ProgressDialog;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.webartil.cameraapp.FullScreenImageActivity;

import java.io.File;

public class FirebaseApi {

    private static final String IMAGES_FOLDER = "images/";
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FirebaseApi() {
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
                        /*progressDialog.dismiss();
                        mViewModel.setImageUploaded(uploadFile.getAbsolutePath());
                        Toast.makeText(FullScreenImageActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();*/
                    })
                    .addOnFailureListener(e -> {
                        listener.addOnFailureListener(e);
                        /*progressDialog.dismiss();
                        Toast.makeText(FullScreenImageActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();*/
                    });
        }
    }

    public interface UploadListener{
        void addOnSuccessListener();
        void addOnFailureListener(Exception e);
    }
}
