package com.newstee;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Arnold on 24.02.2016.
 */
public class NewsThreadListFragment extends NewsListFragment {
    @Override
    public void setStatusImageButton(final ImageButton statusImageButton, final int newsStatus) {
        switch (newsStatus)
        {
            case News.STATUS_NOT_ADDED:
                statusImageButton.setImageResource(R.drawable.news_to_add_button);
                break;
            case News.STATUS_WAS_ADDED:
                statusImageButton.setImageResource(R.drawable.news_to_play_button);
                break;
            case News.STATUS_IS_PLAYING:
                statusImageButton.setImageResource(R.drawable.news_is_playing_button);
                break;
        }
        statusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (newsStatus)
                {
                    case News.STATUS_NOT_ADDED:
                        Toast.makeText(getActivity(),"This news is added", Toast.LENGTH_SHORT).show();
                        statusImageButton.setImageResource(R.drawable.news_to_play_button);
                        break;
                    case News.STATUS_WAS_ADDED:
                        statusImageButton.setImageResource(R.drawable.news_is_playing_button);
                        Toast.makeText(getActivity(),"This news started playing", Toast.LENGTH_SHORT).show();
                        break;
                    case News.STATUS_IS_PLAYING:
                        Toast.makeText(getActivity(),"Media player is opening", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
