package com.kaineras.pilliadventuremobile;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.kaineras.pilliadventuremobile.custom.CustomImageView;
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
        final CustomImageView nivComic;
        v = inflater.inflate(R.layout.fragment_about, container, false);
        nivComic = (CustomImageView) v.findViewById(R.id.ivAbout);
        nivComic.setAdjustViewBounds(true);
        Tools.loadImageFromInternet(getActivity(),nivComic,url);
        return v;
    }


}
