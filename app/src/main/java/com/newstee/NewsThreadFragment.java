package com.newstee;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Arnold on 20.02.2016.
 */
public class NewsThreadFragment extends Fragment {
    private LinearLayout startButton;
    private ImageButton filterButton;
    private final static String TAG = "NewsThreadFragment";
    View mediaPlayer;
    private boolean newSongValue = false;
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
            }
        });
        filterButton= (ImageButton)rootView.findViewById(R.id.filter_imageButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "filter clicked",Toast.LENGTH_SHORT).show();
            }
        });
        mediaPlayer = rootView.findViewById(R.id.thread_media_player);
        mpBtnPlay = (ImageButton) mediaPlayer.findViewById(R.id.media_player_small_play_button);
        mpBtnPlay.setOnClickListener(new View.OnClickListener() {

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
                        mpBtnPlay.setImageResource(R.drawable.pause);
                    }
                }

            }
        });
        mpTitle = (TextView) mediaPlayer.findViewById(R.id.media_player_small_titlet_TextView);
        mediaPlayer.setVisibility(View.GONE);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment  = new NewsThreadListFragment();
            fm.beginTransaction().add(R.id.thread_content, fragment)
                    .commit();

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


            updateMPNews();
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
                            newSongValue = !newSongValue;
                        }
                        if (playingValue != musicSrv.isPlaying()) {
                            if (playingValue) {
                                mpBtnPlay.setImageResource(R.drawable.play);
                            } else {
                                mpBtnPlay.setImageResource(R.drawable.pause);

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

        if(musicBound)
        {
            if(musicSrv!=null)
            {

                if(musicSrv.isPlaying())
                {
                    mpBtnPlay.setImageResource(R.drawable.pause);
                    mediaPlayer.setVisibility(View.VISIBLE);

                }
                else
                {
                    mediaPlayer.setVisibility(View.GONE);
                    mpBtnPlay.setImageResource(R.drawable.play);
                }
                mpTitle.setText(musicSrv.getSongTitle());
            }
        }
    }

}

