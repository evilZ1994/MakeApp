package com.example.lollipop.makeupapp.ui.base;

import android.support.v4.app.Fragment;

import com.example.lollipop.makeupapp.app.App;
import com.example.lollipop.makeupapp.injector.component.DaggerFragmentComponent;
import com.example.lollipop.makeupapp.injector.component.FragmentComponent;

/**
 * Created by Lollipop on 2017/8/13.
 */

public class BaseFragment extends Fragment {
    private FragmentComponent fragmentComponent;

    public FragmentComponent getFragmentComponent(){
        if (fragmentComponent == null){
            fragmentComponent = DaggerFragmentComponent.builder().applicationComponent(App.getApplicationComponent()).build();
        }
        return fragmentComponent;
    }
}
