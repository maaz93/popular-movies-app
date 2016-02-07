package com.udacity.android.maaz.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.android.maaz.popularmovies.R;
import com.udacity.android.maaz.popularmovies.fragment.MovieDetailFragment;
import com.udacity.android.maaz.popularmovies.fragment.MoviesFragment;
import com.udacity.android.maaz.popularmovies.model.MovieData;
import com.udacity.android.maaz.popularmovies.utilities.PopularMovieConstants;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedCallback {

    private static final String DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This means that the detail fragment is visible due to tab layout
        if (findViewById(R.id.fragment_movie_detail) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_movie_detail, new MovieDetailFragment(), DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(MovieData movieData) {
        if (mTwoPane) {
            // Find the currently visible fragment
            MovieDetailFragment visibleFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);

            // Create the new fragment that will replace the existing one
            MovieDetailFragment newFragment = MovieDetailFragment.newInstance(movieData);

            getSupportFragmentManager().beginTransaction()
                    .replace(visibleFragment.getId(), newFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .putExtra(PopularMovieConstants.MOVIE_DATA, movieData);
            startActivity(intent);
        }
    }
}
