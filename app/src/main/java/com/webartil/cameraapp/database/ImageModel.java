package com.webartil.cameraapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Entity(tableName = "images")
public class ImageModel {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "file_path")
    @Exclude private String filePath;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "uploaded")
    @Exclude private int upload;

    public ImageModel(final String filePath) {
        this.filePath = filePath;
        this.upload = 0;
    }

    public ImageModel() {
    }

    @NonNull
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(@NonNull final String filePath) {
        this.filePath = filePath;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(final int upload) {
        this.upload = upload;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }
}
