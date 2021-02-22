package com.od.makh.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.od.makh.myapplication.Adapter.PostsAdapter;
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

public class SearchFragment extends Fragment
{
    EditText edtInput;
    ImageView imgMic, imgSearch, imgClear;

    private RecyclerView mRecyclerView;
    private MaterialProgressBar progressBar;
    private TextView txtNoResult;

    private PostsAdapter adapter;
    private ArrayList<Post> postItems = new ArrayList<>();

    public SearchFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        initialize(view);
        return view;
    }

    private void initialize(View view)
    {
        findViews(view);
        setupFragment();
        checkEditTextState();
        setTypefaces();
        setListeners();
    }

    private void findViews(View view)
    {
        edtInput = (EditText) view.findViewById(R.id.edtInput);
        imgMic = (ImageView) view.findViewById(R.id.imgMic);
        imgSearch = (ImageView) view.findViewById(R.id.imgSearch);
        imgClear = (ImageView) view.findViewById(R.id.imgClear);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.postItems);
        progressBar = (MaterialProgressBar) view.findViewById(R.id.progressBar);
        txtNoResult = (TextView) view.findViewById(R.id.txtNoResult);
    }

    private void setupFragment()
    {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsAdapter(getContext(), postItems, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
    }

    private void checkEditTextState()
    {
        if (edtInput.getText().toString().equals(""))
        {
            imgSearch.setVisibility(View.GONE);
            imgClear.setVisibility(View.GONE);
        }
        else
            {
                imgSearch.setVisibility(View.VISIBLE);
                imgClear.setVisibility(View.VISIBLE);
            }
    }

    private void setTypefaces()
    {
        edtInput.setTypeface(DivarUtils.face);
    }

    private void setListeners()
    {
        imgClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                edtInput.setText("");
            }
        });

        imgMic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Toast.makeText(getContext(), "Microphne Button is Clicked", Toast.LENGTH_SHORT).show();
                DivarUtils.promptSpeechInput(getActivity(),"fa-IR","دنبال چه هستید؟");
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                search();
            }
        });

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkEditTextState();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DivarUtils.SPEECH_INPUT_REQUEST_CODE && resultCode == Constant.RESULT_OK && data != null)
        {
            edtInput.setText(DivarUtils.getSpeechData(data));
            search();
        }
    }

    private void search()
    {
//        Toast.makeText(getContext(), edtInput.getText().toString() + " Searched", Toast.LENGTH_SHORT).show();
        postItems.clear();
        final String url = Constant.BASE_URL + "search.php";

        mRecyclerView.setVisibility(View.GONE);
        txtNoResult.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        JSONObject jsonData = new JSONObject();
        try
        {
            jsonData.put("searchText", edtInput.getText().toString());
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
                try
                {
                    postItems.addAll(response);
                    adapter.notifyDataSetChanged();
                    txtNoResult.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                txtNoResult.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
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
