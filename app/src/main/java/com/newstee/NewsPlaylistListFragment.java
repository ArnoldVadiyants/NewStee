package com.newstee;

import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Arnold on 24.02.2016.
 */
public class NewsPlaylistListFragment extends NewsListFragment {
    @Override
    public void setStatusImageButton(ImageButton statusImageButton, int newsStatus) {
       if(newsStatus == News.STATUS_IS_PLAYING) {
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

}
