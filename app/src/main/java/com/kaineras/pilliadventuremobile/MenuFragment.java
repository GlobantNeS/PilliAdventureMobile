package com.kaineras.pilliadventuremobile;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kaineras.pilliadventuremobile.adapter.LinkAdapter;
import com.kaineras.pilliadventuremobile.pojo.OptionsMenu;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {


    private List<OptionsMenu> optionList = new ArrayList<>();

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof OptionsMenuListener)) {
            throw new ClassCastException();
        }
        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.menu_left_layout, container, false);
        ListView lView = (ListView) v.findViewById(R.id.menuList);
        LinkAdapter la = new LinkAdapter(optionList, getActivity());
        lView.setAdapter(la);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                ((OptionsMenuListener) getActivity()).optionsMenuListener(optionList.get(position).getName());
            }
        });
        setHasOptionsMenu(true);
        return v;
    }

    private void init() {
        optionList.add(new OptionsMenu(getString(R.string.text_news), "NEWS", R.drawable.ic_action_action_language));
        optionList.add(new OptionsMenu(getString(R.string.text_comic), "COMIC", R.drawable.ic_action_image_photo));
        optionList.add(new OptionsMenu(getString(R.string.text_about), "ABOUT", R.drawable.ic_menu_contact));
        optionList.add(new OptionsMenu(getString(R.string.text_contact), "CONTACT", R.drawable.ic_action_communication_email));
    }

    public interface OptionsMenuListener {
        public void optionsMenuListener(String optionMenu);
    }
}
