package com.josepgrs.reminder;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public static final int SET = 0;
    public static final int RECENT = 1;
    private List<NewsInfo> newsList;
    private int[] mDataSetTypes;

    public NewsAdapter(List<NewsInfo> newsList, int[] mDataSetTypes) {
        this.newsList = newsList;
        this.mDataSetTypes = mDataSetTypes;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == SET) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_layout_set, viewGroup, false);

            return new SetViewHolder(v);
        } else if (viewType == RECENT) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_layout_news, viewGroup, false);
            return new NewsViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == SET) {
            SetViewHolder holder = (SetViewHolder) viewHolder;
            NewsInfo newsInfo = newsList.get(position);
            holder.SetContext.setText(newsInfo.getInfo());
        } else if (viewHolder.getItemViewType() == RECENT) {
            NewsViewHolder holder = (NewsViewHolder) viewHolder;
            NewsInfo newsInfo = newsList.get(position);
            holder.vInfoContext.setText(newsInfo.getInfo());
            holder.vInfo.setText(newsInfo.getInfo());

        }

    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public static class NewsViewHolder extends ViewHolder {

        protected TextView vInfoContext;
        protected TextView vInfo;


        public NewsViewHolder(View v) {
            super(v);
            vInfoContext = (TextView) v.findViewById(R.id.InfoContext);
            vInfo = (TextView) v.findViewById(R.id.Info);

        }
    }

    public static class SetViewHolder extends ViewHolder {

        protected TextView SetContext;
        protected EditText SetEditText;


        public SetViewHolder(View v) {
            super(v);
            SetContext = (TextView) v.findViewById(R.id.SetContext);
            SetEditText = (EditText) v.findViewById(R.id.SetEditText);
        }
    }
}