package com.webartil.cameraapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.webartil.cameraapp.viewModel.ViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> implements
        ListPreloader.PreloadModelProvider<File> {

    private Context context;
    private OnImageClickListener listener;
    private ViewModel viewModel;
    private File imagesFiles;

    public ImageAdapter
            (Context context, OnImageClickListener onImageClickListener, ViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
        listener = onImageClickListener;
        this.imagesFiles = viewModel.getLocalImageFolder();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_gallery_item_holder, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String filePath = imagesFiles.listFiles()[position].getAbsolutePath();
        String fileName = imagesFiles.listFiles()[position].getName();
        GlideApp.with(context)
                .load(filePath)
                .override(600, 600)
                .into(holder.imageView);
        try {
            int uploadBookmark = viewModel.getImageModelByFileName(fileName).getUpload();
            Log.d("BOOKMARK", String.valueOf(uploadBookmark));
            if (uploadBookmark == 1) {
                holder.bookmark.setVisibility(View.VISIBLE);
            } else {
                holder.bookmark.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return imagesFiles.listFiles().length;
    }

    @NonNull
    @Override
    public List<File> getPreloadItems(final int position) {
        return new ArrayList<>(Arrays.asList(imagesFiles.listFiles()))
                .subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(final File item) {
        return GlideApp.with(context)
                .load(item)
                .override(600, 600);
    }


    public interface OnImageClickListener {
        void onClickImage(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        ImageView bookmark;
        OnImageClickListener listener;

        ViewHolder(View view, OnImageClickListener onImageClickListener) {
            super(view);
            listener = onImageClickListener;
            imageView = view.findViewById(R.id.image_gallery_item);
            bookmark = view.findViewById(R.id.bookmark);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            listener.onClickImage(getAdapterPosition());
        }
    }

}
