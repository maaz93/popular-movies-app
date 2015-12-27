package com.udacity.android.maaz.popularmovies.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.android.maaz.popularmovies.R;
import com.udacity.android.maaz.popularmovies.model.MovieData;
import com.udacity.android.maaz.popularmovies.utilities.PopularMovieConstants;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        TextView movieTitle = (TextView) rootView.findViewById(R.id.text_movie_title);
        ImageView posterImage = (ImageView) rootView.findViewById(R.id.image_poster);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.text_release_date);
        TextView rating = (TextView) rootView.findViewById(R.id.text_user_rating);
        TextView plotSynopsis = (TextView) rootView.findViewById(R.id.text_synopsis);

        Intent intent = getActivity().getIntent();
        if(intent != null) {
            MovieData movieData = intent.getParcelableExtra(PopularMovieConstants.MOVIE_DATA);
            movieTitle.setText(movieData.getOriginalTitle());
            Picasso.with(getActivity()).load(PopularMovieConstants.BASE_POSTER_URI_W342 + movieData.getPosterPath()).into(posterImage);
            releaseDate.setText(movieData.getReleaseDate().split("-")[0]);
            rating.setText(movieData.getVoteAverage().toString() + "/10");
            plotSynopsis.setText(movieData.getOverview());
        }
        return rootView;

    }
}
