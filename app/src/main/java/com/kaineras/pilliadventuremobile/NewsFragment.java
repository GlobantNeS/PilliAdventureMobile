package com.kaineras.pilliadventuremobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created the first version by kaineras on 12/03/15.
 */
public class NewsFragment extends Fragment {

    private static final String URL = "http://www.pilli-adventure.com";
    //private static final String URL = "http://www.globant.com";

    public NewsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView;
        WebView webView;
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        webView = (WebView) rootView.findViewById(R.id.web_news);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(URL);
        return rootView;
    }
}
