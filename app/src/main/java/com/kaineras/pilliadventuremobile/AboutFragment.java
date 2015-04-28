package com.kaineras.pilliadventuremobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private static final String URL = "http://pilli-adventure.com/wp-content/uploads/2008/07/cast-web.jpg";
    private static final String URL_ESP = "http://pilli-adventure.com/espa/wp-content/uploads/2009/05/bios.jpg";
    private static final String URL_VIL_ESP = "http://pilli-adventure.com/wp-content/uploads/2009/05/monsters-copia.jpg";

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        Map settings;
        final ImageView nivAbout;
        Tools tools=new Tools();
        v = inflater.inflate(R.layout.fragment_about, container, false);
        nivAbout = (ImageView) v.findViewById(R.id.ivAbout);
        nivAbout.setAdjustViewBounds(true);
        settings=tools.getPreferences(getActivity());
        if("espa".equals(settings.get("language"))){
            ImageView nivAboutVil = (ImageView) v.findViewById(R.id.ivAboutVil);
            nivAboutVil.setAdjustViewBounds(true);
            Glide.with(getActivity()).load(URL_ESP).into(nivAbout);
            Glide.with(getActivity()).load(URL_VIL_ESP).into(nivAboutVil);
        }else {
            Glide.with(getActivity()).load(URL).into(nivAbout);
        }
        return v;
    }
}