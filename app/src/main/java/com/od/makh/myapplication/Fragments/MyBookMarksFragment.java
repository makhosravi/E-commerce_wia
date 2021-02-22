package com.od.makh.myapplication.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.od.makh.myapplication.Adapter.PostsAdapterType2;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.Model.Post;
import com.od.makh.myapplication.Network.DivarPostRequest;
import com.od.makh.myapplication.R;
import com.od.makh.myapplication.Utils.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wiadevelopers.com.library.DivarUtils;
import wiadevelopers.com.library.MaterialLoading.MaterialProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBookMarksFragment extends Fragment
{

    private View headerFilter, headerNoInternetConnection;
    private TextView txtNoBookMark, txtFilterText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialProgressBar progressBar;
    private RecyclerView mRecyclerView;

    private final ArrayList<Post> postsList = new ArrayList<Post>();
    private PostsAdapterType2 mPostsAdapter;

    public MyBookMarksFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_book_marks, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view)
    {
        findViews(view);
        setTypefaces();
        setupFragment();
        setupListeners();
    }

    private void findViews(View view)
    {
        txtFilterText = view.findViewById(R.id.txtText);
        progressBar = view.findViewById(R.id.progressBar);
        txtNoBookMark = view.findViewById(R.id.txtNoAdvertise);
        headerNoInternetConnection = view.findViewById(R.id.headerNoInternetConnection);
        headerFilter = view.findViewById(R.id.headerFilter);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = view.findViewById(R.id.postItems);
    }

    private void setTypefaces()
    {
        txtFilterText.setTypeface(DivarUtils.faceLight);
        txtNoBookMark.setTypeface(DivarUtils.face);
    }

    private void setupFragment()
    {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostsAdapter = new PostsAdapterType2(getContext(),postsList,PostsAdapterType2.BOOK_MARK);
        mRecyclerView.setAdapter(mPostsAdapter);
        getMarkedListPosts();
    }

    private void setupListeners()
    {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getMarkedListPosts();
            }
        });

        headerNoInternetConnection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getMarkedListPosts();
            }
        });
    }

    private void getMarkedListPosts()
    {
        final String url = Constant.BASE_URL + "markedList.php";

        headerFilter.setVisibility(View.VISIBLE);
        headerNoInternetConnection.setVisibility(View.GONE);
        txtNoBookMark.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


        JSONObject jsonData = new JSONObject();
        try
        {
            String phone = DivarUtils.readDataFromStorage(Constant.USER_PHONE, null);
            jsonData.put(Constant.USER_PHONE, phone.toString().trim());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<ArrayList<Post>> listener = new Response.Listener<ArrayList<Post>>()
        {
            @Override
            public void onResponse(ArrayList<Post> response)
            {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);

                if (response == null || response.size() == 0)
                {
                    txtNoBookMark.setVisibility(View.VISIBLE);
                }
                else
                {
                    postsList.clear();
                    postsList.addAll(response);
                    mPostsAdapter.notifyDataSetChanged();
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressBar.setVisibility(View.GONE);
                headerFilter.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                headerNoInternetConnection.setVisibility(View.VISIBLE);
            }
        };

        DivarPostRequest request = new DivarPostRequest(url, jsonData, listener, errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        AppSingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
