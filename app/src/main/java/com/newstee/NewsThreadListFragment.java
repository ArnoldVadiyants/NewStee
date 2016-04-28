package com.newstee;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Arnold on 24.02.2016.
 */
public class NewsThreadListFragment extends NewsListFragment {

    public static NewsListFragment newInstance(String argument,String category) {
        NewsListFragment fragment = new NewsThreadListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        args.putString(ARG_PARAMETER,argument);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsThreadListFragment() {
    }

    @Override
    public void setStatusImageButton(final ImageButton statusImageButton, final int newsStatus) {
        switch (newsStatus) {
            case Constants.STATUS_NOT_ADDED:
                statusImageButton.setImageResource(R.drawable.news_to_add_button);
                break;
            case Constants.STATUS_WAS_ADDED:
                statusImageButton.setImageResource(R.drawable.ic_is_added);
                break;
            case Constants.STATUS_IS_PLAYING:
                statusImageButton.setImageResource(R.drawable.news_is_playing_button);
                break;
        }

    }

    @Override
    String getEmpty() {
        return getString(R.string.check_internet_con);
    }

    @Override
    int getTextColor() {
        return getResources().getColor(android.R.color.background_dark);
    }

    @Override
    void setLikeView(TextView likeTextView, ImageView likeImageView, boolean isLiked) {

        if (isLiked) {
            likeTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            likeImageView.setImageResource(R.drawable.ic_favourited);
        } else {
            likeTextView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            likeImageView.setImageResource(R.drawable.ic_favourite);
        }
    }


}
