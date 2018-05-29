package com.webartil.cameraapp;


import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webartil.cameraapp.viewModel.ViewModel;


public class ImageFragment extends Fragment {

    public interface ImageFragmentListener {
        void onTouchImage();

        void onClickImage();
    }

    public static final String IMAGE_NAME_DATA = "Image name data";
    private String fileName;
    private ImageFragmentListener mListener;
    private ImageView mImageView;
    private TextView textViewComment;
    private ViewModel mViewModel;

    public static ImageFragment createInstance(String imagePath) {
        ImageFragment instance = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_NAME_DATA, imagePath);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ImageFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " ImageFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileName = getArguments().getString(IMAGE_NAME_DATA);
        mViewModel = ViewModelProviders.of(this).get(ViewModel.class);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_container, container, false);
        mImageView = view.findViewById(R.id.place_image);
        textViewComment = view.findViewById(R.id.text_comment);
        mImageView.setOnClickListener(v -> mListener.onClickImage());
        mImageView.setOnTouchListener((v, event) -> {
            mListener.onTouchImage();
            return false;
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getLiveDataImageModelByFileName(fileName)
                .observe(this, imageModel -> {
                    if (imageModel.getComment() != null) {
                        String comment = imageModel.getComment();
                        textViewComment.setText(comment);
                    }
                });
        Glide.with(this)
                // TODO: 29.05.2018 delete hardcode
                .load("/storage/emulated/0/Android/data/com.webartil.cameraapp/files/Pictures/image gallery/" + fileName)
                .into(mImageView);
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.with(this).clear(mImageView);
    }
}
