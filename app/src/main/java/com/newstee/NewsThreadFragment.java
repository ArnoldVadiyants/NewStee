package com.newstee;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newstee.model.data.News;
import com.newstee.model.data.NewsLab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnold on 20.02.2016.
 */
public class NewsThreadFragment extends Fragment {


    private LinearLayout startButton;
    private ViewPager mViewPager;
    private PlayListPager mPlayListPager;
    private ImageButton filterButton;
    private final static String TAG = "NewsThreadFragment";
   // View mediaPlayer;
    private int newSongValue = 0;
    private boolean playingValue = false;
    private Handler mHandler = new Handler();
    ImageButton mpBtnPlay;
    TextView mpTitle;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_thread, container, false);
        startButton = (LinearLayout)rootView.findViewById(R.id.thread_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "add all news to playlist",Toast.LENGTH_SHORT ).show();
                String category = null;
                if(mViewPager.getCurrentItem() == 0)
                {
                    category = Constants.CATEGORY_NEWS;
                }
                else if(mViewPager.getCurrentItem() == 1) {
                category = Constants.CATEGORY_ARTICLE;
                }
                if(category==null)
                {
                 return;
                }
                    List<News>news = NewsLab.getInstance().getNews();
                    List<News>newsList= new ArrayList<News>();
                    for(News n:news)
                    {
                        if(n.getCategory().equals(category))
                        {
                            newsList.add(n);
                        }
                    }
                    PlayList.getInstance().setNewsList(newsList);
                Intent i = new Intent(getActivity(), MediaPlayerFragmentActivity.class);
                i.putExtra(MediaPlayerFragmentActivity.ARG_AUDIO_ID, newsList.get(0).getLinksong());
                startActivity(i);

                }

        });
        filterButton= (ImageButton)rootView.findViewById(R.id.filter_imageButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "filter clicked",Toast.LENGTH_SHORT).show();
            }
        });
     //   mediaPlayer = rootView.findViewById(R.id.thread_media_player);
     //   mpBtnPlay = (ImageButton) mediaPlayer.findViewById(R.id.media_player_small_play_button);
      /*  mpBtnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (musicSrv.isPlaying()) {
                    if (musicBound) {
                        musicSrv.pausePlayer();
                        mpBtnPlay.setImageResource(R.drawable.play);
                    }
                } else {
                    // Resume song
                    if (musicBound) {
                        musicSrv.go();
                        // Changing button image to pause button
                        mpBtnPlay.setImageResource(R.drawable.ic_media_pause);
                    }
                }

            }
        });*/
    //    mpTitle = (TextView) mediaPlayer.findViewById(R.id.media_player_small_titlet_TextView);
     //   mediaPlayer.setVisibility(View.GONE);
        mPlayListPager = new PlayListPager(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)rootView.findViewById(R.id.thread_container);
        mViewPager.setAdapter(mPlayListPager);
        mViewPager.setCurrentItem(0);
        TabLayout tabLayout = (TabLayout)rootView.findViewById(R.id.thread_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "******onServiceConnected*****");
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass catalogue
            musicBound = true;


         //   updateMPNews();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void connectService() {
        if (playIntent == null) {
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    public void  updateMPNews()
    {

        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable stream
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if(musicSrv != null) {
                if(!musicSrv.isNullPlayer())
                {

                    if (!musicSrv.isPaused()) {
                        if (newSongValue != musicSrv.getNewSongValue()) {
                            mpTitle.setText(musicSrv.getSongTitle());
                            newSongValue = musicSrv.getNewSongValue();
                        }
                        if (playingValue != musicSrv.isPlaying()) {
                            if (playingValue) {
                                mpBtnPlay.setImageResource(R.drawable.ic_media_play);
                            } else {
                                mpBtnPlay.setImageResource(R.drawable.ic_media_pause);

                            }
                            playingValue = !playingValue;
                        }

                    }
                }
            }
            // Running this stream after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    //start and bind the service when the activity starts
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "******onStart*****");
        connectService();
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "******onResume*****");
/*
        if(musicBound)
        {
            if(musicSrv!=null)
            {

                if(musicSrv.isPlaying())
                {
                    mpBtnPlay.setImageResource(R.drawable.ic_media_pause);
                    mediaPlayer.setVisibility(View.VISIBLE);

                }
                else
                {
                    mediaPlayer.setVisibility(View.GONE);
                    mpBtnPlay.setImageResource(R.drawable.ic_media_play);
                }
                mpTitle.setText(musicSrv.getSongTitle());
            }
        }*/

    }

    public class PlayListPager extends FragmentPagerAdapter {
        public int current_position = 0;
        public PlayListPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return NewsThreadListFragment.newInstance(Constants.ARGUMENT_NONE,Constants.CATEGORY_NEWS);
                case 1:
                    return NewsThreadListFragment.newInstance(Constants.ARGUMENT_NONE,Constants.CATEGORY_ARTICLE);
                case 2:
                    return  NewsThreadListFragment.newInstance(Constants.ARGUMENT_NONE,Constants.CATEGORY_STORY);
            }

            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*Drawable image = ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_image_thread);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;*/
           /* switch (position) {
                case 0:
                    return (getResources().getString(R.string.lenta)).toUpperCase();
                case 1:
                    return (getResources().getString(R.string.catalog)).toUpperCase();
                case 2:
                    return (getResources().getString(R.string.play_list)).toUpperCase();
            }
            return null;*/

            switch (position) {
                case 0:
                    return getActivity().getResources().getString(R.string.news);
                case 1:
                    return getActivity().getResources().getString(R.string.articles);
                case 2:
                    return getActivity().getResources().getString(R.string.story);

            }

            return null;
        }
    }
}

