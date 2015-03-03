package com.kaineras.pilliadventuremobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.toolbox.NetworkImageView;
import com.kaineras.pilliadventuremobile.custom.CustomNetworkImageView;
import com.kaineras.pilliadventuremobile.tools.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    View v;
    final static String url="http://pilli-adventure.com/wp-content/uploads/2008/07/cast-web.jpg";

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //NetworkImageView nivComic;
        CustomNetworkImageView nivComic;
        v = inflater.inflate(R.layout.fragment_about, container, false);
        nivComic = (CustomNetworkImageView) v.findViewById(R.id.ivAbout);
        Tools.loadImageFromInternet(getActivity(), nivComic, url);
        return v;
    }


}
