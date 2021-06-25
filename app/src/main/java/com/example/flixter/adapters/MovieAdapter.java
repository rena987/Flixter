package com.example.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixter.DetailsActivity;
import com.example.flixter.R;
import com.example.flixter.databinding.ItemMovieBinding;
import com.example.flixter.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // inflate a view using where adapter constructed from
    Context context;
    // Actual data
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // inflating a layout from XML and returning the ViewHolder
    // comes up fewer times in log than onBind since different binds are being created to same ViewHolders
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "OnCreateViewHolder");
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    // involves populating data into the view from viewholder (after previous function)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder: " + position);
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    // returns the total count of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemMovieBinding binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemMovieBinding.bind(itemView);
        }

        public void bind(Movie movie) {
            binding.tvOverview.setText(movie.getOverview());
            binding.tvTitle.setText(movie.getTitle());

            String imageUrl;
            int orientation = context.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            } else {
                imageUrl = movie.getPosterPath();
            }

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.flicks_movie_placeholder)
                    .transform(new RoundedCornersTransformation(30, 0))
                    .into(binding.ivPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MovieAdapter", "Movie Clicked: " + movie.getTitle());
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(intent);
                }
            });

        }
    }
}
