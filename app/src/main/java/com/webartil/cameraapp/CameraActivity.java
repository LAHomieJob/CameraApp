package com.webartil.cameraapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.webartil.cameraapp.api.LocalStorageApi;
import com.webartil.cameraapp.database.ImageModel;
import com.webartil.cameraapp.viewModel.ViewModel;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements ImageAdapter.OnImageClickListener {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    public static final String PATH = "PATH";
    public static final String FILE_NAME = "FILE_NAME";
    private LocalStorageApi mApiInstance;
    private ViewModel mViewModel;
    private File tempPhotoFile;
    private RecyclerView mRecyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mApiInstance = new LocalStorageApi(this); //initialize Api with predefined folder
        mViewModel = ViewModelProviders.of(this).get(ViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar_activity_camera);
        setSupportActionBar(toolbar);
        initRecyclerView();
        FloatingActionButton fab = findViewById(R.id.fab_shoot_photo);
        fab.setOnClickListener(view -> takePhoto());
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.gallery_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter
                (this, mApiInstance.getGeneratedImageFolder(), this);
        FixedPreloadSizeProvider<File> preloadSizeProvider =
                new FixedPreloadSizeProvider<>(600, 600);
        RecyclerViewPreloader<File> preloader = new RecyclerViewPreloader<>
                (Glide.with(this), imageAdapter, preloadSizeProvider, 10);
        mRecyclerView.setAdapter(imageAdapter);
        mRecyclerView.addOnScrollListener(preloader);
    }

    private void takePhoto() {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempPhotoFile = mApiInstance.createTemporaryImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempPhotoFile));
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.d(FILE_NAME, tempPhotoFile.getName());
        mViewModel.insert(new ImageModel(tempPhotoFile.getName()));
        RecyclerView.Adapter newImageAdapter = new ImageAdapter
                (this, mApiInstance.getGeneratedImageFolder(), this);
        mRecyclerView.swapAdapter(newImageAdapter, false);
    }

    @Override
    public void onClickImage(final int position) {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.putExtra(PATH, position);
        startActivity(intent);
    }
}
