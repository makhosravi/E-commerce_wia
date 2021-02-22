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
public class MyAdvertiseFragment extends Fragment
{

    private View headerFilter, headerNoInternetConnection;
    private TextView txtNoAdvertise,txtFilterText;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialProgressBar progressBar;

    private final ArrayList<Post> postsList = new ArrayList<Post>();
    private PostsAdapterType2 mPostsAdapter;

    public MyAdvertiseFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_advertise, container, false);
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
        txtFilterText = (TextView) view.findViewById(R.id.txtText);
        txtNoAdvertise = (TextView) view.findViewById(R.id.txtNoAdvertise);

        progressBar = (MaterialProgressBar) view.findViewById(R.id.progressBar);
        headerNoInternetConnection = view.findViewById(R.id.headerNoInternetConnection);
        headerFilter = view.findViewById(R.id.headerFilter);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.postItems);
    }

    private void setTypefaces()
    {
        txtFilterText.setTypeface(DivarUtils.faceLight);
        txtNoAdvertise.setTypeface(DivarUtils.face);
    }

    private void setupFragment()
    {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostsAdapter = new PostsAdapterType2(getContext(),postsList,PostsAdapterType2.MY_ADVERTISE);
        mRecyclerView.setAdapter(mPostsAdapter);
        getMyAdvertise();
    }

    private void setupListeners()
    {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getMyAdvertise();
            }
        });

        headerNoInternetConnection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getMyAdvertise();
            }
        });
    }

    private void getMyAdvertise()
    {
        final String url = Constant.BASE_URL + "userAds.php";

        headerFilter.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        headerNoInternetConnection.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        txtNoAdvertise.setVisibility(View.GONE);

        JSONObject jsonData = new JSONObject();
        try
        {
            String phone = DivarUtils.readDataFromStorage(Constant.USER_PHONE, null);
            jsonData.put(Constant.USER_PHONE,phone);
        } catch (JSONException e)
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
                    txtNoAdvertise.setVisibility(View.VISIBLE);
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
