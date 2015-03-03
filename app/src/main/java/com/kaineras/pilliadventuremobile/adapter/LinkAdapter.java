package com.kaineras.pilliadventuremobile.adapter;

/**
 * Created the first version by kaineras on 3/02/15.
 */

import java.util.List;

import com.kaineras.pilliadventuremobile.R;
import com.kaineras.pilliadventuremobile.pojo.OptionsMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LinkAdapter extends ArrayAdapter<OptionsMenu> {

    private List<OptionsMenu> optionList;
    private Context ctx;

    public LinkAdapter(List<OptionsMenu> optionList, Context ctx) {
        super(ctx, R.layout.options_layout, optionList);
        this.ctx = ctx;
        this.optionList = optionList;
    }

    @Override
    public int getCount() {
        return optionList.size();
    }

    @Override
    public OptionsMenu getItem(int position) {
        return optionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return optionList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if ( v == null) {
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.options_layout, null);
            TextView tName = (TextView) v.findViewById(R.id.optName);
            ImageView icoIma = (ImageView) v.findViewById(R.id.imageIco);
            tName.setText(optionList.get(position).text);
            icoIma.setImageResource(optionList.get(position).ico);
        }
        return v;
    }

}
