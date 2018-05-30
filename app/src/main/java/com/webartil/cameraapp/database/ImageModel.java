package com.webartil.cameraapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "images")
public class ImageModel {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "file_path")
    private String filePath;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "uploaded")
    private int upload;

    public ImageModel(final String filePath) {
        this.filePath = filePath;
        this.upload = 0;
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

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public void setUpload(final int upload) {
        this.upload = upload;
    }
}
