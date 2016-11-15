package com.josepgrs.reminder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.josepgrs.reminder.NewsAdapter.RECENT;
import static com.josepgrs.reminder.NewsAdapter.SET;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsView extends android.app.Fragment {

    List<NewsInfo> result = new ArrayList<>();
    private int viewtypes[] = {SET, RECENT, SET};
    public NewsView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        NewsAdapter ca = new NewsAdapter(result, viewtypes);
        recList.setAdapter(ca);

        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setInfo("You haven't set your username,yet. Please take a time to do it");
        result.add(newsInfo);

        ca.notifyDataSetChanged();
    }



}

