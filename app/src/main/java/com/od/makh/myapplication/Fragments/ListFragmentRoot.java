package com.od.makh.myapplication.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.od.makh.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragmentRoot extends Fragment
{

    private ListFragmentCategories listFragmentCategories = new ListFragmentCategories();

    public ListFragmentRoot()
    {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_root,container,false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.listRootContainer, listFragmentCategories);
        transaction.commit();
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        listFragmentCategories.onActivityResult(requestCode,resultCode,data);
    }
}
