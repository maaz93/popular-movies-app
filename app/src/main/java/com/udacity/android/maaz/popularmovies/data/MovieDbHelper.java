package com.udacity.android.maaz.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udacity.android.maaz.popularmovies.data.MovieContract.MovieEntry;
import com.udacity.android.maaz.popularmovies.model.MovieData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    static final String DB_NAME = "movie.db";

    private static final String[] SELECT_ALL_PROJECTION = {
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry._ID
    };

    private static final int COL_MOVIE_ID = 0;
    private static final int COL_ORIGINAL_TITLE = 1;
    private static final int COL_POSTER_PATH = 2;
    private static final int COL_RELEASE_DATE = 3;
    private static final int COL_VOTE_AVERAGE = 4;
    private static final int COL_OVERVIEW = 5;
    private static final int COL_ID = 6;

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +

                // Ignoring any duplicate entries that are attempted
                " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID +
                ") ON CONFLICT IGNORE);";

        // Creating the database
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }

    public long insertMovie(MovieData movieData) {
        final SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(MovieEntry.TABLE_NAME, null, convertMovieDataToContentValues(movieData));
        return id;
    }

    public long deleteMovie(long movieId) {
        final SQLiteDatabase db = this.getWritableDatabase();
        long id = db.delete(MovieEntry.TABLE_NAME,
                MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movieId)});
        return id;
    }

    public List<MovieData> fetchAllFavouriteMovies() {
        final SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MovieEntry.TABLE_NAME, SELECT_ALL_PROJECTION, null, null, null, null, null);
        List<MovieData> movieDataList = new ArrayList<MovieData>();
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                movieDataList.add(convertCursorToMovieData(cursor));
            }
        }
        return movieDataList;
    }

    public Set<Long> fetchFavouriteMovieIds() {
        final SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MovieEntry.TABLE_NAME, new String[]{MovieEntry.COLUMN_MOVIE_ID}, null, null, null, null, null);
        Set<Long> idSet = new HashSet<Long>() {
        };
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                idSet.add(cursor.getLong(0));
            }
        }
        return idSet;
    }

    // Check if an item is a favourtite
    public boolean isFavourite(long movieId) {
        final SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MovieEntry.TABLE_NAME,
                new String[]{MovieEntry.COLUMN_MOVIE_ID},
                MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movieId)},
                null,
                null,
                null);

        return cursor.moveToFirst();
    }

    private ContentValues convertMovieDataToContentValues(MovieData movieData) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_ID, movieData.getId());
        values.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movieData.getOriginalTitle());
        values.put(MovieEntry.COLUMN_OVERVIEW, movieData.getOverview());
        values.put(MovieEntry.COLUMN_POSTER_PATH, movieData.getPosterPath());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, movieData.getVoteAverage());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, movieData.getReleaseDate());
        return values;
    }

    private MovieData convertCursorToMovieData(Cursor cursor) {
        MovieData movieData = new MovieData();
        movieData.setId(cursor.getLong(COL_MOVIE_ID));
        movieData.setOriginalTitle(cursor.getString(COL_ORIGINAL_TITLE));
        movieData.setPosterPath(cursor.getString(COL_POSTER_PATH));
        movieData.setReleaseDate(cursor.getString(COL_RELEASE_DATE));
        movieData.setVoteAverage(cursor.getDouble(COL_VOTE_AVERAGE));
        movieData.setOverview(cursor.getString(COL_OVERVIEW));
        return movieData;
    }
}
