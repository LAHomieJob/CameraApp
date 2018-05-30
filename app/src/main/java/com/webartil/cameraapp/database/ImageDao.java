package com.webartil.cameraapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert
    void insert(ImageModel imageModel);

    @Query("SELECT * FROM images")
    LiveData<List<ImageModel>> getAllImageModels();

    @Query("UPDATE images SET comment= :comment WHERE file_path= :filePath")
    void addComment(String filePath, String comment);

    @Query("UPDATE images SET uploaded= 1 WHERE file_path= :filePath")
    void setImageUploaded(String filePath);

    @Query("SELECT * FROM images WHERE file_path= :filePath")
    ImageModel getImageModelByFilePath(String filePath);

    @Query("SELECT * FROM images WHERE file_path = :filePath")
    LiveData<ImageModel> getLiveDataImageModelByFilePath (String filePath);
}
