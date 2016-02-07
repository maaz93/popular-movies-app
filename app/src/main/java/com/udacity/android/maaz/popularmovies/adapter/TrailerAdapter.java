package com.udacity.android.maaz.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.android.maaz.popularmovies.R;
import com.udacity.android.maaz.popularmovies.model.Trailer;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super(context, 0, trailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer trailer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.activity_list_item, parent, false);
        }

        ImageView playImage = (ImageView) convertView.findViewById(android.R.id.icon);
        playImage.setImageResource(R.drawable.ic_play_arrow_black_24dp);

        TextView trailerNameTextView = (TextView) convertView.findViewById(android.R.id.text1);
        trailerNameTextView.setText(trailer.getName());

        return convertView;
    }
}
