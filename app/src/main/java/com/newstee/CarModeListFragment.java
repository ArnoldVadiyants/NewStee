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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_car_mode, container, false);
        icon =(ImageView) rootView.findViewById(R.id.car_mode_list_icon);
        icon.setImageResource(R.drawable.play_clicked);
        title =(TextView) rootView.findViewById(R.id.car_mode_list_title);
        title.setText(getResources().getText(R.string.tab_play_list));
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
                fragment = new NewsThreadListFragment();
                fm.beginTransaction().add(R.id.car_mode_list_content, fragment)
                        .commit();
            }



        return rootView;
    }


}

