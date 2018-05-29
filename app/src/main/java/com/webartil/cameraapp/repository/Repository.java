package com.webartil.cameraapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.webartil.cameraapp.database.AppDatabase;
import com.webartil.cameraapp.database.ImageDao;
import com.webartil.cameraapp.database.ImageModel;

import java.util.concurrent.ExecutionException;

public class Repository {

    private ImageDao imageDao;

    public Repository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        imageDao= db.mImageDao();
    }

    public void insert(ImageModel imageModel) {
        new InsertAsyncTask(imageDao).execute(imageModel);
    }

    public void addComment(String fileName, String comment){
        new AddCommentAsyncTask(imageDao).execute(fileName, comment);
    }

    public void setImageUploaded(String fileName) {
        new SetImageUploadedAsyncTask(imageDao).execute(fileName);
    }

    public ImageModel getImageModelByFileName(String fileName) {
        try {
            return new GetImageModelByFileNameAsyncTask(imageDao).execute(fileName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<ImageModel> getLiveDataImageModelByFileName(String fileName){
        return imageDao.getLiveDataImageModelByFileName(fileName);
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

    private static class GetImageModelByFileNameAsyncTask extends AsyncTask<String, Void, ImageModel> {

        private ImageDao mAsyncTaskDao;

        GetImageModelByFileNameAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected ImageModel doInBackground(String... params) {
            return mAsyncTaskDao.getImageModelByFileName(params[0]);
        }

        @Override
        protected void onPostExecute(ImageModel imageModel) {
            super.onPostExecute(imageModel);
        }
    }
}
