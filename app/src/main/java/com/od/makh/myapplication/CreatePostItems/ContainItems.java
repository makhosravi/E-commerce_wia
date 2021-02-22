package com.od.makh.myapplication.CreatePostItems;

import android.widget.Spinner;
import android.widget.TextView;

import com.od.makh.myapplication.Adapter.SpinnerAdapter;
import com.od.makh.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

import wiadevelopers.com.library.DivarUtils;

public class ContainItems extends CreatePostRootView
{

    private TextView txtTitle;
    private Spinner spnrItems;

    ArrayList<String> spinnerItems = new ArrayList<>();

    public ContainItems(String title, String key, ArrayList<String> spinnerItems)
    {
        super(title, key, CreatePostRootView.CONTAIN_ITEMS);
        this.spinnerItems = spinnerItems;
        initialize();
    }

    private void initialize()
    {
        findViews();
        setTypefaces();
        setupViews();
    }

    private void findViews()
    {
        txtTitle = (TextView) super.view.findViewById(R.id.txtTitle);
        spnrItems = (Spinner) super.view.findViewById(R.id.spnrItems);
    }

    private void setTypefaces()
    {
        txtTitle.setTypeface(DivarUtils.faceLight);
    }

    private void setupViews()
    {
        txtTitle.setText(this.title);
        SpinnerAdapter customAdapter = new SpinnerAdapter(spinnerItems);
        spnrItems.setAdapter(customAdapter);
    }

    public HashMap getData()
    {
        HashMap hashMap = new HashMap();
        if (spnrItems.getSelectedItemPosition() == 0)
        {
            hashMap.put(super.key, "");
        }
        else
        {
            hashMap.put(super.key, spnrItems.getSelectedItem().toString().trim());
        }
        return hashMap;
    }

    public void restoreData(String data)
    {
        for (int i = 0; i < spinnerItems.size(); i++)
        {
            if (data.equals(spinnerItems.get(i)))
            {
                spnrItems.setSelection(i);
                break;
            }
        }
    }
}
