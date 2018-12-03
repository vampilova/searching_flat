package com.example.ewigkeit.searching_flat.Adapters;

/**
 * Created by ewigkeit on 19/11/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ewigkeit.searching_flat.App;
import com.example.ewigkeit.searching_flat.DatabaseHelper;
import com.example.ewigkeit.searching_flat.R;
import com.example.ewigkeit.searching_flat.Tables.Address;
import com.example.ewigkeit.searching_flat.Tables.Estate;
import com.example.ewigkeit.searching_flat.Tables.Photos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Estate> estates = new ArrayList<>();
    private OnDeleteListener onDeleteListener;
    private Context context;
    private DatabaseHelper databaseHelper;

    public PageAdapter(Context context, List<Estate> estates) {
        this.context = context;
        this.estates = estates;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_some_data, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final NewsViewHolder viewHolder = (NewsViewHolder) holder;
        databaseHelper = App.getDatabaseInstance();
        viewHolder.balcon.setText("Наличие балкона: "+ (estates.get(position).getBalcon()==1 ?"Есть":"Нет") );
        viewHolder.square.setText("Площадь: "+ estates.get(position).getSquare());
        Address address = databaseHelper.getAddressDao().getAddressById(estates.get(position).getId());
        viewHolder.cost.setText(estates.get(position).getCost()+" руб.");
        viewHolder.district.setText("Район: "+databaseHelper.getDistrictDao().getDistrictById(address.getDistrictId()).getDescription());
        viewHolder.flatType.setText("Тип квартиры: "+databaseHelper.getFlatTypeDao().getFlatTypeById(estates.get(position).getFlatTypeId()).getDescription());

        viewHolder.repairType.setText("Тип ремонта: "+databaseHelper.getRepairTypeDao().getRepairTypeById(estates.get(position).getRepairTypeId()).getDescription());
        viewHolder.roomNumber.setText("Кол-во комнат: "+estates.get(position).getRoomNumber());
        if (databaseHelper.getPhotosDao().getPhotosByEstateId(estates.get(position).getId()).size() != 0) {
            Photos photos = databaseHelper.getPhotosDao().getPhotosByEstateId(estates.get(position).getId()).get(0);
            viewHolder.mainPhoto.setImageDrawable(null);
            Glide.clear(viewHolder.mainPhoto);
            Glide.with(context).load(photos.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .override(400,400) // Customize size as per requirement
                    //.placeholder(ContextCompat.getDrawable(context, R.mipmap.ic_user_placeholder_50dp))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override public boolean onException(Exception e, String model,
                                                             Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override public boolean onResourceReady(GlideDrawable resource, String model,
                                                                 Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                                 boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(viewHolder.mainPhoto);
        } else {
            // make sure Glide doesn't load anything into this view until told otherwise
            Glide.clear(viewHolder.mainPhoto);
            // remove the placeholder (optional); read comments below
        }
    }

    @Override
    public int getItemCount() {
        return estates.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.district_item)
        public TextView district;
        @BindView(R.id.square_item)
        public TextView square;
        @BindView(R.id.cost_item)
        public TextView cost;
        @BindView(R.id.flattype_item)
        public TextView flatType;
        @BindView(R.id.repairtype_item)
        public TextView repairType;
        @BindView(R.id.balcon_item)
        public TextView balcon;
        @BindView(R.id.roomnumber_item)
        public TextView roomNumber;
        @BindView(R.id.delete)
        public TextView delete;
        @BindView(R.id.main_photo)
        public ImageView mainPhoto;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            delete.setOnClickListener(view -> {
                onDeleteListener.onDelete(estates.get(getAdapterPosition()));
                estates.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface OnDeleteListener {
        void onDelete(Estate vacancy);
    }


}
