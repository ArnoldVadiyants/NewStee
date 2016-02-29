package com.newstee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Arnold on 20.02.2016.
 */
public class NewsThreadFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate( R.layout.fragment_thread,container, false );
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment  = new NewsThreadListFragment();
            fm.beginTransaction().add(R.id.thread_content, fragment)
                    .commit();

        return view;
    }
}

