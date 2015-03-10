package com.kaineras.pilliadventuremobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kaineras.pilliadventuremobile.custom.CustomImageView;
import com.kaineras.pilliadventuremobile.tools.Tools;

/**
 * Created the first version by kaineras on 12/02/15.
 */
public class ImageComicsViewFragment extends Fragment {

    private static String URL = "URL";
    private static final String INDEX = "INDEX";
    private String url;


    public static ImageComicsViewFragment newInstance(String url, int index) {
        ImageComicsViewFragment fragment = new ImageComicsViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putInt(INDEX, index);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load parameters when the initial creation of the fragment is done
        url = (getArguments() != null) ? getArguments().getString(URL) : "index.html";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Tools tools=new Tools();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_image_comic, container, false);
        CustomImageView nivComic;
        nivComic = (CustomImageView) rootView.findViewById(R.id.ivComic);
        nivComic.setAdjustViewBounds(true);
        tools.loadImageFromInternet(getActivity(), nivComic, url);
        Log.v("URL CACHE EN FRAGMENTO", url);
        return rootView;

    }

}
