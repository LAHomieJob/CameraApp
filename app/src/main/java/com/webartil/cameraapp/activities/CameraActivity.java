package com.webartil.cameraapp.activities;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.webartil.cameraapp.adapters.ImageAdapter;
import com.webartil.cameraapp.R;
import com.webartil.cameraapp.database.ImageModel;
import com.webartil.cameraapp.viewModel.ViewModel;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements ImageAdapter.OnImageClickListener {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private static final String POSITION = "Position of photo in grid";
    private ViewModel mViewModel;
    private File tempPhotoFile;
    private RecyclerView mRecyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = findViewById(R.id.toolbar_activity_camera);
        setSupportActionBar(toolbar);
        mViewModel = ViewModelProviders.of(this).get(ViewModel.class);
        mViewModel.getAllImageModels()
                .observe(this, imageModels -> {
                    imageAdapter.setImageModels(imageModels);
                });
        initRecyclerView();
        FloatingActionButton fab = findViewById(R.id.fab_shoot_photo);
        fab.setOnClickListener(view -> takePhoto());
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.gallery_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(this, this);
        FixedPreloadSizeProvider<ImageModel> preloadSizeProvider = new FixedPreloadSizeProvider<>(600, 600);
        RecyclerViewPreloader<ImageModel> preloader = new RecyclerViewPreloader<>
                (Glide.with(this), imageAdapter, preloadSizeProvider, 10);
        mRecyclerView.setAdapter(imageAdapter);
        mRecyclerView.addOnScrollListener(preloader);
    }

    private void takePhoto() {
        Intent callCameraApplicationIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempPhotoFile = mViewModel.createTemporaryImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempPhotoFile));
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA_APP) {
            if (resultCode == RESULT_OK) {
                mViewModel.insert(new ImageModel(tempPhotoFile.getAbsolutePath()));
            } else {
                /*delete empty temporary file if the user closes
                the camera without shooting photo*/
                tempPhotoFile.delete();
            }
        }
    }

    @Override
    public void onClickImage(final int position) {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.putExtra(POSITION, position);
        startActivity(intent);
    }
}
