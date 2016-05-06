package com.newstee;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newstee.helper.SQLiteHandler;
import com.newstee.helper.SessionManager;
import com.newstee.model.data.AuthorLab;
import com.newstee.model.data.DataAuthor;
import com.newstee.model.data.DataNews;
import com.newstee.model.data.DataPost;
import com.newstee.model.data.DataTag;
import com.newstee.model.data.DataUserAuthentication;
import com.newstee.model.data.NewsLab;
import com.newstee.model.data.Tag;
import com.newstee.model.data.TagLab;
import com.newstee.model.data.User;
import com.newstee.model.data.UserLab;
import com.newstee.network.FactoryApi;
import com.newstee.network.interfaces.NewsTeeApiInterface;
import com.newstee.utils.DisplayImageLoaderOptions;
import com.newstee.utils.MPUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  implements  SeekBar.OnSeekBarChangeListener{
    private SessionManager session;
    private static String TAG = "MainActivity";
    private MusicService musicSrv;
    private SQLiteHandler db;
    private final  int[] tabIcons = {
            R.drawable.tab_image_thread,
            R.drawable.tab_image_list,
            R.drawable.tab_image_player,
            R.drawable.tab_image_profile,
            R.drawable.tab_image_car_mode};
    private boolean isMPShow = false;
private View mediaPlayer;
    private  TabLayout mTabLayout;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
     /**
     * The {@link ViewPager} that will host the section contents.
     */
     private MPUtilities utils;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private FrameLayout mProgress;
    private ViewPager mViewPager;
    private ImageButton mpBtnPlay;
    private Handler mHandler = new Handler();
    private int newSongValue = -1;
    private TextView mpTitle;
    private ImageView mpPicture;
    private SeekBar mpDuring;
    private boolean mpPlayingValue = false;
    private Intent playIntent;
    //binding
    private boolean musicBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        utils = new MPUtilities();
        View view =  findViewById(R.id.main_toolbar);

        mediaPlayer = view.findViewById(R.id.main_media_player);
        mpBtnPlay = (ImageButton) mediaPlayer.findViewById(R.id.media_player_small_play_button);
        mpTitle = (TextView) mediaPlayer.findViewById(R.id.media_player_small_title_TextView);
        mpPicture =  (ImageView) mediaPlayer.findViewById(R.id.media_player_small_picture_ImageView);
        mpDuring = (SeekBar)mediaPlayer.findViewById(R.id.media_player_small_during_seekBar);
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getResources().getColor(R.color.colorPrimary, null);
        }
        else {
            color = getResources().getColor(R.color.colorPrimary);
        }
        mpDuring.getProgressDrawable().setColorFilter(
                color, PorterDuff.Mode.SRC_IN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mpDuring.getThumb().mutate().setAlpha(0);
        }
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mpDuring.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } */

        mpDuring.setOnSeekBarChangeListener(this);
        mediaPlayer.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mProgress = (FrameLayout)findViewById(R.id.main_progress);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mpBtnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (musicBound) {
                    if (musicSrv != null) {
                        if (musicSrv.isPlaying()) {

                            musicSrv.pausePlayer();
                            mpBtnPlay.setImageResource(R.drawable.ic_media_play);
                        } else {
                            // Resume song
                            musicSrv.go();
                            // Changing button image to pause button
                            mpBtnPlay.setImageResource(R.drawable.ic_media_pause);

                        }
                    }
                }


            }
        });

      /*  // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(2);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
    //    mTabLayout.setupWithViewPager(mViewPager);

        int dpValue1 = 6;
        int dpValue2 = 8;// padding in dips
        float d = getResources().getDisplayMetrics().density;
        int padding1 = (int)(dpValue1 * d);
        int padding2 = (int)(dpValue2 * d);// padding in pixels
        for(int i = 0; i<mTabLayout.getTabCount(); i++)
        {
      //    mTabLayout.getTabAt(i).setIcon(tabIcons[i]);
            LinearLayout layout = ((LinearLayout)((LinearLayout)mTabLayout.getChildAt(0)).getChildAt(i));
            //  layout.setBackground(getDrawable(tabIcons[i]));
            ImageView tab = new ImageView(this);
            tab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            tab.setImageResource(tabIcons[i]);
            tab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
           if(i == 0 || i ==2)
           {
               tab.setPadding(padding2, padding2, padding2, padding2);
           }
           else tab.setPadding(padding1, padding1, padding1, padding1);
            tab.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            layout.addView(tab);
        }
        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(2);*/
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                Call<List<AuthorLab>> call = newsTeeApiInterface.getAuthors();
                try {
                    Response<List<AuthorLab>> response = call.execute();
                    List<AuthorLab> authors = response.body();
                    for(AuthorLab author : authors)
                    {
                        System.out.println("*************");
                        System.out.println("Id " + author.getId() + "name "+ author.getAuthorName() + "avatar " + author.getAvatar() + "subs" + author.getQuantitySubs() );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
*/

        new LoadAsyncTask().execute();
/*
new Thread(new Runnable() {
    @Override
    public void run() {
     //   mViewPager.setAdapter(null);
//mSectionsPagerAdapter = null;
//        mSectionsPagerAdapter.notifyDataSetChanged();
      //  mTabLayout.removeView(mViewPager);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            public void run() {




              *//*  mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                mViewPager.getAdapter().notifyDataSetChanged();*//*

       //         mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
         //       mViewPager.setAdapter(mSectionsPagerAdapter);
         //       mViewPager.getAdapter().notifyDataSetChanged();
            }
        });

      //  mSectionsPagerAdapter.notifyDataSetChanged();
D
    }
}).start();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectService();
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
            if(musicSrv == null)
            {
                return;
            }
            if(musicSrv.isPlaying())
            {
                mediaPlayer.setVisibility(View.VISIBLE);
            }
         //   updateMPNews();
        }

/*
    @Override
    protected void onStart() {
        super.onSt
        connectService();
    }*/
    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicBound = false;
    }
};
    public void connectService() {
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

    }
    public void updateData()
    {
        NewsTeeApiInterface api = FactoryApi.getInstance(this);
        if(session.isLoggedIn()) {

            HashMap<String, String> userData = db.getUserDetails();
            String password = userData.get("password");
            String email = userData.get("email");
            System.out.println("@@@@@@ Пароль " + password + "@@@@ mail" + email);
            Call<DataUserAuthentication> userC = api.signIn(email, password, "ru");
            try {
                Response<DataUserAuthentication> userR = userC.execute();
                String result = userR.body().getResult();
                final String msg = userR.body().getMessage();
                if (result.equals(Constants.RESULT_SUCCESS)) {
                    User u = userR.body().getData().get(0);
                    UserLab.getInstance().setUser(u);
                } else {
                    db.deleteUsers();
                    session.setLogin(false);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            Call<DataAuthor> authorC = api.getAuthors();








    try {
        Response<DataAuthor> authorR = authorC.execute();
        DataAuthor dataAuthor  = authorR.body();
          AuthorLab.getInstance().setAuthors(dataAuthor.getData());
    } catch (IOException e) {
        e.printStackTrace();
    }
        String addedIds =  UserLab.getInstance().getUser().getNewsAddedIds();
        if( addedIds !=null)
        {
            Call<DataNews> newsByIdC = api.getNewsByIds(addedIds);
            try {
                Response<DataNews>  newsByIdR = newsByIdC.execute();
                UserLab.getInstance().setAddedNews(newsByIdR.body().getNews());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String likedIds =  UserLab.getInstance().getUser().getNewsLikedIds();
        if( likedIds !=null)
        {
            Call<DataNews> newsByIdC = api.getNewsByIds(likedIds);
            try {
                Response<DataNews>  newsByIdR = newsByIdC.execute();
                UserLab.getInstance().setLikedNews(newsByIdR.body().getNews());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    Call<DataNews> newsC = api.getNews();

    try {
        Response<DataNews>  newsR = newsC.execute();
        NewsLab.getInstance().setNews(newsR.body().getNews());
    } catch (IOException e) {
        e.printStackTrace();
    }
    Call<DataTag> tagC = api.getTags();

    try {
        Response<DataTag>  tagR = tagC.execute();
        TagLab.getInstance().setTags(tagR.body().getData());
    } catch (IOException e) {
        e.printStackTrace();
    }
        String tagIds =  UserLab.getInstance().getUser().getTagsIds();
        if(tagIds !=null)
        {
            String mas[] = tagIds.split(",");
            for (int i = 0; i < mas.length; i++) {
                mas[i] = mas[i].trim();
            }
            List<Tag>tags = TagLab.getInstance().getTags();
            for(Tag t : tags)
            {
                for (int i = 0; i < mas.length; i++) {
                    if(t.getId().equals(mas[i]))
                    {
                        UserLab.getInstance().addTag(t);
                    }
                }
            }
            }
        }


    private void showContent() {
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(2);
        mTabLayout.setupWithViewPager(mViewPager);

        int dpValue1 = 6;
        int dpValue2 = 8;// padding in dips
        float d = getResources().getDisplayMetrics().density;
        int padding1 = (int)(dpValue1 * d);
        int padding2 = (int)(dpValue2 * d);// padding in pixels
        for(int i = 0; i<mTabLayout.getTabCount(); i++)
        {
            //    mTabLayout.getTabAt(i).setIcon(tabIcons[i]);
            LinearLayout layout = ((LinearLayout)((LinearLayout)mTabLayout.getChildAt(0)).getChildAt(i));
            //  layout.setBackground(getDrawable(tabIcons[i]));
            ImageView tab = new ImageView(getApplicationContext());
            tab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            tab.setImageResource(tabIcons[i]);
            tab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if(i == 0 || i ==2)
            {
                tab.setPadding(padding2, padding2, padding2, padding2);
            }
            else tab.setPadding(padding1, padding1, padding1, padding1);
            tab.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            layout.addView(tab);
        }
        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(2);
        mProgress.setVisibility(View.GONE);

    }
    private void hideContent() {
        mProgress.setVisibility(View.VISIBLE);
    }
    public void showCanalDeatails() {
        startActivity(new Intent(MainActivity.this, CanalFragmentActivity.class));
    }

    public void showMediaPlayer()
    {
        startActivity(new Intent(MainActivity.this, MediaPlayerFragmentActivity.class));
      /*  isMPShow = true;
        //mViewPager = null;
     //   mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
      //  mViewPager = (ViewPager) findViewById(R.id.container);
       // mViewPager.setAdapter(mSectionsPagerAdapter);
       // mViewPager.setCurrentItem(2);

        //TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(mViewPager);

//        mViewPager.setCurrentItem(2);
     *//*   int[] tabIcons = {
                R.drawable.tab_image_thread,
                R.drawable.tab_image_list,
                R.drawable.tab_image_player,
                R.drawable.tab_image_profile,
                R.drawable.tab_image_car_mode};

        for(int i = 0; i<tabLayout.getTabCount(); i++)
        {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
        isMPShow = true;*//*
      //  mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(2);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        for(int i = 0; i<mTabLayout.getTabCount(); i++)
        {
            mTabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
        mViewPager.setCurrentItem(2);*/
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

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
    public void updateMediaPlayer() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if(musicSrv != null) {
                if (!musicSrv.isNullPlayer()) {
                    if (!musicSrv.isPaused()) {

                        long totalDuration = musicSrv.getDur();
                        long currentDuration = musicSrv.getPosn();
                        if (newSongValue != musicSrv.getNewSongValue()) {
                            mpTitle.setText(musicSrv.getSongTitle());
                            imageLoader.displayImage(musicSrv.getNewsPictureUrl(),mpPicture, DisplayImageLoaderOptions.getInstance());
                            newSongValue = musicSrv.getNewSongValue();
                        }
                        if (mpPlayingValue != musicSrv.isPlaying()) {
                            if (mpPlayingValue) {
                                mpBtnPlay.setImageResource(R.drawable.ic_media_play);
                            } else {
                                mpBtnPlay.setImageResource(R.drawable.ic_media_pause);

                            }
                            mpPlayingValue = !mpPlayingValue;
                        }

                        // Updating progress bar
                        int progress = (utils.getProgressPercentage(currentDuration, totalDuration));
                        //Log.d("Progress", ""+progress);
                        Log.d(TAG, "Progress " + progress + " bufferedProgress "+musicSrv.getBufferPosition());
                        mpDuring.setProgress(progress);
                        mpDuring.setSecondaryProgress(musicSrv.getBufferPosition());

                    }
                }
            }

            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(musicBound)
        {
            if(musicSrv != null)
            {
                if(musicSrv.isPlaying())
                {
                    mediaPlayer.setVisibility(View.VISIBLE);
                }
            }
        }
        updateMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    private  class LoadAsyncTask extends AsyncTask<String, String, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         hideContent();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showContent();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            updateData();
            return true;
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logMenu = menu.findItem(R.id.action_logout);
        if(session.isLoggedIn())
        {
            logMenu.setTitle(R.string.btn_logout);
        }
        else
        {
            logMenu.setTitle(R.string.btn_login);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            if(session.isLoggedIn())
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage(R.string.logout_msg);
                alertDialogBuilder.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                      NewsTeeApiInterface nApi =  FactoryApi.getInstance(getApplicationContext());
                        Call<DataPost> logout = nApi.logOut();
                        logout.enqueue(new Callback<DataPost>() {
                            @Override
                            public void onResponse(Call<DataPost> call, Response<DataPost> response) {
                                String result = response.body().getResult();
                                String msg = response.body().getMessage();
                                if (result.equals(Constants.RESULT_SUCCESS))
                                {
                                    db.deleteUsers();
                                    session.setLogin(false);
                                    UserLab.getInstance().resetData();
                                    FactoryApi.reset();
                                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Fail"+ msg, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DataPost> call, Throwable t) {
Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

                alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else
            {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
public class ProgressPagerAdapter extends FragmentPagerAdapter
    {
        public ProgressPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return new ProgressFragment();
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
           // return new ProgressFragment();
            switch (position) {
                case 0:

                    return new NewsThreadFragment();
                case 1:

                    return new CatalogFragment();
                case 2:

                    if (isMPShow) {
                       // isMPShow = false;
                        return new MediaPlayerFragment();

                    } else
                    {

                        return new MyPlaylistFragment();
                    }

                            // MediaPlayerFragment();
                case 3:

                    return new ProfileFragment();
                case 4:

                    return new CarModeFragment();
            }

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

     /*   @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_image_thread);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString("");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
       *//*    switch (position) {
                case 0:
                    return (getResources().getString(R.string.lenta)).toUpperCase();
                case 1:
                    return (getResources().getString(R.string.catalog)).toUpperCase();
                case 2:
                    return (getResources().getString(R.string.play_list)).toUpperCase();
            }
            return null;*//*
         //   return null;
        }*/
    }

    /**
     * A placeholder fragment containing a simple view.
     */
          public static class PlaceholderFragment extends Fragment {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private static final String ARG_SECTION_NUMBER = "section_number";

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            public static PlaceholderFragment newInstance(int sectionNumber) {
                PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
                return fragment;
            }

            public PlaceholderFragment() {
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
        //    stopService(new Intent(this, MusicService.class));

    }


}
