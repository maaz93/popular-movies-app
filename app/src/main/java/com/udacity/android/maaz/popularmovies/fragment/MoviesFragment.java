package com.udacity.android.maaz.popularmovies.fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.udacity.android.maaz.popularmovies.model.DiscoverMovieResults;
import com.udacity.android.maaz.popularmovies.model.MovieData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    private DiscoverMovieAdapter mMovieAdapter;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieAdapter = new DiscoverMovieAdapter(getActivity(), new ArrayList<MovieData>());
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView movieGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        movieGridView.setAdapter(mMovieAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchMoviesTask().execute("popularity.desc");
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<MovieData>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
        private final String SORT_BY = "sort_by";
        private final String API_KEY = "api_key";

        @Override
        protected List<MovieData> doInBackground(String... params) {
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

            if (movieResults == null) {
                return null;
            }
            return movieResults.getResults();
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