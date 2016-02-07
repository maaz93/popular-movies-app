package com.udacity.android.maaz.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.android.maaz.popularmovies.model.Review;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView authorTextView = (TextView) convertView.findViewById(android.R.id.text1);
        authorTextView.setText(review.getAuthor());

        TextView contentTextView = (TextView) convertView.findViewById(android.R.id.text2);
        contentTextView.setText(review.getContent());

        return convertView;
    }
}
