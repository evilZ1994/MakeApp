package com.example.lollipop.makeupapp.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lollipop.makeupapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectFragment extends Fragment {

    public static Fragment newInstance(){
        CollectFragment fragment = new CollectFragment();
        return fragment;
    }

    public CollectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }

}
