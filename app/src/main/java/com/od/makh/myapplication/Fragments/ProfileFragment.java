package com.od.makh.myapplication.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.od.makh.myapplication.R;

import wiadevelopers.com.library.DivarUtils;

public class ProfileFragment extends Fragment
{

    TextView txtConversation, txtPost, txtBookmark, txtRecent, txtLocation, txtSupport, txtTerms, txtAbout;
    ImageView imgConversation, imgPost, imgBookmark, imgRecent, imgLocation, imgSupport, imgTerms, imgAbout;
    LinearLayout lnrConversation, lnrPost, lnrBookmark, lnrRecent, lnrLocation, lnrSupport, lnrTerms, lnrAbout;

    private MyBookMarksFragment myBookMarksFragment = new MyBookMarksFragment();
    private LastSeenRecentlyFragment lastSeenRecentlyFragment = new LastSeenRecentlyFragment();
    private MyAdvertiseFragment myAdvertiseFragment = new MyAdvertiseFragment();

    public ProfileFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initialize(view);
        return view;
    }

    private void initialize(View view)
    {
        findViews(view);
        setTypefaces();
        setListeners();
    }

    private void findViews(View view)
    {
        txtConversation = (TextView) view.findViewById(R.id.profileTxtConversation);
        txtPost = (TextView) view.findViewById(R.id.profileTxtPost);
        txtBookmark = (TextView) view.findViewById(R.id.profileTxtBookmark);
        txtRecent = (TextView) view.findViewById(R.id.profileTxtRecent);
        txtLocation = (TextView) view.findViewById(R.id.profileTxtLocation);
        txtSupport = (TextView) view.findViewById(R.id.profileTxtSupport);
        txtTerms = (TextView) view.findViewById(R.id.profileTxtTerms);
        txtAbout = (TextView) view.findViewById(R.id.profileTxtAbout);

        imgConversation = (ImageView) view.findViewById(R.id.profileImgConversation);
        imgPost = (ImageView) view.findViewById(R.id.profileImgPost);
        imgBookmark = (ImageView) view.findViewById(R.id.profileImgBookmark);
        imgRecent = (ImageView) view.findViewById(R.id.profileImgRecent);
        imgLocation = (ImageView) view.findViewById(R.id.profileImgLocation);
        imgSupport = (ImageView) view.findViewById(R.id.profileImgSupport);
        imgTerms = (ImageView) view.findViewById(R.id.profileImgTerms);
        imgAbout = (ImageView) view.findViewById(R.id.profileImgAbout);

        lnrConversation = (LinearLayout) view.findViewById(R.id.lnrConversations);
        lnrPost = (LinearLayout) view.findViewById(R.id.lnrPost);
        lnrBookmark = (LinearLayout) view.findViewById(R.id.lnrBookmark);
        lnrRecent = (LinearLayout) view.findViewById(R.id.lnrRecent);
        lnrLocation = (LinearLayout) view.findViewById(R.id.lnrLocation);
        lnrSupport = (LinearLayout) view.findViewById(R.id.lnrSupport);
        lnrTerms = (LinearLayout) view.findViewById(R.id.lnrTerms);
        lnrAbout = (LinearLayout) view.findViewById(R.id.lnrAbout);
    }

    private void setTypefaces()
    {
        Typeface typeface = DivarUtils.face;
        txtConversation.setTypeface(typeface);
        txtPost.setTypeface(typeface);
        txtBookmark.setTypeface(typeface);
        txtRecent.setTypeface(typeface);
        txtLocation.setTypeface(typeface);
        txtSupport.setTypeface(typeface);
        txtTerms.setTypeface(typeface);
        txtAbout.setTypeface(typeface);
    }

    private void setListeners()
    {
        lnrConversation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        lnrBookmark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                replaceFragment(myBookMarksFragment);
            }
        });

        lnrRecent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                replaceFragment(lastSeenRecentlyFragment);
            }
        });

        lnrPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                replaceFragment(myAdvertiseFragment);
            }
        });

        lnrAbout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.profileRootContainer, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }
}
