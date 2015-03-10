package com.kaineras.pilliadventuremobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kaineras.pilliadventuremobile.custom.CustomImageView;
import com.kaineras.pilliadventuremobile.tools.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private static final String URL = "http://pilli-adventure.com/wp-content/uploads/2008/07/cast-web.jpg";

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        final CustomImageView nivComic;
        Tools tools=new Tools();
        v = inflater.inflate(R.layout.fragment_about, container, false);
        nivComic = (CustomImageView) v.findViewById(R.id.ivAbout);
        nivComic.setAdjustViewBounds(true);
        tools.loadImageFromInternet(getActivity(), nivComic, URL);
        return v;
    }
}
