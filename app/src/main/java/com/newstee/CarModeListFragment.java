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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Arnold on 17.02.2016.
 */
public class CarModeListFragment extends Fragment {
    private final static String TAG = "CarModeListFragment";

ImageView icon;
    TextView title;
    private PlayListPager mPlayListPager;
    public static CarModeListFragment newInstance(int sectionNumber) {
        CarModeListFragment fragment = new CarModeListFragment();
        Bundle args = new Bundle();
        args.putInt(CarModeListActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public CarModeListFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPlayListPager = new PlayListPager(getChildFragmentManager());
        View rootView = inflater.inflate(R.layout.fragment_car_mode, container, false);

        icon =(ImageView) rootView.findViewById(R.id.car_mode_list_icon);
        title =(TextView) rootView.findViewById(R.id.car_mode_list_title);

        int sectionNumber = getArguments().getInt(CarModeListActivity.ARG_SECTION_NUMBER);

        switch (sectionNumber)
        {
            case CarModeListActivity.TAB_STREAM:
            {
                icon.setImageResource(R.drawable.stream_clicked);
                title.setText(getResources().getText(R.string.tab_stream));
                break;
            }
            case CarModeListActivity.TAB_MY_PLAYLIST:
            {
                icon.setImageResource(R.drawable.play_clicked);
                title.setText(getResources().getText(R.string.tab_play_list));
                break;
            }
            case CarModeListActivity.TAB_RECENT:
            {
                icon.setImageResource(R.drawable.ic_clock);
                title.setText(getResources().getText(R.string.car_mode_tab_recent));
                break;
            }
            case CarModeListActivity.TAB_RECOMMEND:
            {
                icon.setImageResource(R.drawable.ic_star);
                title.setText(getResources().getText(R.string.car_mode_tab_recommend));
                break;
            }
        }
        LinearLayout startBtn = (LinearLayout) rootView.findViewById(R.id.car_mode_list_start_button);
        ImageButton exitBtn = (ImageButton)rootView.findViewById(R.id.car_mode_list_exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ViewPager mViewPager = (ViewPager)rootView.findViewById(R.id.car_mode_list_container);
        mViewPager.setAdapter(mPlayListPager);
        mViewPager.setCurrentItem(0);

        TabLayout tabLayout = (TabLayout)rootView.findViewById(R.id.car_mode_list_tabs);
        tabLayout.setupWithViewPager(mViewPager);

/*

        FragmentManager fm = getChildFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.car_mode_list_content);
            if (fragment == null) {
                fragment = new CarModeNewsListFragment();
                fm.beginTransaction().add(R.id.car_mode_list_content, fragment)
                        .commit();
            }
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
                    return CarModeNewsListFragment.newInstance(Constants.ARGUMENT_NONE,Constants.CATEGORY_NEWS);
                case 1:
                    return CarModeNewsListFragment.newInstance(Constants.ARGUMENT_NONE,Constants.CATEGORY_ARTICLE);
                case 2:
                    return  CarModeNewsListFragment.newInstance(Constants.ARGUMENT_NONE,Constants.CATEGORY_STORY);
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

