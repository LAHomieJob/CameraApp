package com.webartil.cameraapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "images")
public class ImageModel {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "file_name")
    private String name;

    @ColumnInfo(name = "comment")
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public ImageModel(final String name) {
        this.name = name;
    }
}
