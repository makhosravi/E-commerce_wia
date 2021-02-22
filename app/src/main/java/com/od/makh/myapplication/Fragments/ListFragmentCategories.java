package com.od.makh.myapplication.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.R;

import wiadevelopers.com.library.DivarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragmentCategories extends Fragment {

    private LinearLayout lnrBuilding, lnrVehicles, lnrElectronics, lnrHomeApplications, lnrServices, lnrPersonal,
                lnrEntertainment, lnrSocial, lnrBusiness, lnrHire;

    private TextView txtBulding, txtVehicles, txtElectronics, txtHomeApplication, txtServices, txtPersonal,
            txtEntertainment,txtSocial, txtBusiness, txtHire;

    private ListFragmentSecond fragmentSecond = new ListFragmentSecond();


    public ListFragmentCategories()
    {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list_categories,container,false);
        initialize(view);
        return view;
    }

    private void initialize(View view)
    {
        findViews(view);
        setTypefaces();
        setLinteners();
    }

    private void findViews(View view)
    {
        lnrBuilding = (LinearLayout) view.findViewById(R.id.categoriesLnrBuilding);
        lnrVehicles = (LinearLayout) view.findViewById(R.id.categoriesLnrVehicles);
        lnrElectronics = (LinearLayout) view.findViewById(R.id.categoriesLnrElectronics);
        lnrHomeApplications = (LinearLayout) view.findViewById(R.id.categoriesLnrHomeApplications);
        lnrServices = (LinearLayout) view.findViewById(R.id.categoriesLnrServices);
        lnrPersonal = (LinearLayout) view.findViewById(R.id.categoriesLnrPersonal);
        lnrEntertainment = (LinearLayout) view.findViewById(R.id.categoriesLnrEntertainment);
        lnrSocial = (LinearLayout) view.findViewById(R.id.categoriesLnrSocial);
        lnrBusiness = (LinearLayout) view.findViewById(R.id.categoriesLnrBusiness);
        lnrHire = (LinearLayout) view.findViewById(R.id.categoriesLnrHire);

        txtBulding = (TextView) view.findViewById(R.id.categoriesTxtBuilding);
        txtVehicles = (TextView) view.findViewById(R.id.categoriesTxtVehicles);
        txtElectronics = (TextView) view.findViewById(R.id.categoriesTxtElectronics);
        txtHomeApplication = (TextView) view.findViewById(R.id.categoriesTxtHomeApplications);
        txtServices = (TextView) view.findViewById(R.id.categoriesTxtServices);
        txtPersonal = (TextView) view.findViewById(R.id.categoriesTxtPersonal);
        txtEntertainment = (TextView) view.findViewById(R.id.categoriesTxtEntertainment);
        txtSocial = (TextView) view.findViewById(R.id.categoriesTxtSocial);
        txtBusiness = (TextView) view.findViewById(R.id.categoriesTxtBusiness);
        txtHire = (TextView) view.findViewById(R.id.categoriesTxtHire);
    }

    private void setTypefaces()
    {
        Typeface typeface = DivarUtils.face;
        txtBulding.setTypeface(typeface);
        txtVehicles.setTypeface(typeface);
        txtElectronics.setTypeface(typeface);
        txtHomeApplication.setTypeface(typeface);
        txtServices.setTypeface(typeface);
        txtPersonal.setTypeface(typeface);
        txtEntertainment.setTypeface(typeface);
        txtSocial.setTypeface(typeface);
        txtBusiness.setTypeface(typeface);
        txtHire.setTypeface(typeface);
    }

    private void setLinteners()
    {
        lnrSetOnClickListener(lnrBuilding, Constant.ID_BULDING);
        lnrSetOnClickListener(lnrVehicles, Constant.ID_VEHICLES);
        lnrSetOnClickListener(lnrElectronics, Constant.ID_ELECTRONICS);
        lnrSetOnClickListener(lnrHomeApplications, Constant.ID_HOMEAPPLICATION);
        lnrSetOnClickListener(lnrServices, Constant.ID_SERVICES);
        lnrSetOnClickListener(lnrPersonal, Constant.ID_PERSONAL);
        lnrSetOnClickListener(lnrEntertainment, Constant.ID_ENTERTAINMENT);
        lnrSetOnClickListener(lnrSocial, Constant.ID_SOCIAL);
        lnrSetOnClickListener(lnrBusiness, Constant.ID_BUSINESS);
        lnrSetOnClickListener(lnrHire, Constant.ID_HIRE);
    }

    private void lnrSetOnClickListener(LinearLayout lnr, final int id)
    {
        lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id + "");
                fragmentSecond.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.listRootContainer, fragmentSecond);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        fragmentSecond.onActivityResult(requestCode,resultCode,data);
    }
}
