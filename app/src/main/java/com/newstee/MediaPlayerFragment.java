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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.newstee.MusicService.MusicBinder;
import com.newstee.model.data.News;
import com.newstee.model.data.NewsLab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnold on 17.02.2016.
 */
public class MediaPlayerFragment extends Fragment implements  SeekBar.OnSeekBarChangeListener {
    private final static String TAG = "MediaPlayerFragment";
    private boolean newSongValue = false;
    private boolean playingValue = false;
    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private SeekBar songProgressBar;
    private SeekBar volumeSeekBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private Utilities utils;
    private Handler mHandler = new Handler();
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    //song catalogue variables
    private List<News> mNewsList;
    //service
    private MusicService musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound=false;
    //controller


    //activity and playback pause flags
    private boolean paused=false, playbackPaused=false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsList = new ArrayList<News>();
        getSongList();

        Log.d(TAG, "******onCreate*****");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "******onResume*****");
        if (musicBound) {
            updateMPNews();
        }
    }

    public void updateMPNews() {
        if (musicSrv != null) {
            if (musicSrv.isPlaying()) {
                btnPlay.setImageResource(android.R.drawable.ic_media_pause);
            } else {
                btnPlay.setImageResource(android.R.drawable.ic_media_play);
            }
            songTitleLabel.setText(musicSrv.getSongTitle());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "******onCreateView*****");
        //retrieve catalogue view
        View root = inflater.inflate(R.layout.media_conroller,container, false );
        btnPlay = (ImageButton) root.findViewById(R.id.pause);
        btnForward = (ImageButton) root.findViewById(R.id.ffwd);
        btnBackward = (ImageButton) root.findViewById(R.id.rew);
        btnNext = (ImageButton) root.findViewById(R.id.next);
        Log.d("asff", btnForward.getWidth()+"  " + btnBackward.getHeight());
        btnPrevious = (ImageButton) root.findViewById(R.id.prev);
        btnPlaylist = (ImageButton) root.findViewById(R.id.play_list_imageButton);
        volumeSeekBar = (SeekBar)root.findViewById(R.id.volume_seekBar);
        songProgressBar = (SeekBar) root.findViewById(R.id.during_seekBar);
        songTitleLabel = (TextView) root.findViewById(R.id.title_TextView);
        songCurrentDurationLabel = (TextView) root.findViewById(R.id.curent_time_TextView);
        songTotalDurationLabel = (TextView) root.findViewById(R.id.total_time_TextView);
        utils = new Utilities();

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(musicSrv == null) {
                    return;
                }
                    musicSrv.setVolume(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        songProgressBar.setOnSeekBarChangeListener(this);
        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = musicSrv.getPosn();
                int totalPos = musicSrv.getDur();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= totalPos) {
                    // forward song
                    musicSrv.seek(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    musicSrv.seek(totalPos);
                }
            }
        });

        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = musicSrv.getPosn();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    musicSrv.seek(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    musicSrv.seek(0);
                }

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                musicSrv.playNext();

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                musicSrv.playPrev();

            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if(musicSrv.isPlaying()){
                    if(musicBound){
                        musicSrv.pausePlayer();
                        btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    }
                }else{
                    // Resume song
                    if(musicBound){
                        musicSrv.go();
                        // Changing button image to pause button
                        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    }
                }

            }
        });
        //instantiate catalogue

        //get songs from device

        //sort alphabetically by title

        //create and set adapter
     /*   SongAdapter songAdt = new SongAdapter(getActivity(), songList);
       songView.setAdapter(songAdt);
        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                songPicked(view);
                                            }
                                        });

                //setup controller
                setController(root);*/

        return root;
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "******onServiceConnected*****");
            MusicBinder binder = (MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass catalogue
            musicBound = true;
            volumeSeekBar.setProgress(musicSrv.getVolume());
            playSong(getArguments().getString(MediaPlayerFragmentActivity.ARG_AUDIO_ID));

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public  void connectService()
    {
        if(playIntent==null){
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }
    @Override
    public void onStop() {
        Log.d(TAG, "******onStop*****");
        super.onStop();
    //    getActivity().unbindService(musicConnection);
    }
    //start and bind the service when the activity starts
    @Override
    public  void onStart() {
        super.onStart();
        Log.d(TAG, "******onStart*****");
       connectService();

    }

    //user song select
    /*public void songPicked(View view){
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();


    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                musicSrv.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    //method to retrieve song info from device
    public void getSongList(){
        //query external audio
        mNewsList = NewsLab.getInstance().getNews();
      /*  ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //ic_add songs to catalogue
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });*/
    }

    public void  playSong(@Nullable String audioUrl){
        // Play song
        if(audioUrl !=null)
        {

            musicSrv.setSong(PlayList.getInstance().getPosition(audioUrl));
            if(musicSrv.playSong())
            {
                btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                songProgressBar.setProgress(0);
                songProgressBar.setMax(100);
            }
            // Displaying Song title
            //      String songTitle = songList.get(songIndex).getTitle();
            //        songTitleLabel.setText(songTitle);


        }


            // Updating progress bar
            updateProgressBar();


    }
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable stream
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if(musicSrv != null) {
                if (!musicSrv.isNullPlayer()) {

                    if (!musicSrv.isPaused()) {
                        long totalDuration = musicSrv.getDur();
                        long currentDuration = musicSrv.getPosn();
                        if (newSongValue != musicSrv.getNewSongValue()) {
                            songTitleLabel.setText(musicSrv.getSongTitle());

                            newSongValue = !newSongValue;
                        }
                        if (playingValue != musicSrv.isPlaying()) {
                            if (playingValue) {
                                btnPlay.setImageResource(android.R.drawable.ic_media_play);
                            } else {
                                btnPlay.setImageResource(android.R.drawable.ic_media_pause);

                            }
                            playingValue = !playingValue;
                        }

                        songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
                        songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

                        // Updating progress bar
                        int progress = (utils.getProgressPercentage(currentDuration, totalDuration));
                        //Log.d("Progress", ""+progress);
                        songProgressBar.setProgress(progress);

                    }
                }
            }
            // Running this stream after 100 milliseconds
               mHandler.postDelayed(this, 100);
        }
    };
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = musicSrv.getDur();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        musicSrv.seek(currentPosition);

        // update timer progress again
        updateProgressBar();
    }
    public class Utilities {

        /**
         * Function to convert milliseconds time to
         * Timer Format
         * Hours:Minutes:Seconds
         * */
        public String milliSecondsToTimer(long milliseconds){
            String finalTimerString = "";
            String secondsString = "";

            // Convert total duration into time
            int hours = (int)( milliseconds / (1000*60*60));
            int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
            int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
            // Add hours if there
            if(hours > 0){
                finalTimerString = hours + ":";
            }

            // Prepending 0 to seconds if it is one digit
            if(seconds < 10){
                secondsString = "0" + seconds;
            }else{
                secondsString = "" + seconds;}

            finalTimerString = finalTimerString + minutes + ":" + secondsString;

            // return timer string
            return finalTimerString;
        }

        /**
         * Function to get Progress percentage
         * @param currentDuration
         * @param totalDuration
         * */
        public int getProgressPercentage(long currentDuration, long totalDuration){
            Double percentage = (double) 0;

            long currentSeconds = (int) (currentDuration / 1000);
            long totalSeconds = (int) (totalDuration / 1000);

            // calculating percentage
            percentage =(((double)currentSeconds)/totalSeconds)*100;

            // return percentage
            return percentage.intValue();
        }

        /**
         * Function to change progress to timer
         * @param progress -
         * @param totalDuration
         * returns current duration in milliseconds
         * */
        public int progressToTimer(int progress, int totalDuration) {
            int currentDuration = 0;
            totalDuration = (int) (totalDuration / 1000);
            currentDuration = (int) ((((double)progress) / 100) * totalDuration);

            // return current duration in milliseconds
            return currentDuration * 1000;
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "******onPause*****");
    }

    public static MediaPlayerFragment newInstance(String audioId) {
        MediaPlayerFragment fragment = new MediaPlayerFragment();
        Bundle args = new Bundle();
        args.putString(MediaPlayerFragmentActivity.ARG_AUDIO_ID, audioId);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
