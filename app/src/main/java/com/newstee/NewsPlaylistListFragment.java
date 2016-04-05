package com.newstee;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Arnold on 24.02.2016.
 */
public class NewsPlaylistListFragment extends NewsListFragment {
    @Override
    public void setStatusImageButton(ImageButton statusImageButton, int newsStatus) {
       if(newsStatus == Constants.STATUS_IS_PLAYING) {
           statusImageButton.setVisibility(View.VISIBLE);
           statusImageButton.setImageResource(R.drawable.news_is_playing_button);
       }
        else
       {
           statusImageButton.setVisibility(View.GONE);
       }

    }

    @Override
    String getEmpty() {
        return getString(R.string.empty_my_play_list);
    }
    @Override
    int getTextColor() {
        return getResources().getColor(android.R.color.background_dark);
    }

    @Override
    void setLikeView(TextView likeTextView, ImageView likeImageView, boolean isLiked) {
        likeTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
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
