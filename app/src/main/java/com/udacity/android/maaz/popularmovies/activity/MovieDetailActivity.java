package com.udacity.android.maaz.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.udacity.android.maaz.popularmovies.R;
import com.udacity.android.maaz.popularmovies.fragment.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            MovieDetailFragment detailFragment = new MovieDetailFragment();
            detailFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_movie_detail, detailFragment)
                    .commit();
        }
    }

}
