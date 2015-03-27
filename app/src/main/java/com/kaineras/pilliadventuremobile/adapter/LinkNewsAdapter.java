package com.kaineras.pilliadventuremobile.adapter;

/**
 * Created the first version by kaineras on 3/02/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kaineras.pilliadventuremobile.R;
import com.kaineras.pilliadventuremobile.custom.CustomImageView;
import com.kaineras.pilliadventuremobile.pojo.NewsData;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.util.List;

public class LinkNewsAdapter extends ArrayAdapter<NewsData> {

    private List<NewsData> newsDataList;
    private Context ctx;

    public LinkNewsAdapter(List<NewsData> newsList, Context ctx) {
        super(ctx, R.layout.news_layout, newsList);
        this.ctx = ctx;
        this.newsDataList = newsList;
    }

    @Override
    public int getCount() {
        return newsDataList.size();
    }

    @Override
    public NewsData getItem(int position) {
        return newsDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return newsDataList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Tools tools = new Tools();
        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inf.inflate(R.layout.news_layout, null);
        TextView tName = (TextView) v.findViewById(R.id.nameNews);
        TextView tDate = (TextView) v.findViewById(R.id.dateNews);
        TextView tEntry = (TextView) v.findViewById(R.id.entryNews);
        CustomImageView icoIma = (CustomImageView) v.findViewById(R.id.imageNews);
        tName.setText(newsDataList.get(position).getName());
        tDate.setText(newsDataList.get(position).getDate());
        tEntry.setText(newsDataList.get(position).getEntry());
        tools.loadImageFromInternet(icoIma, newsDataList.get(position).getImage());
        return v;
    }

}
