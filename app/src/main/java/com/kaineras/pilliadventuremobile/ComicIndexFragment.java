package com.kaineras.pilliadventuremobile;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kaineras.pilliadventuremobile.adapter.LinkComicIndexAdapter;
import com.kaineras.pilliadventuremobile.pojo.ComicsIndexData;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class ComicIndexFragment extends Fragment {


    private List<ComicsIndexData> comicsIndexDataList = new ArrayList<>();

    public ComicIndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comic_index_list, container, false);
        ListView lView = (ListView) v.findViewById(R.id.list_comic_index);
        LinkComicIndexAdapter la = new LinkComicIndexAdapter(comicsIndexDataList, getActivity());
        lView.setAdapter(la);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getActivity(),ComicsViewFullScreenActivity.class);
                intent.putExtra("INDEX",comicsIndexDataList.get(position).getUrlDest());
                intent.putExtra("PAGE",true);
                startActivity(intent);
            }
        });
        setHasOptionsMenu(true);
        return v;
    }

    private void init() {
        Resources res=getResources();
        Tools tools = new Tools();
        Map setting = tools.getPreferences(getActivity());
        List<String> urlImage;
        List<String> urlDest;
        List<String> name;
        if("espa".equals(setting.get("language"))){
            urlImage= Arrays.asList(res.getStringArray(R.array.url_chapters_image_esp));
            urlDest= Arrays.asList(res.getStringArray(R.array.url_chapters_dest_esp));
            name= Arrays.asList(res.getStringArray(R.array.name_chapter_esp));
        }else{
            urlImage= Arrays.asList(res.getStringArray(R.array.url_chapters_image_eng));
            urlDest= Arrays.asList(res.getStringArray(R.array.url_chapters_dest_eng));
            name= Arrays.asList(res.getStringArray(R.array.name_chapter_eng));
        }

        for(int i=0;i<name.size();i++) {
            comicsIndexDataList.add(new ComicsIndexData(name.get(i),urlImage.get(i),urlDest.get(i)));
            Log.d("INDICES", name.get(i));
        }
    }

}
