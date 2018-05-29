package com.webartil.cameraapp.api;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseApi {

    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FirebaseApi() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

}
