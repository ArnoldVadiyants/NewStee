package com.newstee;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newstee.model.data.Audio;
import com.newstee.model.data.AudioLab;
import com.newstee.network.interfaces.NewsTeeApiInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 * 
 * Sue Smith - February 2014
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {


    private final static String TAG = "MusicService";

    public boolean getNewSongValue() {
        return newSongValue;
    }
    private void updateNewSongValue()
    {
        if(newSongValue)
        {
            newSongValue = false;
        }
        else
        {
            newSongValue = true;
        }
    }
    private boolean newSongValue = false;

    public boolean isPaused() {
        return paused;
    }

    //media play
    private boolean paused = false;



    private MediaPlayer player;
    //song catalogue
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    private String songId;
    private String songUrl;
    //binder
    private final IBinder musicBind = new MusicBinder();

    public String getSongTitle() {
        return songTitle;
    }

    //title of current song
    private String songTitle="";
    //notification id
    private static final int NOTIFY_ID=1;
    //shuffle flag and random
    private boolean shuffle=false;
    private Random rand;

    public void onCreate(){
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //random
        rand=new Random();
        //create play
        player = new MediaPlayer();
        //initialize
        initMusicPlayer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    Log.d(TAG ,""+ songPosn );

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void initMusicPlayer(){
        //set play properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    //pass song catalogue
    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    //binder
    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent){
        if(player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }

        return false;
    }

    //play a song
    public void playSong(){
        new AudioAsyncTask().execute();
    }



    class AudioAsyncTask extends AsyncTask<String,Integer, AudioLab>

    {
        ProgressDialog pDialog;


        @Override
        protected AudioLab doInBackground(String... params) {

            Call<AudioLab> call = newsTeeApiInterface.getAudio();
            AudioLab audioLab = new AudioLab();
            try {
                Response<AudioLab> response = call.execute();
                audioLab = response.body();
                List<Audio> audio = audioLab.getData();
                if(songId.equals("3") )
                {
                    songId = "2";
                }
                for(Audio a : audio)
                {
                    if(a.getId().equals(songId))
                    {
                        songUrl = a.getSource();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return audioLab;
        }

        @Override
        protected void onPreExecute() {
            player.reset();
            // Showing progress dialog before sending http request
      /*      pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show(); */       }

        @Override
        protected void onPostExecute(AudioLab audioLab) {
           super.onPostExecute(audioLab);
      //      pDialog.dismiss();
          //  Uri trackUri =Uri.parse(songUrl);
       /*         ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);*/
            //set the data source
            try{

                player.setDataSource(songUrl);
                player.prepare();
                player.start();
            }
            catch(Exception e){
                Log.e("MUSIC SERVICE", "Error setting data source", e);
            }




        }
    }







    //set the song
    public void setSong(String songId){
        this.songId=songId;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //check if playback has reached the end of a track
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        //notification
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);
        paused = false;
        updateNewSongValue();
    }


    //playback methods
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }
    public boolean isNullPlayer(){
       return player==null;
    }
    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    //skip to previous track
    public void playPrev(){
        songPosn--;
        if(songPosn<0) songPosn=songs.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){
        if(shuffle){
            int newSong = songPosn;
            while(newSong==songPosn){
                newSong=rand.nextInt(songs.size());
            }
            songPosn=newSong;
        }
        else{
            songPosn++;
            if(songPosn>=songs.size()) songPosn=0;
        }
        playSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    //toggle shuffle
    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }

}
