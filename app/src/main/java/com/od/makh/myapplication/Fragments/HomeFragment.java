package com.od.makh.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.od.makh.myapplication.Activities.Dialog.FilterActivity;
import com.od.makh.myapplication.Adapter.OnLoadMoreListener;
import com.od.makh.myapplication.Adapter.PostsAdapter;
import com.od.makh.myapplication.Application.G;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.Model.Post;
import com.od.makh.myapplication.Network.DivarPostRequest;
import com.od.makh.myapplication.R;
import com.od.makh.myapplication.Utils.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import wiadevelopers.com.library.DivarUtils;
import wiadevelopers.com.library.MaterialLoading.MaterialProgressBar;

public class HomeFragment extends Fragment
{

    private RelativeLayout rltvContainer;
//    private TextView txtResult;
    private TextView txtNoData, txtAlert, txtText;
    private RecyclerView rcyclrPostItems;
    private MaterialProgressBar progressBar;

    private PostsAdapter adapter;
    private ArrayList<Post> postItems = new ArrayList<>();

    private int page = 1;
    private int loadingItemIndex = 0;

    private View headerFilter, headerNoInternetConnection;

    private SwipeRefreshLayout swipeRefresh;

    private JSONObject jsonFilter = null;

    public HomeFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initialize(view);
        return view;
    }

    private void initialize(View view)
    {
        findViews(view);
        setTypefaces();
        setupFragment();
        setupListeners();
        getPosts();
    }

    private void findViews(View view)
    {
        rltvContainer = (RelativeLayout) view.findViewById(R.id.rltvContainer);
//        txtResult = (TextView) view.findViewById(R.id.txtResult);
        txtNoData = (TextView) view.findViewById(R.id.txtNoData);
        txtAlert = (TextView) view.findViewById(R.id.txtAlert);
        txtText = (TextView) view.findViewById(R.id.txtText);
        rcyclrPostItems = (RecyclerView) view.findViewById(R.id.rcyclrPostItems);
        progressBar = (MaterialProgressBar) view.findViewById(R.id.progressBar);

        headerFilter = view.findViewById(R.id.headerFilter);
        headerNoInternetConnection = view.findViewById(R.id.headerNoInternetConnection);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
    }

    private void setTypefaces()
    {
//        txtResult.setTypeface(DivarUtils.face);
        txtNoData.setTypeface(DivarUtils.faceLight);
        txtAlert.setTypeface(DivarUtils.faceLight);
        txtText.setTypeface(DivarUtils.faceLight);
    }

    private void setupFragment()
    {
        rcyclrPostItems.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsAdapter(getContext(),postItems, rcyclrPostItems);
        rcyclrPostItems.setAdapter(adapter);

        rcyclrPostItems.setVisibility(View.GONE);
        txtNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        headerNoInternetConnection.setVisibility(View.GONE);
        headerFilter.setVisibility(View.VISIBLE);
    }

    private void setupListeners()
    {
        rltvContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                startActivity(new Intent(getActivity(), FilterActivity.class));
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.putExtra(Constant.KEY_SELECTED_ID, "0");
                startActivityForResult(intent, Constant.REQUEST_FILTER_HOME_FRAGMENT);
            }
        });

        headerNoInternetConnection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                headerNoInternetConnection.setVisibility(View.GONE);
                headerFilter.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                getPosts();
            }
        });

        adapter.setOnLoadMoreListener(new OnLoadMoreListener()
        {
            @Override
            public void onLoadMore()
            {
                page++;
                getPosts();
                postItems.add(null);
                loadingItemIndex = postItems.size() - 1;
                adapter.notifyItemInserted(loadingItemIndex);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                rcyclrPostItems.setVisibility(View.GONE);
                txtNoData.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                headerNoInternetConnection.setVisibility(View.GONE);
                headerFilter.setVisibility(View.VISIBLE);
                page = 1;
                postItems.clear();
                adapter.notifyDataSetChanged();
                getPosts();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == Constant.REQUEST_FILTER_HOME_FRAGMENT && resultCode == Constant.RESULT_OK && data != null)
        {
            String result = data.getStringExtra(Constant.KEY_FILTER_DATA_JSON);
            try {
                jsonFilter = new JSONObject(result);
                rcyclrPostItems.setVisibility(View.GONE);
                txtNoData.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                headerNoInternetConnection.setVisibility(View.GONE);
                headerFilter.setVisibility(View.VISIBLE);
                page = 1;
                postItems.clear();
                adapter.notifyDataSetChanged();
                getPosts();
                Log.i(G.TAG, jsonFilter.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getPosts()
    {
        final String url = Constant.BASE_URL + "filter.php";

        JSONObject jsonData = new JSONObject();
        try
        {
            if (jsonFilter != null)
                jsonData = jsonFilter;
            jsonData.put(Constant.CATEGORY_ID, Constant.ALL_POSTS);
            jsonData.put(Constant.PAGE, page);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<ArrayList<Post>> listener = new Response.Listener<ArrayList<Post>>()
        {
            @Override
            public void onResponse(ArrayList<Post> response)
            {

                if (page != 1)
                {
                    postItems.remove(loadingItemIndex);
                    adapter.notifyItemRemoved(postItems.size());
                }

                if (response != null)
                {
                    postItems.addAll(response);
                    adapter.notifyDataSetChanged();
                    rcyclrPostItems.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    adapter.setLoading(false);
                    swipeRefresh.setRefreshing(false);

                /*String result = "";
                for (int i = 0; i < posts.length; i++)
                {
                    result += posts[i].getId() + " ==> " + posts[i].getAdTitle() + "\n";
                }

                txtResult.setText(result);*/
                }
                else
                {
                    if (page == 1)
                    {
                        rcyclrPostItems.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.VISIBLE);
                    }
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
//                Toast.makeText(getContext(),"Error :(", Toast.LENGTH_SHORT).show();
                headerFilter.setVisibility(View.GONE);
                headerNoInternetConnection.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        };

        DivarPostRequest request = new DivarPostRequest(url,jsonData,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json; charset=utf-8");
                return hashMap;
            }
        };

        AppSingleton.getInstance(getContext()).addToRequestQueue(request);

    }

    private void getPosts1()
    {
        final String url = Constant.BASE_URL + "filter.php";

        JSONObject jsonData = new JSONObject();
        try
        {
            jsonData.put(Constant.CATEGORY_ID, Constant.ALL_POSTS);
            jsonData.put(Constant.PAGE, page);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {

                if (page != 1)
                {
                    postItems.remove(loadingItemIndex);
                    adapter.notifyItemRemoved(postItems.size());
                }

                String state = "";
                try
                {
                    state = response.getJSONObject(0).getString("state");
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if (!state.equalsIgnoreCase("2"))
                {
                    Gson gson = new Gson();
                    Post[] posts = gson.fromJson(response.toString(), Post[].class);

                    Collections.addAll(postItems, posts);
                    adapter.notifyDataSetChanged();
                    rcyclrPostItems.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    adapter.setLoading(false);
                    swipeRefresh.setRefreshing(false);

                /*String result = "";
                for (int i = 0; i < posts.length; i++)
                {
                    result += posts[i].getId() + " ==> " + posts[i].getAdTitle() + "\n";
                }

                txtResult.setText(result);*/
                }
                else
                {
                    if (page == 1)
                    {
                        rcyclrPostItems.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.VISIBLE);
                    }
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
//                Toast.makeText(getContext(),"Error :(", Toast.LENGTH_SHORT).show();
                headerFilter.setVisibility(View.GONE);
                headerNoInternetConnection.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,url,jsonData,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json; charset=utf-8");
                return hashMap;
            }
        };

        AppSingleton.getInstance(getContext()).addToRequestQueue(request);

    }
}
