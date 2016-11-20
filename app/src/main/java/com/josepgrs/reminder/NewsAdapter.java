package com.josepgrs.reminder;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public static final int SET = 0;
    public static final int RECENT = 1;
    private List<NewsInfo> newsList;
    private ArrayList<Integer> mDataSetTypes;

    public NewsAdapter(List<NewsInfo> newsList, ArrayList<Integer> mDataSetTypes) {
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
            final SetViewHolder holder = (SetViewHolder) viewHolder;
            NewsInfo newsInfo = newsList.get(position);
            holder.SetContext.setText(newsInfo.getInfo());
            holder.SetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateUserName(holder.SetEditText.getText().toString(), newsList.get(position));
                }
            });
        } else if (viewHolder.getItemViewType() == RECENT) {
            NewsViewHolder holder = (NewsViewHolder) viewHolder;
            NewsInfo newsInfo = newsList.get(position);
            holder.vInfoContext.setText(newsInfo.getInfo());
            holder.vInfo.setText(newsInfo.getInfo());

        }

    }

    private void updateUserName(String username, final NewsInfo teste) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map = new HashMap<>();
        map.put("/users/" + MainView.uid + "/username", username);
        map.put("/username/" + username, MainView.uid);
        mDatabase.updateChildren(map);
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes.get(position);

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
        protected TextView SetButton;

        public SetViewHolder(View v) {
            super(v);
            SetContext = (TextView) v.findViewById(R.id.SetContext);
            SetEditText = (EditText) v.findViewById(R.id.SetEditText);
            SetButton = (TextView) v.findViewById(R.id.setButton);
        }
    }
}