package com.tdtu.starrail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tdtu.starrail.R;
import com.tdtu.starrail.classes.Movie;

import java.util.ArrayList;
import java.util.List;

public class AdapterRcvSearch extends RecyclerView.Adapter<AdapterRcvSearch.ViewHolder> implements Filterable {
    private Context context;
    private List<Movie> movieList;
    private OnItemClickListener listener;

    public AdapterRcvSearch(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_rcvsearch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.tvName.setText(movie.getName());
        holder.tvDesc.setText(movie.getDescription());
        Glide.with(context)
                .load("file:///android_asset/" + movie.getImage1())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(movie);
                }
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase();
                List<Movie> filteredList = new ArrayList<>();
                if (searchText.isEmpty()) {
                    filteredList.addAll(movieList);
                } else {
                    for (Movie movie : movieList) {
                        if (movie.getName().toLowerCase().contains(searchText)) {
                            filteredList.add(movie);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                movieList.clear();
                movieList.addAll((List<Movie>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public void setData(List<Movie> filteredList) {
        this.movieList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName;
        TextView tvDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_itemmoviesearch);
            tvName = itemView.findViewById(R.id.tv_namemoviesearch);
            tvDesc = itemView.findViewById(R.id.tv_movie_desc);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
