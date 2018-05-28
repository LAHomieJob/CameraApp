package com.webartil.cameraapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class ImageFragment extends Fragment {

    public interface ImageFragmentListener {
        void onTouchImage();

        void onClickImage();
    }

    public static final String IMAGE_PATH_DATA = "Image path data";
    private String imagePath;
    private ImageFragmentListener mListener;
    private ImageView mImageView;
    private TextView textViewComment;

    public static ImageFragment createInstance(String imagePath) {
        ImageFragment instance = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_PATH_DATA, imagePath);
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
        imagePath = getArguments().getString(IMAGE_PATH_DATA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_container, container, false);
        mImageView = view.findViewById(R.id.place_image);
        textViewComment = view.findViewById(R.id.text_comment);
        textViewComment.setText("LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOLLLLLLLLLLLLLLLLLLLLLLL");
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
        Glide.with(this)
                .load("/storage/emulated/0/Android/data/com.webartil.cameraapp/files/Pictures/image gallery/" + imagePath)
                .into(mImageView);
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.with(this).clear(mImageView);
    }
}
