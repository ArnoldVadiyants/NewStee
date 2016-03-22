package com.newstee;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Arnold on 17.02.2016.
 */
public class CatalogFragment extends Fragment {
    private final static String TAG = "CatalogFragment";

    View mediaPlayer;
    TextView publish_titleTextView;
    TextView article_titleTextView;
    TextView mixes_titleTextView;
    RelativeLayout publish_relative;
    RelativeLayout article_relative;
    RelativeLayout mixes_relative;
    ImageView publish_icon;
    ImageView article_icon;
    ImageView mixes_icon;
    ImageButton filter_button;
    private boolean newSongValue = false;
    private boolean playingValue = false;
    private Handler mHandler = new Handler();
    ImageButton mpBtnPlay;
    TextView mpTitle;
    private MusicService musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound = false;
    private CatalogPagerAdapter mCatalogPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    // private ViewPager mViewPager;

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

    @Override
    public void onStop() {
        super.onStop();
  //      getActivity().unbindService(musicConnection);
    }

    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_catalog, container, false);
        mediaPlayer = rootView.findViewById(R.id.catalog_media_player);
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

        CatalogPagerAdapter mCatalogPagerAdapter = new CatalogPagerAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.catalog_container);
        mViewPager.setAdapter(mCatalogPagerAdapter);
        mViewPager.setCurrentItem(0);


        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.catalog_tabs);
        tabLayout.setupWithViewPager(mViewPager);
       /* View article_view =  rootView.findViewById(R.id.theme_article_item);
            article_icon = (ImageView) article_view.findViewById(R.id.item_icon);
            article_titleTextView = (TextView) article_view.findViewById(R.id.item_title);
        article_titleTextView.setText(R.string.theme_article);
            article_relative = (RelativeLayout) article_view.findViewById(R.id.item_action);

        View publish_view =  rootView.findViewById(R.id.publish);
            publish_icon = (ImageView) publish_view.findViewById(R.id.item_icon);
            publish_titleTextView = (TextView) publish_view.findViewById(R.id.item_title);
        publish_titleTextView.setText(R.string.publish);
            publish_relative = (RelativeLayout) publish_view.findViewById(R.id.item_action);
        View mixes_view =  rootView.findViewById(R.id.new_mixes);
            mixes_icon = (ImageView) mixes_view.findViewById(R.id.item_icon);
            mixes_titleTextView = (TextView)mixes_view.findViewById(R.id.item_title);
        mixes_titleTextView.setText(R.string.publish);
            mixes_relative = (RelativeLayout)mixes_view.findViewById(R.id.item_action);
            filter_button = (ImageButton)mixes_view.findViewById(R.id.item_filter_button);

*/
        return rootView;
    }

    public class CatalogPagerAdapter extends FragmentPagerAdapter {

        public CatalogPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return CatalogListFragment.newInstance(false);
                case 1:
                    return CatalogListFragment.newInstance(true);
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
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
                    return getActivity().getResources().getString(R.string.category);
                case 1:
                    return getActivity().getResources().getString(R.string.publish);

            }

            return null;
        }


    }

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


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }


    }
}

