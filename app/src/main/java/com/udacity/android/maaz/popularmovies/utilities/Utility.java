package com.udacity.android.maaz.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.udacity.android.maaz.popularmovies.R;

public class Utility {
    public static String getPreferredSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sorting_key),
                context.getString(R.string.pref_sorting_default));
    }
}
