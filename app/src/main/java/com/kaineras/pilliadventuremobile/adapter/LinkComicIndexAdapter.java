package com.kaineras.pilliadventuremobile.adapter;

/**
 * Created the first version by kaineras on 3/02/15.
 */

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kaineras.pilliadventuremobile.R;
import com.kaineras.pilliadventuremobile.custom.CustomImageView;
import com.kaineras.pilliadventuremobile.pojo.ComicsIndexData;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.util.List;

public class LinkComicIndexAdapter extends ArrayAdapter<ComicsIndexData> {

    private List<ComicsIndexData> comicsIndexDataList;
    private Context ctx;

    public LinkComicIndexAdapter(List<ComicsIndexData> optionList, Context ctx) {
        super(ctx, R.layout.comic_index_layout, optionList);
        this.ctx = ctx;
        this.comicsIndexDataList = optionList;
    }

    @Override
    public int getCount() {
        return comicsIndexDataList.size();
    }

    @Override
    public ComicsIndexData getItem(int position) {
        return comicsIndexDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return comicsIndexDataList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Tools tools = new Tools();
        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inf.inflate(R.layout.comic_index_layout, null);
        TextView tName = (TextView) v.findViewById(R.id.nameIndex);
        ImageView icoIma = (ImageView) v.findViewById(R.id.imageIndex);
        tName.setText(comicsIndexDataList.get(position).getName());
        Glide.with(ctx).load(comicsIndexDataList.get(position).getUrlImage()).fitCenter().into(icoIma);
        return v;
    }

}
