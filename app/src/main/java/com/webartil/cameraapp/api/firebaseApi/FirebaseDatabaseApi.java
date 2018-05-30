package com.webartil.cameraapp.api.firebaseApi;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webartil.cameraapp.database.ImageModel;

import java.io.File;

public class FirebaseDatabaseApi {

    private final static String ROOT_REFERENCE = "images";
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabaseReference;

    public FirebaseDatabaseApi() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference(ROOT_REFERENCE);
    }

    public void uploadCommentInformation(ImageModel imageModel){
        //Create id from name of image
        String fileNameId = generateFileNameIdFromModel(imageModel);
        mDatabaseReference.child(fileNameId).setValue(imageModel);
    }

    @NonNull
    private String generateFileNameIdFromModel(final ImageModel imageModel) {
        String filePath = imageModel.getFilePath();
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1, filePath.length());
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

}
