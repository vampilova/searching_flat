package com.example.ewigkeit.searching_flat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ewigkeit.searching_flat.DatabaseHelper;
import com.example.ewigkeit.searching_flat.R;
import com.example.ewigkeit.searching_flat.Tables.Estate;
import com.example.ewigkeit.searching_flat.Tables.Photos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Photos> photos = new ArrayList<>();
    private OnDeleteListener onDeleteListener;
    private Context context;
    private DatabaseHelper databaseHelper;

    public ImageGalleryAdapter(Context context, List<Photos> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_view, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final NewsViewHolder viewHolder = (NewsViewHolder) holder;

        if (photos.size()!=0) {
            Glide.with(context)
                    .load(photos.get(position).getUrl())
                    .into(viewHolder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_photo)
        public ImageView imageView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface OnDeleteListener {
        void onDelete(Estate vacancy);
    }


}
