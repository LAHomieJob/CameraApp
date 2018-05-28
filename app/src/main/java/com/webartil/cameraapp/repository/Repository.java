package com.webartil.cameraapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.webartil.cameraapp.database.AppDatabase;
import com.webartil.cameraapp.database.ImageDao;
import com.webartil.cameraapp.database.ImageModel;

public class Repository {

    ImageDao imageDao;

    public Repository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        imageDao= db.mImageDao();
    }

    public void insert(ImageModel imageModel) {
        new InsertAsyncTask(imageDao).execute(imageModel);
    }

    public void addComment(String name, String comment){
        new AddCommentAsyncTask(imageDao).execute(name, comment);
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

}
