package com.udacity.android.maaz.popularmovies.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.udacity.android.maaz.popularmovies.BuildConfig;
import com.udacity.android.maaz.popularmovies.R;
import com.udacity.android.maaz.popularmovies.adapter.DiscoverMovieAdapter;
import com.udacity.android.maaz.popularmovies.data.MovieDbHelper;
import com.udacity.android.maaz.popularmovies.model.DiscoverMovieResults;
import com.udacity.android.maaz.popularmovies.model.MovieData;
import com.udacity.android.maaz.popularmovies.utilities.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    private DiscoverMovieAdapter mMovieAdapter;
    private OnMovieSelectedCallback mMovieSelectedCallback;
    private MovieDbHelper mMovieDbHelper;

    // The callback interface that all activities with this fragment msu implement
    public interface OnMovieSelectedCallback {
        public void onMovieSelected(MovieData movieData);
    }

    public MoviesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof OnMovieSelectedCallback) {
            mMovieSelectedCallback = (OnMovieSelectedCallback) getActivity();
        } else {
            throw new ClassCastException(getActivity().toString() + " must implement MoviesFragment.OnMovieSelectedCallback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieAdapter = new DiscoverMovieAdapter(getActivity(), new ArrayList<MovieData>());
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView movieGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieData movieData = mMovieAdapter.getItem(position);
                mMovieSelectedCallback.onMovieSelected(movieData);
                /*Intent intent = new Intent(getActivity().getApplicationContext(), MovieDetailActivity.class);
                intent.putExtra(PopularMovieConstants.MOVIE_DATA, movieData);
                startActivity(intent);*/
            }
        });
        movieGridView.setAdapter(mMovieAdapter);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize a DB helper instance for use in the fragment
        mMovieDbHelper = new MovieDbHelper(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchMoviesTask().execute(Utility.getPreferredSortOrder(getActivity()));
    }

    public void onSortOrderChanged() {
        new FetchMoviesTask().execute(Utility.getPreferredSortOrder(getActivity()));
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<MovieData>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
        private final String SORT_BY = "sort_by";
        private final String API_KEY = "api_key";

        @Override
        protected List<MovieData> doInBackground(String... params) {
            List<MovieData> movieDataList = null;
            if (params[0].contentEquals("favourites")) {
                movieDataList = mMovieDbHelper.fetchAllFavouriteMovies();
            } else {
                Uri uri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(SORT_BY, params[0])
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                OkHttpClient httpClient = new OkHttpClient();
                Request request = new Request.Builder().url(uri.toString()).build();
                Response response = null;
                DiscoverMovieResults movieResults = null;
                try {
                    response = httpClient.newCall(request).execute();
                    Gson gson = new Gson();
                    movieResults = gson.fromJson(response.body().string(), DiscoverMovieResults.class);
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Error making network call. " + e.getMessage());
                } catch (JsonSyntaxException e) {
                    Log.d(LOG_TAG, "Error while parsing the JSON. " + e.getMessage());
                }

                if (movieResults != null) {
                    movieDataList = movieResults.getResults();
                }
            }
            return movieDataList;
        }

        @Override
        protected void onPostExecute(List<MovieData> movies) {
            if (movies != null) {
                mMovieAdapter.clear();
                mMovieAdapter.addAll(movies);
            } else {
                Toast.makeText(getActivity(), "There was a problem syncing with the servers. Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        }
    }
}