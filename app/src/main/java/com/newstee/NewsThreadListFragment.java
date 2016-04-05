package com.newstee;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Arnold on 24.02.2016.
 */
public class NewsThreadListFragment extends NewsListFragment {
    @Override
    public void setStatusImageButton(final ImageButton statusImageButton, final int newsStatus) {
        switch (newsStatus) {
            case Constants.STATUS_NOT_ADDED:
                statusImageButton.setImageResource(R.drawable.news_to_add_button);
                break;
            case Constants.STATUS_WAS_ADDED:
                statusImageButton.setImageResource(R.drawable.news_to_play_button);
                break;
            case Constants.STATUS_IS_PLAYING:
                statusImageButton.setImageResource(R.drawable.news_is_playing_button);
                break;
        }
        statusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (newsStatus) {
                    case Constants.STATUS_NOT_ADDED:
                        Toast.makeText(getActivity(), "Constants #" + v.getTag() + " is added", Toast.LENGTH_SHORT).show();
                        statusImageButton.setImageResource(R.drawable.news_to_play_button);
                        break;
                    case Constants.STATUS_WAS_ADDED:
                        statusImageButton.setImageResource(R.drawable.news_is_playing_button);
                        Toast.makeText(getActivity(), "Constants #" + v.getTag() + " started playing", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.STATUS_IS_PLAYING:
                        Toast.makeText(getActivity(), "Constants #" + v.getTag() + " is opening", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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

        if(isLiked)
        {
            likeTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            likeImageView.setImageResource(R.drawable.ic_favourited);
        }
        else
        {
            likeTextView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            likeImageView.setImageResource(R.drawable.ic_favourite);
        }
    }
}
