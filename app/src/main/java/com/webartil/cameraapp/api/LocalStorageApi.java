package com.webartil.cameraapp.api;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalStorageApi {

    private Context context;
    private File imageFolder;
    private final static String galleryLocation = "image gallery";

    public LocalStorageApi(final Context context) {
        this.context = context;
        this.imageFolder = createExternalImageFolder();
    }

    public File getLocalImageFolder() {
        return imageFolder;
    }

    public String getFileNameFromLocalFolderByPosition(int listPosition) {
        return imageFolder.listFiles()[listPosition].getName();
    }

    public File getFileFromFolderByPosition(int listPosition) {
        return imageFolder.listFiles()[listPosition];
    }

    public File createTemporaryImageFile() throws IOException {
        String imageFileName = generateDataImageName();
        return File.createTempFile(imageFileName, ".jpg", imageFolder);
    }

    private String generateDataImageName(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        return imageFileName;
    }

    private File createExternalImageFolder() {
        File mGalleryFolder =
                new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        galleryLocation);
        if (!mGalleryFolder.exists()) {
            mGalleryFolder.mkdirs();
        }
        return mGalleryFolder;
    }
}
