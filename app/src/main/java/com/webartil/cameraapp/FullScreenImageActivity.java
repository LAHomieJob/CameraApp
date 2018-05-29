package com.webartil.cameraapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.webartil.cameraapp.viewModel.ViewModel;

import java.io.File;
import java.util.Objects;

public class FullScreenImageActivity extends AppCompatActivity
        implements ImageFragment.ImageFragmentListener,
        CommentAlertDialog.DialogListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    public static final String POSITION = "Position of photo in grid";
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = this::hide;

    private static final String COMMENT_DIALOG = "Comment dialog";
    private ViewModel mViewModel;
    private ViewPager mViewPager;
    private CommentAlertDialog dialog;
    private int position;
    StorageMetadata metadata;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private void setPosition(final int position) {
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        mVisible = true;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mViewModel = ViewModelProviders.of(this).get(ViewModel.class);
        mViewPager = findViewById(R.id.image_pager);
        position = getIntent().getIntExtra(POSITION, 0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                setPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        initToolbar();
        setImage();
    }


    private void setImage() {
        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position, false);
    }

    private void initToolbar() {
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_fullscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_comment:
                showCommentDialog();
                return true;
            case R.id.action_upload:
                uploadImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void uploadImage() {
        File uploadedFile = mViewModel.getFileFromLocalFolderByPosition(position);
        Uri filePath = Uri.fromFile(uploadedFile);
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + uploadedFile.getName());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        mViewModel.setImageUploaded(uploadedFile.getName());
                        Toast.makeText(FullScreenImageActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(FullScreenImageActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        }
    }

    private void showCommentDialog() {
        dialog = new CommentAlertDialog();
        dialog.show(getSupportFragmentManager(), COMMENT_DIALOG);
    }

    @Override
    public void onDialogPositiveClick(final DialogFragment dialog) {
        EditText commentBox = dialog.getDialog().findViewById(R.id.text_comment);
        if (TextUtils.isEmpty(commentBox.getText())) {
            dialog.dismiss();
        } else {
            String fileName = mViewModel.getFileNameFromLocalFolderByPosition(position);
            String comment = commentBox.getText().toString();
            mViewModel.addComment(fileName, comment);
            StorageReference ref = storageReference.child("images/" + fileName);
            metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .setCustomMetadata("UserComment", comment)
                    .build();
            ref.updateMetadata(metadata)
                    .addOnFailureListener(exception -> {
                        Log.d("ERROR", "Upload comment");
                    });
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onTouchImage() {
        // Set up the user interaction to manually show or hide the system UI.
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS);
        }
    }

    @Override
    public void onClickImage() {
        /*Upon interacting with UI controls, delay any scheduled hide()
        operations to prevent the jarring behavior of controls going away
        while interacting with the UI.*/
        toggle();
    }


    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            setPosition(position);
            return ImageFragment.createInstance(mViewModel.getFileNameFromLocalFolderByPosition(position));
        }

        @Override
        public int getCount() {
            return mViewModel.getLocalImageFolder().list().length;
        }
    }
}
