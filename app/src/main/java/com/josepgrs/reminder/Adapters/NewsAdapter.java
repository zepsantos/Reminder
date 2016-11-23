package com.josepgrs.reminder.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josepgrs.reminder.Init.MainView;
import com.josepgrs.reminder.Model.NewsInfo;
import com.josepgrs.reminder.R;

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
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == SET) {
            final SetViewHolder holder = (SetViewHolder) viewHolder;
            NewsInfo newsInfo = newsList.get(position);
            holder.SetContext.setText(newsInfo.getInfo());
            holder.SetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    updateUserName(holder.SetEditText.getText().toString(), newsList.get(position), position, holder.mView);
                }
            });
        } else if (viewHolder.getItemViewType() == RECENT) {
            NewsViewHolder holder = (NewsViewHolder) viewHolder;
            NewsInfo newsInfo = newsList.get(position);
            holder.vInfoContext.setText(newsInfo.getInfo());
            holder.vInfo.setText(newsInfo.getInfo());

        }

    }

    private void updateUserName(String username, final NewsInfo teste, final int position, final View mView) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map = new HashMap<>();
        map.put("/users/" + MainView.uid + "/username", username);
        map.put("/username/" + username, MainView.uid);
        mDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mView.getContext(), R.string.userNameAlreadyUse, Toast.LENGTH_SHORT).show();


                    }
                });
                task.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        newsList.remove(teste);
                        mDataSetTypes.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        Toast.makeText(mView.getContext(), R.string.userNameSet, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
        View mView;
        protected TextView SetContext;
        protected EditText SetEditText;
        protected TextView SetButton;

        public SetViewHolder(View v) {
            super(v);
            mView = v;
            SetContext = (TextView) v.findViewById(R.id.SetContext);
            SetEditText = (EditText) v.findViewById(R.id.SetEditText);
            SetButton = (TextView) v.findViewById(R.id.setButton);
        }
    }
}