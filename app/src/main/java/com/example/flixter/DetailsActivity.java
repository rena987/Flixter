package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.databinding.ActivityDetailsBinding;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class DetailsActivity extends AppCompatActivity {

    ImageView dtPoster;
    TextView dtTitle;
    RatingBar dtRatingBar;
    TextView dtSynopsis;
    TextView dtVoteCount;
    String videoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dtPoster = findViewById(R.id.dtPoster);
        dtTitle = findViewById(R.id.dtTitle);
        dtRatingBar = findViewById(R.id.dtRatingBar);
        dtSynopsis = findViewById(R.id.dtSynopsis);
        dtVoteCount = findViewById(R.id.dtNumOfVotes);

        Intent intent = getIntent();
        Movie movie = (Movie) Parcels.unwrap(intent.getParcelableExtra("movie"));

        dtTitle.setText(movie.getTitle());

        float rating = (float) movie.getRating()/2;
        Log.d("DetailsActivity", "Rating: " + rating);
        dtRatingBar.setRating(rating);
        dtSynopsis.setText(movie.getOverview());
        dtVoteCount.setText("(" + String.valueOf(movie.getVoteCount()) + ")");

        Glide.with(this)
                .load(movie.getPosterPath())
                .placeholder(R.drawable.flicks_movie_placeholder)
                .transform(new RoundedCornersTransformation(30, 0))
                .into(dtPoster);

        BitmapDrawable drawable = (BitmapDrawable) dtPoster.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Palette palette = Palette.from(bitmap).generate();
        Palette.Swatch vibrant = palette.getVibrantSwatch();

        if (vibrant != null) {
            view.setBackgroundColor(vibrant.getRgb());
        }

        Log.d("DetailsActivity", "ID: " + movie.getId());

        AsyncHttpClient client = new AsyncHttpClient();
        String MOVIE_URL = "https://api.themoviedb.org/3/movie/"
                + movie.getId()
                + "/videos?api_key="
                + getString(R.string.VID_API_KEY);

        client.get(MOVIE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d("DetailsActivity", "onSuccess Movie Video: " + json);
                JSONObject jsonObject = json.jsonObject;
                JSONArray results;
                try {
                    results = jsonObject.getJSONArray("results");
                    JSONObject videoObj = results.getJSONObject(0);
                    videoID = videoObj.getString("key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("DetailsActivity", "onFailure Movie Video");
            }
        });

        dtPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, MovieTrailerActivity.class);
                intent.putExtra("VideoID", videoID);
                startActivity(intent);
            }
        });
    }
}