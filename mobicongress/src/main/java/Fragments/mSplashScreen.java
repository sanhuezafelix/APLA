package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 27-03-14.
 */
public class mSplashScreen extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.splash_layout,container,false);
        return RootView;
    }
}
