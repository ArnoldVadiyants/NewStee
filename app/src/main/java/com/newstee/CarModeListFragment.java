package com.newstee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

            FragmentManager fm = getChildFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.car_mode_list_content);
            if (fragment == null) {
                fragment = new CarModeNewsListFragment();
                fm.beginTransaction().add(R.id.car_mode_list_content, fragment)
                        .commit();
            }



        return rootView;
    }


}

