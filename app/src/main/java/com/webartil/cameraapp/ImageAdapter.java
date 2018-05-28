package com.webartil.cameraapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> implements
        ListPreloader.PreloadModelProvider<File> {

    private File imagesFile;
    private Context context;
    private OnImageClickListener listener;

    public ImageAdapter(Context context, File folderFile, OnImageClickListener onImageClickListener) {
        this.context = context;
        imagesFile = folderFile;
        listener = onImageClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_gallery_item_holder, parent, false);
        return new ViewHolder(view, listener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GlideApp.with(context)
                .load(imagesFile.listFiles()[position])
                .override(600, 600)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesFile.listFiles().length;
    }

    @NonNull
    @Override
    public List<File> getPreloadItems(final int position) {
        return new ArrayList<>(Arrays.asList(imagesFile.listFiles()))
                .subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull final File item) {
        return GlideApp.with(context)
                .load(item)
                .override(600, 600);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        OnImageClickListener listener;

        public ViewHolder(View view, OnImageClickListener onImageClickListener) {
            super(view);
            listener = onImageClickListener;
            imageView = view.findViewById(R.id.image_gallery_item);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            listener.onClickImage(getAdapterPosition());
        }
    }

    public interface OnImageClickListener{
        void onClickImage(int position);
    }

}
