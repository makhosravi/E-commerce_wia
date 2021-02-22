package com.od.makh.myapplication.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.od.makh.myapplication.Adapter.PostsAdapterType2;
import com.od.makh.myapplication.Model.Post;
import com.od.makh.myapplication.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import wiadevelopers.com.library.DivarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastSeenRecentlyFragment extends Fragment
{

    private final ArrayList<Post> postsList = new ArrayList<Post>();
    private PostsAdapterType2 adapter;
    private RecyclerView mRecyclerView;
    private TextView txtNoBookMark;

    private Realm realm;

    public LastSeenRecentlyFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_last_seen_recently, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view)
    {
        findViews(view);
        setTypeFaces();
        setupFragment();
    }

    private void findViews(View view)
    {
        txtNoBookMark = (TextView) view.findViewById(R.id.txtNoAdvertise);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.postItems);
    }

    private void setTypeFaces()
    {
        txtNoBookMark.setTypeface(DivarUtils.face);
    }

    private void setupFragment()
    {
        realm = Realm.getDefaultInstance();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsAdapterType2(getContext(),postsList,PostsAdapterType2.LAST_SEEN);
        mRecyclerView.setAdapter(adapter);
        fetchPostsDataFromDB();
    }

    private void fetchPostsDataFromDB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Post> results = realm.where(Post.class).findAll();
                if (results.size() != 0)
                    assignDataToArrayList(results);
                else
                {
                    txtNoBookMark.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void assignDataToArrayList(RealmResults<Post> results)
    {
        postsList.clear();
        for (int i = 0; i < results.size(); i++)
            postsList.add(results.get(i));
        adapter.notifyDataSetChanged();
    }
}
