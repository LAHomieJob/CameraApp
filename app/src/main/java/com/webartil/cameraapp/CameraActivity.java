package com.webartil.cameraapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity implements ImageAdapter.OnImageClickListener {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    public static final String PATH = "PATH";
    private String GALLERY_LOCATION = "image gallery";
    private File mGalleryFolder;
    private RecyclerView mRecyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        createImageGallery();
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
        imageAdapter = new ImageAdapter(this, mGalleryFolder, this);
        FixedPreloadSizeProvider<File> preloadSizeProvider =
                new FixedPreloadSizeProvider<>(600, 600);
        RecyclerViewPreloader<File> preloader = new RecyclerViewPreloader<>
                (Glide.with(this), imageAdapter, preloadSizeProvider, 10);
        mRecyclerView.setAdapter(imageAdapter);
        mRecyclerView.addOnScrollListener(preloader);
    }

    public void takePhoto() {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        RecyclerView.Adapter newImageAdapter = new ImageAdapter(this, mGalleryFolder, this);
        mRecyclerView.swapAdapter(newImageAdapter, false);
    }

    File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        return File.createTempFile(imageFileName, ".jpg", mGalleryFolder);
    }

    private void createImageGallery() {
        mGalleryFolder =
                new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        GALLERY_LOCATION);
        if (!mGalleryFolder.exists()) {
            mGalleryFolder.mkdirs();
        }
    }

    @Override
    public void onClickImage(final int position) {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.putExtra(PATH, position);
        startActivity(intent);
    }
}
