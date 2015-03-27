package com.kaineras.pilliadventuremobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaineras.pilliadventuremobile.adapter.LinkNewsAdapter;
import com.kaineras.pilliadventuremobile.pojo.NewsData;
import com.kaineras.pilliadventuremobile.tools.Tools;
import com.kaineras.pilliadventuremobile.tools.VolleySingleton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created the first version by kaineras on 12/03/15.
 */
public class NewsFragment extends Fragment {

    private Tools tools = new Tools();
    private Map<String, String> settings;
    private List<NewsData> newsDataList = new ArrayList<>();
    private LinkNewsAdapter la;
    private static final int INI = 14;
    private static final int FIN = 4;
    private static final String LOG_TAG = NewsFragment.class.getSimpleName();
    private static final String URL = "http://www.pilli-adventure.com";
    private static final String URL_ESP = "http://www.pilli-adventure.com/espa/";

    public NewsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = tools.getPreferences(getActivity());
        init();
    }

    private void init() {
        new ReadNews().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_list, container, false);
        ListView lView = (ListView) v.findViewById(R.id.list_news);
        la = new LinkNewsAdapter(newsDataList, getActivity());
        lView.setAdapter(la);
        setHasOptionsMenu(true);
        return v;
    }

    class ReadNews extends AsyncTask<Void,Void,Void>{

        String language = getLanguage();

        @Override
        protected Void doInBackground(Void... params) {
            StringRequest stringRequest;
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        addNewsToList(response);
                    } catch (MalformedURLException e) {
                        Log.d(LOG_TAG, e.toString());
                        Logger.getLogger(NewsFragment.class.getName()).log(Level.SEVERE, null, e);
                    }

                }
            };

            Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG_TAG, error.toString());
                }
            };
            String url = "espa".equals(language)?URL_ESP:URL;
            stringRequest = new StringRequest(Request.Method.GET, url,responseListener,responseErrorListener);
            VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
            return null;
        }

        private void addNewsToList(String html) throws MalformedURLException {
            Document doc = Jsoup.parse(html);
            Elements images = doc.select("img[src$=.jpg]");
            for(Element image:images){
                if(image.attr("src").contains("comics") && !image.attr("src").contains("banner")){
                    String tmp=image.attr("src");
                    String urlImage=tools.constructURLIma(language, tmp.substring(tmp.length() - INI, tmp.length() - FIN) + ".jpg").toString();
                    NewsData newsData=new NewsData(image.attr("title"),"","",urlImage);
                    newsDataList.add(newsData);
                }
            }
            Elements news = doc.select(".post");
            for(Element nw:news){
                Document document = Jsoup.parse(nw.html());
                String name=document.select("a").first().text()+"\n";
                String date=document.select(".postdate").text();
                String entry=document.select(".entry").text();
                Element imageEntry=document.select("img[src$=.jpg]").first();
                String urlImage=imageEntry!=null?imageEntry.attr("src"):"";
                NewsData newsData=new NewsData(name,date,entry,urlImage);
                newsDataList.add(newsData);
            }
            la.notifyDataSetChanged();
        }
    }

    public String getLanguage() {
        return settings.get("language");
    }
}

