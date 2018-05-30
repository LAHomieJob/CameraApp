package com.webartil.cameraapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.webartil.cameraapp.database.ImageModel;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> implements
        ListPreloader.PreloadModelProvider<ImageModel> {

    private Context context;
    private OnImageClickListener listener;
    private List<ImageModel> imageModels;

    public ImageAdapter(Context context, OnImageClickListener onImageClickListener) {
        this.context = context;
        listener = onImageClickListener;
    }

    public void setImageModels(List<ImageModel> imageModels) {
        this.imageModels = imageModels;
        notifyDataSetChanged();
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
        String filePath = imageModels.get(position).getFilePath();
        int uploadBookmark = imageModels.get(position).getUpload();
        GlideApp.with(context)
                .load(filePath)
                .override(600, 600)
                .into(holder.imageView);
        if (uploadBookmark == 1) {
            holder.bookmark.setVisibility(View.VISIBLE);
        } else {
            holder.bookmark.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return imageModels != null ? imageModels.size() : 0;
    }

    @NonNull
    @Override
    public List<ImageModel> getPreloadItems(final int position) {
        return new ArrayList<>(imageModels).subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(final ImageModel item) {
        return GlideApp.with(context)
                .load(item.getFilePath())
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
