package com.newstee;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Arnold on 23.02.2016.
 */
public class MyPlaylistFragment extends Fragment{
    public static boolean ifShowMediaPlayer = false;
    private LinearLayout startButton;
    private PlayListPager mPlayListPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    // private ViewPager mViewPager;


    @Override
    public void onResume() {
        super.onResume();
        if(ifShowMediaPlayer)
        {

        }

    }
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_playlist, container, false);
        startButton = (LinearLayout)rootView.findViewById(R.id.my_playlist_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "add all news to playlist", Toast.LENGTH_SHORT).show();
            }
        });
         mPlayListPager = new PlayListPager(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager)rootView.findViewById(R.id.my_playlist_container);
        mViewPager.setAdapter(mPlayListPager);
        mViewPager.setCurrentItem(0);

        TabLayout tabLayout = (TabLayout)rootView.findViewById(R.id.my_playlist_tabs);
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
    public class PlayListPager extends FragmentPagerAdapter {

        public PlayListPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new NewsPlaylistListFragment();
                case 1:
                    return  new NewsPlaylistListFragment();
            }

            return PlaceholderFragment.newInstance(position);
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
                    return getActivity().getResources().getString(R.string.subscriptions);
                case 1:
                    return getActivity().getResources().getString(R.string.added);

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

}


