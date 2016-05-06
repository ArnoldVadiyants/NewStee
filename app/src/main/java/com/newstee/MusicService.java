package com.newstee;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.newstee.model.data.AuthorLab;
import com.newstee.model.data.News;

import java.io.IOException;
import java.util.Random;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 * 
 * Sue Smith - February 2014
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,  MediaPlayer.OnBufferingUpdateListener {

    private final static int MAX_VOLUME = 50;
    private final static String TAG = "MusicService";
    private boolean paused = true;
    private MediaPlayer player;
    private int mBufferPosition;
    private String songTitle="";
    private String songContent="";
    private String canalTitle;
    private String newsPictureUrl="";



    private String newsDate = "";
    private static final int NOTIFY_ID=1;
    //shuffle flag and random
    private boolean shuffle=false;
    private Random rand;

    public int getSongPosition() {
        return songPosition;
    }

    private int songPosition;
    private final IBinder musicBind = new MusicBinder();
    private int newSongValue = 1;
    private int currentVolume = 50;
    public int getNewSongValue() {
        return newSongValue;
    }
    private void updateNewSongValue()
    {
       newSongValue++;
    }


    public boolean isPaused() {
        return paused;
    }

    //media play

    //song catalogue
   // private List<News> mNews;
    //current position


    public String getSongTitle() {
        return songTitle;
    }

    //title of current song

    //notification id


    public void onCreate(){
        //create the service
        super.onCreate();
        songPosition =-1;
        rand=new Random();
        player = new MediaPlayer();
        initMusicPlayer();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    Log.d(TAG ,""+ songPosition );

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }

    public void initMusicPlayer(){
        //set play properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnBufferingUpdateListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    //pass song catalogue
  //  public void setList(List<News> news){
  //      mNews=news;
    //}
    //binder
    public int getVolume()
    {

        return currentVolume;
       /* AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);

        int volumeLevel = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return volumeLevel*100/maxVolume;*/
    }

    public void setVolume(int value)
    {
        currentVolume = value;
        if(currentVolume == MAX_VOLUME)
        {
            player.setVolume(1, 1);
            return;
        }
        float log1 = (float)(Math.log(MAX_VOLUME-currentVolume)/Math.log(MAX_VOLUME));
        player.setVolume(1 - log1, 1-log1);

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //setBufferPosition(percent * player.getDuration() / 100);
        setBufferPosition(percent);
    }

    public int getBufferPosition() {
        return mBufferPosition;
    }

    public void setBufferPosition(int bufferPosition) {
        this.mBufferPosition = bufferPosition;
    }

    public String getNewsPictureUrl() {
        return newsPictureUrl;
    }

    public String getSongContent() {
        return songContent;
    }

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
  /*  @Override
    public boolean onUnbind(Intent intent){
        if(player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }

        return false;
    }*/

    //play a song
    public boolean playSong() {
        paused = true;
        try {
            player.stop();
            player.reset();
            player.setDataSource(PlayList.getInstance().getNewsList().get(songPosition).getLinksong());
            player.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            paused = false;
            return false;
        } catch (IOException e) {
            paused = false;
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            paused = false;
            e.printStackTrace();
            return false;
        }
        catch (SecurityException e) {
            e.printStackTrace();
            paused = false;
            return false;
        }
        catch(IndexOutOfBoundsException e)
        {      e.printStackTrace();
            paused = false;
            return false;
        }
        return true;

    }


   /* class AudioAsyncTask extends AsyncTask<String,Integer, AudioLab>

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
      *//*      pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show(); *//*       }

        @Override
        protected void onPostExecute(AudioLab audioLab) {
           super.onPostExecute(audioLab);
      //      pDialog.dismiss();
          //  Uri trackUri =Uri.parse(songUrl);
       *//*         ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);*//*
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
    }*/





    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    //set the song
    public void setSong(int songPosition){
        this.songPosition=songPosition;
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

    public String getCanalTitle() {
        return canalTitle;
    }

    public void setCanalTitle(String canalTitle) {
        this.canalTitle = canalTitle;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        //notification
       /* Intent notIntent = new Intent(this, MediaPlayerFragmentActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
       // Icon i = Icon.createWithContentUri()
        builder.setContentIntent(pendInt)
             //   .setSmallIcon()
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);*/
        News n = PlayList.getInstance().getNewsList().get(songPosition);
        songTitle = n.getTitle();
        canalTitle = AuthorLab.getInstance().getAuthor( n.getIdauthor()).getName();
        songContent = n.getContent();
        newsPictureUrl = n.getPictureNews();
        newsDate = n.getAdditionTime();

        paused = false;
        updateNewSongValue();
    }


    //playback methods
    public int getPosn(){
        if(player == null)
        {
            return 0;
        }
        return player.getCurrentPosition();
    }

    public int getDur(){
        if(player == null)
        {
            return -1;
        }
            return player.getDuration();
    }
    public boolean isNullPlayer(){
       return player == null;
    }
    public boolean isPlaying(){
        if(player == null)
        {
          return false;
        }
        try {
            return player.isPlaying();
        }
        catch (IllegalStateException e)
        {
            return false;
        }


    }

    public void pausePlayer(){
        if(player == null)
        {
            return;
        }
        try {
            player.pause();
        }
        catch (IllegalStateException e)
        {
        }

    }

    public void seek(int posn){
        if( player == null)
        {
            return;
        }
        if(posn >=player.getDuration())
        {
            playNext();
        }
        else
        {
            player.seekTo(posn);
        }

    }

    public void go(){
        if( player == null)
        {
            return;
        }
        player.start();
    }

    //skip to previous track
    public void playPrev(){
        songPosition--;
        if(songPosition <0) songPosition =PlayList.getInstance().getNewsList().size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){
        if(shuffle){
            int newSong = songPosition;
            while(newSong== songPosition){
                newSong=rand.nextInt(PlayList.getInstance().getNewsList().size());
            }
            songPosition =newSong;
        }
        else{
            songPosition++;
            if(songPosition >=PlayList.getInstance().getNewsList().size()) songPosition =0;
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
