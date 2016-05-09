package com.newstee;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newstee.MusicService.MusicBinder;
import com.newstee.model.data.DataPost;
import com.newstee.model.data.News;
import com.newstee.model.data.UserLab;
import com.newstee.network.FactoryApi;
import com.newstee.network.interfaces.NewsTeeApiInterface;
import com.newstee.utils.DisplayImageLoaderOptions;
import com.newstee.utils.MPUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Arnold on 17.02.2016.
 */
public class MediaPlayerFragment extends Fragment implements  SeekBar.OnSeekBarChangeListener {
    private final static String TAG = "MediaPlayerFragment";
    private int newSongValue = -1;
    private boolean playingValue = false;
    private ImageButton btnPlay;
    private MediaConsumer mediaConsumer;
  //  private ImageButton btnForward;
  //  private ImageButton btnBackward;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnLike;
    private ImageView songImageView;
    private SeekBar songProgressBar;
    private SeekBar volumeSeekBar;
    private TextView newsDate;
    private TextView songTitleLabel;
    private TextView songContent;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private MPUtilities utils;
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
        updateMediaPlayer();
    }

    /*public void updateMPNews() {
        if (musicSrv != null) {
            if (musicSrv.isPlaying()) {
                btnPlay.setImageResource(R.drawable.ic_media_pause);
            } else {
                btnPlay.setImageResource(R.drawable.ic_media_play);
            }
            songTitleLabel.setText(musicSrv.getSongTitle());
            songContent.setText(musicSrv.getSongContent());
            imageLoader.displayImage(musicSrv.getNewsPictureUrl(),songImageView, DisplayImageLoaderOptions.getInstance());
        }
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "******onCreateView*****");
        //retrieve catalogue view
        View root = inflater.inflate(R.layout.media_conroller,container, false );
        btnPlay = (ImageButton) root.findViewById(R.id.pause);
        btnLike = (ImageButton)root.findViewById(R.id.like_imageButton);
    //    btnForward = (ImageButton) root.findViewById(R.id.ffwd);
    //    btnBackward = (ImageButton) root.findViewById(R.id.rew);
        btnNext = (ImageButton) root.findViewById(R.id.next);
     //   Log.d("asff", btnForward.getWidth()+"  " + btnBackward.getHeight());
        btnPrevious = (ImageButton) root.findViewById(R.id.prev);
        btnPlaylist = (ImageButton) root.findViewById(R.id.play_list_imageButton);
        volumeSeekBar = (SeekBar)root.findViewById(R.id.volume_seekBar);
        songProgressBar = (SeekBar) root.findViewById(R.id.during_seekBar);
        songImageView = (ImageView)root.findViewById(R.id.canal_imageView);
        songContent = (TextView) root.findViewById(R.id.media_controler_new_content_textView);
        songTitleLabel = (TextView) root.findViewById(R.id.title_TextView);
        songCurrentDurationLabel = (TextView) root.findViewById(R.id.curent_time_TextView);
        songTotalDurationLabel = (TextView) root.findViewById(R.id.total_time_TextView);
        newsDate = (TextView)root.findViewById(R.id.date_TextView);
        utils = new MPUtilities();


btnLike.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(final View v) {
        if(musicSrv == null)
        {
         return;
        }
        final String id = musicSrv.getIdNews();
        if(id == null)
        {
            return;
        }
        NewsTeeApiInterface nApi = FactoryApi.getInstance(getActivity());

        Call<DataPost> call = nApi.likeNews(id);
        call.enqueue(new Callback<DataPost>() {
            @Override
            public void onResponse(Call<DataPost> call, Response<DataPost> response) {
                if (response.body().getResult().equals(Constants.RESULT_SUCCESS)) {
                    UserLab.getInstance().likeNews(PlayList.getInstance().getNewsItem(id));

                    if (UserLab.getInstance().isLikedNews(id)) {
                        Log.d(TAG, "@@@@@@ liked");
                                ((ImageButton) v).setImageResource(R.drawable.ic_is_liked);
                    } else {
                        Log.d(TAG, "@@@@@@ removed");
                        ((ImageButton) v).setImageResource(R.drawable.ic_like);
                    }


                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataPost> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
});
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicSrv == null) {
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
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getResources().getColor(R.color.colorPrimary, null);
        }
        else {
            color = getResources().getColor(R.color.colorPrimary);
        }
        songProgressBar.getProgressDrawable().setColorFilter(
               color, PorterDuff.Mode.SRC_IN);
        volumeSeekBar.getProgressDrawable().setColorFilter(
                color, PorterDuff.Mode.SRC_IN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            songProgressBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            volumeSeekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
       /* btnForward.setOnClickListener(new View.OnClickListener() {

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
        });*/

          /*  btnBackward.setOnClickListener(new View.OnClickListener() {

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
            });*/
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if(musicSrv == null)
                {
                 return;
                }
                musicSrv.playNext();

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if(musicSrv == null)
                {
                    return;
                }
                musicSrv.playPrev();

            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if(musicBound){
                    if(musicSrv != null)
                    {
                        if(musicSrv.isPlaying()){

                            musicSrv.pausePlayer();
                            btnPlay.setImageResource(R.drawable.ic_media_play);
                        }
                        else{
                            // Resume song
                            musicSrv.go();
                            // Changing button image to pause button
                            btnPlay.setImageResource(R.drawable.ic_media_pause);

                        }
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
            String id = musicSrv.getIdNews();
            if(id != null)
            {
                if (UserLab.getInstance().isLikedNews(id)) {
                    Log.d(TAG, "@@@@@@ liked");
                    btnLike.setImageResource(R.drawable.ic_is_liked);
                } else {
                    Log.d(TAG, "@@@@@@ removed");
                    btnLike.setImageResource(R.drawable.ic_like);
                }
            }
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
  /*      mNewsList = NewsLab.getInstance().getNews();*/
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
        String url = "";
        Log.d(TAG,"@@@@@@@ url "+ audioUrl);
        if(audioUrl !=null)
        {
            try
            {
                url = PlayList.getInstance().getNewsList().get(musicSrv.getSongPosition()).getLinksong();
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }


         int location =    PlayList.getInstance().getPosition(audioUrl);
            if(!(url.equals(audioUrl) && location == musicSrv.getSongPosition()))
            {
                Log.d(TAG,"@@@@@@@ playsong "+ audioUrl);
                musicSrv.setSong(location);
                if(musicSrv.playSong())
                {
                    btnPlay.setImageResource(R.drawable.ic_media_pause);
                    songProgressBar.setProgress(0);
                    songProgressBar.setMax(100);
                }
                // Displaying Song title
                //      String songTitle = songList.get(songIndex).getTitle();
                //        songTitleLabel.setText(songTitle);
            }



        }


            // Updating progress bar
        //    updateMediaPlayer();


    }
    public void updateMediaPlayer() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable stream
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Log.d(TAG, "update mediaTask");
            if(musicSrv != null) {
                if (!musicSrv.isNullPlayer()) {
                    if (!musicSrv.isPaused()) {
                        long totalDuration = musicSrv.getDur();
                        long currentDuration = musicSrv.getPosn();
                        if (newSongValue != musicSrv.getNewSongValue()) {
                            String id = musicSrv.getIdNews();
                            if(id != null)
                            {
                                if (UserLab.getInstance().isLikedNews(id)) {
                                    Log.d(TAG, "@@@@@@ liked");
                                    btnLike.setImageResource(R.drawable.ic_is_liked);
                                } else {
                                    Log.d(TAG, "@@@@@@ removed");
                                    btnLike.setImageResource(R.drawable.ic_like);
                                }
                            }
                            songTitleLabel.setText(musicSrv.getSongTitle());
                            songContent.setText(musicSrv.getSongContent());
                            imageLoader.displayImage(musicSrv.getNewsPictureUrl(), songImageView, DisplayImageLoaderOptions.getInstance());
                            newsDate.setText(utils.getDateTimeFormat(musicSrv.getNewsDate()));
                            newSongValue = musicSrv.getNewSongValue();
                        }
                        if (playingValue != musicSrv.isPlaying()) {
                            if (playingValue) {
                                btnPlay.setImageResource(R.drawable.ic_media_play);
                            } else {
                                btnPlay.setImageResource(R.drawable.ic_media_pause);

                            }
                            playingValue = !playingValue;
                        }

                        songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
                        songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

                        // Updating progress bar
                        int progress = (utils.getProgressPercentage(currentDuration, totalDuration));
                        //Log.d("Progress", ""+progress);
                        Log.d(TAG, "Progress " + progress + " bufferedProgress "+musicSrv.getBufferPosition());
                        songProgressBar.setProgress(progress);
                        songProgressBar.setSecondaryProgress(musicSrv.getBufferPosition());

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
     updateMediaPlayer();
    }



    @Override
    public void onPause(){
        super.onPause();
        mHandler.removeCallbacks(mUpdateTimeTask);
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
