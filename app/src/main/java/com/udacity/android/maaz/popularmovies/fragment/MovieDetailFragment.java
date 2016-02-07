package com.udacity.android.maaz.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.udacity.android.maaz.popularmovies.BuildConfig;
import com.udacity.android.maaz.popularmovies.R;
import com.udacity.android.maaz.popularmovies.adapter.ReviewAdapter;
import com.udacity.android.maaz.popularmovies.adapter.TrailerAdapter;
import com.udacity.android.maaz.popularmovies.data.MovieDbHelper;
import com.udacity.android.maaz.popularmovies.model.MovieData;
import com.udacity.android.maaz.popularmovies.model.Review;
import com.udacity.android.maaz.popularmovies.model.ReviewResults;
import com.udacity.android.maaz.popularmovies.model.Trailer;
import com.udacity.android.maaz.popularmovies.model.TrailerResults;
import com.udacity.android.maaz.popularmovies.utilities.PopularMovieConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private MovieData mMovieData;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private MovieDbHelper mMovieDbHelper;
    private boolean mFavChecked;
    private boolean mIsFavourite;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance(MovieData movieData) {
        MovieDetailFragment detailFragment = new MovieDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(PopularMovieConstants.MOVIE_DATA, movieData);
        detailFragment.setArguments(args);

        return detailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieDbHelper = new MovieDbHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Bundle args = getArguments();
        if (args != null) {
            TextView movieTitle = (TextView) rootView.findViewById(R.id.text_movie_title);
            ImageView posterImage = (ImageView) rootView.findViewById(R.id.image_poster);
            TextView releaseDate = (TextView) rootView.findViewById(R.id.text_release_date);
            TextView rating = (TextView) rootView.findViewById(R.id.text_user_rating);
            TextView plotSynopsis = (TextView) rootView.findViewById(R.id.text_synopsis);

            MovieData movieData = args.getParcelable(PopularMovieConstants.MOVIE_DATA);

            // Storing movie in order to make the network calls
            mMovieData = movieData;

            // Setting details
            movieTitle.setText(movieData.getOriginalTitle());
            Picasso.with(getActivity()).load(PopularMovieConstants.BASE_POSTER_URI_W342 + movieData.getPosterPath()).into(posterImage);
            releaseDate.setText(movieData.getReleaseDate().split("-")[0]);
            rating.setText(movieData.getVoteAverage().toString() + "/10");
            plotSynopsis.setText(movieData.getOverview());

            // Trailers
            mTrailerAdapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
            ListView trailersView = (ListView) rootView.findViewById(R.id.list_trailer);
            trailersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trailer trailer = mTrailerAdapter.getItem(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                    startActivity(intent);
                }
            });
            trailersView.setAdapter(mTrailerAdapter);

            // Reviews
            mReviewAdapter = new ReviewAdapter(getActivity(), new ArrayList<Review>());
            ListView reviewsView = (ListView) rootView.findViewById(R.id.list_reviews);
            reviewsView.setAdapter(mReviewAdapter);

            // Favourites
            CheckBox favCheckBox = (CheckBox) rootView.findViewById(R.id.check_box_fav);
            mIsFavourite = mMovieDbHelper.isFavourite(movieData.getId());
            favCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mFavChecked = isChecked;
                }
            });
            favCheckBox.setChecked(mIsFavourite);

        }
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMovieData != null) {
            new FetchTrailersTask().execute(String.valueOf(mMovieData.getId()));
            new FetchReviewsTask().execute(String.valueOf(mMovieData.getId()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Making sure the movie is saved as favourite only when required
        if (mMovieDbHelper != null && mMovieData != null) {
            if (mFavChecked) {
                // Make sure this was not a favourite already
                if (!mIsFavourite) {
                    new AsyncTask<Void, Void, Void>() {
                        protected Void doInBackground(Void... unused) {
                            mMovieDbHelper.insertMovie(mMovieData);
                            return null;
                        }
                    }.execute();
                }
            } else {
                // Make sure the movie was a favourite
                if (mIsFavourite) {
                    new AsyncTask<Void, Void, Void>() {
                        protected Void doInBackground(Void... unused) {
                            mMovieDbHelper.deleteMovie(mMovieData.getId());
                            return null;
                        }
                    }.execute();
                }
            }
        }
    }

    // AsyncTask to fetch all the trailers for the particular movie
    private class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
        private final String BASE_URL = "http://api.themoviedb.org/3/movie";
        private final String API_KEY = "api_key";
        private final String VIDEOS = "videos";

        @Override
        protected List<Trailer> doInBackground(String... params) {
            Uri uri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendPath(params[0])
                    .appendPath(VIDEOS)
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(uri.toString()).build();
            Response response = null;
            TrailerResults trailerResults = null;
            try {
                response = httpClient.newCall(request).execute();
                Gson gson = new Gson();
                trailerResults = gson.fromJson(response.body().string(), TrailerResults.class);
            } catch (IOException e) {
                Log.d(LOG_TAG, "Error making network call. " + e.getMessage());
            } catch (JsonSyntaxException e) {
                Log.d(LOG_TAG, "Error while parsing the JSON. " + e.getMessage());
            }

            if (trailerResults == null) {
                return null;
            }
            return trailerResults.getResults();
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null) {
                mTrailerAdapter.clear();
                mTrailerAdapter.addAll(trailers);
            }
        }
    }

    // AsyncTask to fetch all the reviews for the particular movie
    private class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
        private final String BASE_URL = "http://api.themoviedb.org/3/movie";
        private final String API_KEY = "api_key";
        private final String REVIEWS = "reviews";

        @Override
        protected List<Review> doInBackground(String... params) {
            Uri uri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendPath(params[0])
                    .appendPath(REVIEWS)
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(uri.toString()).build();
            Response response = null;
            ReviewResults reviewResults = null;
            try {
                response = httpClient.newCall(request).execute();
                Gson gson = new Gson();
                reviewResults = gson.fromJson(response.body().string(), ReviewResults.class);
            } catch (IOException e) {
                Log.d(LOG_TAG, "Error making network call. " + e.getMessage());
            } catch (JsonSyntaxException e) {
                Log.d(LOG_TAG, "Error while parsing the JSON. " + e.getMessage());
            }

            if (reviewResults == null) {
                return null;
            }
            return reviewResults.getResults();
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if (reviews != null) {
                mReviewAdapter.clear();
                mReviewAdapter.addAll(reviews);
            }
        }
    }
}
