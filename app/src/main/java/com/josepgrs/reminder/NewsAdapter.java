package com.josepgrs.reminder;

/**
 * Created by josep on 14/11/2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsInfo> newsList;

    public NewsAdapter(List<NewsInfo> newsList) {
        this.newsList = newsList;
    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public void onBindViewHolder(NewsViewHolder newsViewHolder, int i) {
        NewsInfo ci = newsList.get(i);
        newsViewHolder.vInfoContext.setText(ci.InfoContext);
        newsViewHolder.vInfo.setText(ci.Info);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new NewsViewHolder(itemView);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        protected TextView vInfoContext;
        protected TextView vInfo;


        public NewsViewHolder(View v) {
            super(v);
            vInfoContext = (TextView) v.findViewById(R.id.InfoContext);
            vInfo = (TextView) v.findViewById(R.id.Info);

        }
    }
}