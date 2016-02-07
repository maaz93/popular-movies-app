package com.udacity.android.maaz.popularmovies.data;

import android.provider.BaseColumns;

public class MovieContract {

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
    }
}
