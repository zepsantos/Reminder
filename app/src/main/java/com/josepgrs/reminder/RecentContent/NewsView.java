package com.josepgrs.reminder.RecentContent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.josepgrs.reminder.Adapters.NewsAdapter;
import com.josepgrs.reminder.Model.NewsInfo;
import com.josepgrs.reminder.R;

import java.util.ArrayList;
import java.util.List;

import static com.josepgrs.reminder.Adapters.NewsAdapter.SET;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsView extends android.app.Fragment {

    List<NewsInfo> result = new ArrayList<>();
    NewsAdapter cardviewadapter;
    private ArrayList<Integer> viewtypes = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public static Boolean usernameError = false;
    Boolean firstTimeuser = true;
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        CardViewInit(view);
        CheckUserName();

    }

    private void CheckUserName() {
        final DatabaseReference username = mDatabase.child("/users/").child(mAuth.getCurrentUser().getUid()).child("username");
        username.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                NewsInfo newsInfo = new NewsInfo();

                if (dataSnapshot.getValue(String.class) == null) {
                    if (firstTimeuser) {
                        viewtypes.add(SET);

                        newsInfo.setInfo("Please take a time and set your username.");
                        result.add(newsInfo);
                        cardviewadapter.notifyDataSetChanged();
                        firstTimeuser = false;
                    }

                } else {
                    //USERNAME

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void CardViewInit(View view) {
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        cardviewadapter = new NewsAdapter(result, viewtypes);
        recList.setAdapter(cardviewadapter);


    }


}

