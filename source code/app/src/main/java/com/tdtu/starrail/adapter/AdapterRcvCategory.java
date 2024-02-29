package com.tdtu.starrail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tdtu.starrail.R;
import com.tdtu.starrail.classes.Movie;


import java.util.ArrayList;

public class AdapterRcvCategory extends RecyclerView.Adapter<AdapterRcvCategory.ViewHolder> {
    private ArrayList<Movie> moviecategory;
    private Context mContext;
    private OnItemClickListener mListener;
    public AdapterRcvCategory(Context context, ArrayList<Movie> bookList) {
        mContext = context;
        moviecategory = bookList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_item_listmovie, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = moviecategory.get(position);
        holder.namebook.setText(movie.getName());
        holder.pricebook.setText(String.format("%,d", movie.getPrice()) + " VNĐ");

        // Load image from assets folder using Glide library
        Glide.with(mContext)
                .load("file:///android_asset/" + movie.getImage1())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviecategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView namebook;
        public TextView pricebook;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_movie);
            namebook = itemView.findViewById(R.id.tv_namemovie);
            pricebook = itemView.findViewById(R.id.tv_pricemovie);

        }

    }
}
