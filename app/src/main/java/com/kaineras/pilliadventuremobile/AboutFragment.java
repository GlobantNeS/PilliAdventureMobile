package com.kaineras.pilliadventuremobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kaineras.pilliadventuremobile.custom.CustomImageView;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private static final String URL = "http://pilli-adventure.com/wp-content/uploads/2008/07/cast-web.jpg";
    private static final String URL_ESP = "http://pilli-adventure.com/espa/wp-content/uploads/2009/05/bios.jpg";
    private static final String URL_VIL_ESP = "http://pilli-adventure.com/espa/wp-content/uploads/2009/05/bios.jpg";

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        Map settings;
        final CustomImageView nivAbout;
        Tools tools=new Tools();
        v = inflater.inflate(R.layout.fragment_about, container, false);
        nivAbout = (CustomImageView) v.findViewById(R.id.ivAbout);
        nivAbout.setAdjustViewBounds(true);
        settings=tools.getPreferences(getActivity());
        if("espa".equals(settings.get("language"))){
            CustomImageView nivAboutVil = (CustomImageView) v.findViewById(R.id.ivAboutVil);
            nivAboutVil.setAdjustViewBounds(true);
            tools.loadImageFromInternet(nivAbout, URL_ESP);
            tools.loadImageFromInternet(nivAboutVil, URL_VIL_ESP);
        }else {
            tools.loadImageFromInternet(nivAbout, URL);
        }
        return v;
    }
}
