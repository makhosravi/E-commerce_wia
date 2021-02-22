package com.od.makh.myapplication.CreatePostItems;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.od.makh.myapplication.R;

import java.util.HashMap;

import wiadevelopers.com.library.DivarUtils;

public class Optional extends CreatePostRootView
{
    private String radioButtonTitle1;
    private String radioButtonTitle2;
    private String radioButtonTitle3;

    private RadioButton rdBtn1;
    private RadioButton rdBtn2;
    private RadioButton rdBtn3;

    private TextView txtTitle;

    public Optional(String title, String key, String title1, String title2)
    {
        this(title, key, title1, title2, null);
    }


    public Optional(String title, String key, String title1, String title2, String title3) {
        super(title, key, CreatePostRootView.OPTIONAL);
        this.radioButtonTitle1 = title1;
        this.radioButtonTitle2 = title2;
        this.radioButtonTitle3 = title3;

        if (this.radioButtonTitle3 == null)
        {
            initialize(2);
        }
        else
        {
            initialize(3);
        }

    }

    private void initialize(int numberOfOption)
    {
        findViews();
        setTypefaces();
        setupViews(numberOfOption);
    }

    private void findViews()
    {
        txtTitle = (TextView) super.view.findViewById(R.id.txtTitle);

        rdBtn1 = (RadioButton) super.view.findViewById(R.id.rdBtn1);
        rdBtn2 = (RadioButton) super.view.findViewById(R.id.rdBtn2);
        rdBtn3 = (RadioButton) super.view.findViewById(R.id.rdBtn3);
    }

    private void setTypefaces()
    {
        txtTitle.setTypeface(DivarUtils.faceLight);
        rdBtn1.setTypeface(DivarUtils.face);
        rdBtn2.setTypeface(DivarUtils.face);
        rdBtn3.setTypeface(DivarUtils.face);
    }

    private void setupViews(int numberOfOption)
    {
        if (numberOfOption == 2)
        {
            rdBtn3.setVisibility(View.GONE);
        }

        txtTitle.setText(super.title);
        rdBtn1.setText(radioButtonTitle1);
        rdBtn2.setText(radioButtonTitle2);
        rdBtn3.setText(radioButtonTitle3);
    }

    public HashMap getData()
    {
        HashMap hashMap = new HashMap();

        String value = "";
        if (rdBtn1.isChecked())
        {
            value = rdBtn1.getText().toString().trim();
        }
        else if (rdBtn2.isChecked())
        {
            value = rdBtn2.getText().toString().trim();
        }
        else if (rdBtn3.isChecked())
        {
            value = rdBtn3.getText().toString().trim();
        }

        hashMap.put(super.key, value);

        return hashMap;
    }

    public void restoreData(String data)
    {
        if (data.equals(radioButtonTitle1))
        {
            rdBtn1.setChecked(true);
        }
        else if (data.equals(radioButtonTitle2))
        {
            rdBtn2.setChecked(true);
        }
        else if (data.equals(radioButtonTitle3))
        {
            rdBtn3.setChecked(true);
        }
    }
}
