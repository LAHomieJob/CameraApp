package com.webartil.cameraapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.webartil.cameraapp.api.firebaseApi.FirebaseDatabaseApi;
import com.webartil.cameraapp.api.firebaseApi.FirebaseStorageApi;
import com.webartil.cameraapp.api.LocalStorageApi;
import com.webartil.cameraapp.database.AppDatabase;
import com.webartil.cameraapp.database.ImageDao;
import com.webartil.cameraapp.database.ImageModel;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository {

    private ImageDao imageDao;
    private LocalStorageApi mLocalStorageApi;
    private FirebaseStorageApi mFirebaseStorageApi;
    private FirebaseDatabaseApi mFirebaseDatabaseApi;

    public Repository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mLocalStorageApi = new LocalStorageApi(application);
        mFirebaseStorageApi = new FirebaseStorageApi();
        mFirebaseDatabaseApi = new FirebaseDatabaseApi();
        imageDao= db.mImageDao();
    }

    public File createTemporaryImageFile() throws IOException {
        return mLocalStorageApi.createTemporaryImageFile();
    }

    public void uploadImage(File uploadFile, FirebaseStorageApi.UploadListener listener){
        mFirebaseStorageApi.uploadImage(uploadFile, listener);
    }

    public void uploadCommentInformation(ImageModel imageModel){
        mFirebaseDatabaseApi.uploadCommentInformation(imageModel);
    }

    public LiveData<List<ImageModel>> getAllImageModels(){
        return imageDao.getAllImageModels();
    }
    public void insert(ImageModel imageModel) {
        new InsertAsyncTask(imageDao).execute(imageModel);
    }

    public void addComment(String filePath, String comment){
        new AddCommentAsyncTask(imageDao).execute(filePath, comment);
    }

    public void setImageUploaded(String filePath) {
        new SetImageUploadedAsyncTask(imageDao).execute(filePath);
    }

    public ImageModel getImageModelByFilePath(String filePath) {
        try {
            return new GetImageModelByFilePathAsyncTask(imageDao).execute(filePath).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<ImageModel> getLiveDataImageModelByFilePath(String filePath){
        return imageDao.getLiveDataImageModelByFilePath(filePath);
    }

    private static class InsertAsyncTask extends AsyncTask<ImageModel, Void, Void> {

        private ImageDao mAsyncTaskDao;

        InsertAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ImageModel... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class AddCommentAsyncTask extends AsyncTask<String, Void, Void> {

        private ImageDao mAsyncTaskDao;

        AddCommentAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.addComment(params[0], params[1]);
            return null;
        }
    }

    private static class SetImageUploadedAsyncTask extends AsyncTask<String, Void, Void> {

        private ImageDao mAsyncTaskDao;

        SetImageUploadedAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.setImageUploaded(params[0]);
            return null;
        }
    }

    private static class GetImageModelByFilePathAsyncTask extends AsyncTask<String, Void, ImageModel> {

        private ImageDao mAsyncTaskDao;

        GetImageModelByFilePathAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected ImageModel doInBackground(String... params) {
            return mAsyncTaskDao.getImageModelByFilePath(params[0]);
        }

        @Override
        protected void onPostExecute(ImageModel imageModel) {
            super.onPostExecute(imageModel);
        }
    }
}
