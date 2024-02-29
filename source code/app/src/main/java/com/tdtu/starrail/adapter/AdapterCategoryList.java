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

public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.ViewHolder> {
    private ArrayList<Movie> mmovieCategory;
    private Context mContext;
    private OnItemClickListener mListener;

    public AdapterCategoryList(Context context, ArrayList<Movie> movieList) {
        mContext = context;
        mmovieCategory = movieList;
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
        View view = layoutInflater.inflate(R.layout.layout_item_categorylist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mmovieCategory.get(position);
        holder.nameMovieCategory.setText(movie.getName());
        holder.priceMovieCategory.setText(movie.getDescription());

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
        return mmovieCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView nameMovieCategory;
        public TextView priceMovieCategory;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewcl);
            priceMovieCategory = itemView.findViewById(R.id.tv_pricecl);
            nameMovieCategory = itemView.findViewById(R.id.tv_namecl);

        }
    }
}
