package com.od.makh.myapplication.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.od.makh.myapplication.Activities.Dialog.FilterActivity;
import com.od.makh.myapplication.Adapter.CategoriesAdapter;
import com.od.makh.myapplication.Adapter.PostsAdapter;
import com.od.makh.myapplication.Application.G;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.Model.Category;
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
public class ListFragmentSecond extends Fragment
{

    private ListView lstCategories;
    private TextView txtSubCategories;
    private TextView txtAdvertisement;
    private TextView txtAlert, txtText;
    private RelativeLayout rltvContainer;
    private NestedScrollView nestedScrollView;
    private View headerFilter, headerNoInternetConnection;

    private RecyclerView rcyclrPostItems;
    private MaterialProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    private PostsAdapter adapter;
    private ArrayList<Post> postItems = new ArrayList<>();

    private int page = 1;
    private int loadingItemIndex = 0;

    private ArrayList<Category> tmpCategories = new ArrayList<>();

    String id = null;
    private boolean isLoading = false;

    private JSONObject jsonFilter = null;

//    TextView txtId;

    public ListFragmentSecond()
    {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list_second,container,false);
        /*txtId = (TextView) view.findViewById(R.id.txtID);
        String id = getArguments().getString("id");
        txtId.setText(id);*/
        initialize(view);
        return view;
    }

    private void initialize(View view)
    {
        findViews(view);
        setTypefaces();
        setupFragment();
        setListeners();
    }

    private void findViews(View view)
    {
        txtSubCategories = view.findViewById(R.id.txtSubCategories);
        txtAdvertisement = view.findViewById(R.id.txtAdvertisement);
        txtText = view.findViewById(R.id.txtText);
        txtAlert = view.findViewById(R.id.txtAlert);
        lstCategories = view.findViewById(R.id.lstCategories);
        rcyclrPostItems = view.findViewById(R.id.rcyclrPostItems);
        headerFilter = view.findViewById(R.id.headerFilter);
        headerNoInternetConnection = view.findViewById(R.id.headerNoInternetConnection);
        rltvContainer = view.findViewById(R.id.rltvContainer);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
    }

    private void setTypefaces()
    {
        txtAlert.setTypeface(DivarUtils.faceLight);
        txtText.setTypeface(DivarUtils.faceLight);
        txtAdvertisement.setTypeface(DivarUtils.faceLight);
        txtSubCategories.setTypeface(DivarUtils.faceLight);
    }

    private void setupFragment()
    {
        id = getArguments().getString("id");
        Toast.makeText(getContext(), id,Toast.LENGTH_SHORT).show();

        assignCategoryArrayList(id);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getContext(), tmpCategories);
        lstCategories.setAdapter(categoriesAdapter);

        if (tmpCategories.size()!=0)
        {
            int gap = (int) ((getResources().getDimension(R.dimen.itemCategoryContainerMarginTop)));
            int height = (int) ((getResources().getDimension(R.dimen.itemCategoryContainerHeight)));

            int itemGap = gap * tmpCategories.size();
            int itemHeight = height * tmpCategories.size();

            lstCategories.getLayoutParams().height = itemHeight + itemGap;
        }
        else
        {
            txtSubCategories.setVisibility(View.GONE);
            lstCategories.setVisibility(View.GONE);
        }

        txtAdvertisement.setText("آگهی های " + G.getTitle(id));
        txtSubCategories.setText("زیر دسته های " + G.getTitle(id));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext())
        {
            @Override
            public boolean canScrollHorizontally()
            {
                return false;
            }

            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        rcyclrPostItems.setLayoutManager(linearLayoutManager);
        rcyclrPostItems.setNestedScrollingEnabled(false);
        adapter = new PostsAdapter(getContext(),postItems,rcyclrPostItems);
        rcyclrPostItems.setAdapter(adapter);

        getPosts();

        headerNoInternetConnection.setVisibility(View.GONE);
        headerFilter.setVisibility(View.VISIBLE);
    }

    private void setListeners()
    {
        lstCategories.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String _id = tmpCategories.get(position).getId();
//                Toast.makeText(getContext(),_id,Toast.LENGTH_SHORT).show();
                ListFragmentSecond fragmentSecond = new ListFragmentSecond();
                Bundle bundle = new Bundle();
                bundle.putString("id", _id);
                fragmentSecond.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.listRootContainer, fragmentSecond);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
        {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                int diff = DivarUtils.getScrollViewUpperBound(v);
                Log.e(G.TAG,"oldScrollY  = " + oldScrollY);
                Log.e(G.TAG,"scrollY     = " + scrollY);
                Log.e(G.TAG,"diff        = " + diff);

                int height = (int) ((getResources().getDimension(R.dimen.itemPostSize)));
                height *= 3;

                if (scrollY >= diff - height && isLoading == false)      // scrollY == diff && isLoading == false
                {
                    Toast.makeText(getContext(),"Start",Toast.LENGTH_SHORT).show();
                    page++;
                    getPosts();
                    postItems.add(null);
                    loadingItemIndex = postItems.size() - 1;
                    adapter.notifyItemInserted(loadingItemIndex);
                    isLoading = true;
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                rcyclrPostItems.setVisibility(View.GONE);
//                txtNoData.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                headerNoInternetConnection.setVisibility(View.GONE);
                headerFilter.setVisibility(View.VISIBLE);
                page = 1;
                postItems.clear();
                adapter.notifyDataSetChanged();
                getPosts();
            }
        });

        rltvContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                startActivity(new Intent(getActivity(), FilterActivity.class));
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.putExtra(Constant.KEY_SELECTED_ID, id);
                startActivityForResult(intent, Constant.REQUEST_FILTER_LIST_FRAGMENT);
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
    }

    void assignCategoryArrayList(String subId)
    {
        subId = subId.trim();

        tmpCategories.clear();
        for (int i = 0; i < G.CATEGORIES.size(); i++)
        {
            int idLength = G.CATEGORIES.get(i).getId().length();
            if (idLength == subId.length() + 1)
            {
                if (G.CATEGORIES.get(i).getId().startsWith(subId))
                    tmpCategories.add(G.CATEGORIES.get(i));
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == Constant.REQUEST_FILTER_LIST_FRAGMENT && resultCode == Constant.RESULT_OK && data != null)
        {
//            Toast.makeText(getContext(),"Reach",Toast.LENGTH_SHORT).show();
            String result = data.getStringExtra(Constant.KEY_FILTER_DATA_JSON);
            try {
                jsonFilter = new JSONObject(result);
                rcyclrPostItems.setVisibility(View.GONE);
//                txtNoData.setVisibility(View.GONE);
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
            jsonData.put(Constant.CATEGORY_ID, id);
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
                    isLoading = false;

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
//                        txtNoData.setVisibility(View.VISIBLE);
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
}
