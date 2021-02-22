package com.od.makh.myapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.od.makh.myapplication.R;

public class ProfileFragmentRoot extends Fragment
{

    public ProfileFragmentRoot()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_root,container,false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.profileRootContainer, new ProfileFragment());
        transaction.commit();
        return view;
    }
}
