package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixter.models.Movie;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    ImageView dtPoster;
    TextView dtTitle;
    RatingBar dtRatingBar;
    TextView dtSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dtPoster = findViewById(R.id.dtPoster);
        dtTitle = findViewById(R.id.dtTitle);
        dtRatingBar = findViewById(R.id.dtRatingBar);
        dtSynopsis = findViewById(R.id.dtSynopsis);

        Intent intent = getIntent();
        Movie movie = (Movie) Parcels.unwrap(intent.getParcelableExtra("movie"));

        dtTitle.setText(movie.getTitle());

        float rating = (float) movie.getRating()/2;
        Log.d("DetailsActivity", "Rating: " + rating);
        dtRatingBar.setRating(rating);
        dtSynopsis.setText(movie.getOverview());

        Glide.with(this)
                .load(movie.getPosterPath())
                .placeholder(R.drawable.flicks_movie_placeholder)
                .into(dtPoster);

    }
}