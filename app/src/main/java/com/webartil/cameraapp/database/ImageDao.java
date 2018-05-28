package com.webartil.cameraapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface ImageDao {

    @Insert
    void insert(ImageModel imageModel);

    @Query("UPDATE images SET comment= :comment WHERE name= :name")
    void addComment(String name, String comment);
}
