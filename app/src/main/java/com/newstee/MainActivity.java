package com.newstee;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
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
    private String BASE_URL = "http://213.231.4.68/music-web/app/php/android/";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build();
    private NewsTeeInterface newsTeeInterface = retrofit.create(NewsTeeInterface.class);
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view =  findViewById(R.id.main_toolbar);
        mediaPlayer = view.findViewById(R.id.main_media_player);
        mediaPlayer.setVisibility( View.GONE);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(2);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
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
        mViewPager.setCurrentItem(2);
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
                Call<List<Author>> call = newsTeeInterface.getAuthors();
                try {
                    Response<List<Author>> response = call.execute();
                    List<Author> authors = response.body();
                    for(Author author : authors)
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
        ImageLoader imageLoader = ImageLoader.getInstance();
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(100 * 1024 * 1024)
                .writeDebugLogs()
                .build();
        imageLoader.init(config);



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
    public boolean onPrepareOptionsMenu(Menu menu) {
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

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
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
            stopService(new Intent(this, MusicService.class));

    }


}
