package com.webartil.cameraapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface ImageDao {

    @Insert
    void insert(ImageModel imageModel);

    @Query("UPDATE images SET comment= :comment WHERE file_name= :fileName")
    void addComment(String fileName, String comment);

    @Query("SELECT * FROM images WHERE file_name = :fileName")
    ImageModel getImageModelByFileName (String fileName);

    @Query("SELECT * FROM images WHERE file_name = :fileName")
    LiveData<ImageModel> getLiveDataImageModelByFileName (String fileName);
}
