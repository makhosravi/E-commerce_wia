package com.od.makh.myapplication.FilterItems;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.od.makh.myapplication.Adapter.SpinnerAdapter;
import com.od.makh.myapplication.R;

import java.util.ArrayList;

public class ContainItems extends FilterRootView
{
    ArrayList<String> spinnerItems;
    private Spinner spinner;

    public ContainItems(String title, String key, ArrayList<String> spinnerItems)
    {
        super(title, key, spinnerItems.get(0).toString(), FilterRootView.CONTAIN_ITEMS);
        super.data = super.defData;
        this.spinnerItems = spinnerItems;

        initialize();
    }

    private void initialize()
    {
        findViews();
        setData();
        setupListeners();
    }

    private void findViews()
    {
        spinner = (Spinner) super.view.findViewById(R.id.filterContainItemsSpnrItems);
    }

    private void setData()
    {
        super.txtData.setText(data);
        SpinnerAdapter customAdapter = new SpinnerAdapter(spinnerItems);
        spinner.setAdapter(customAdapter);
    }

    private void setupListeners()
    {

        imgClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final int itemPos = 0;
                spinner.setSelection(itemPos);
                ContainItems.super.data = spinnerItems.get(itemPos);
                ContainItems.super.checkState();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                ContainItems.super.data = spinner.getSelectedItem().toString();
                ContainItems.super.checkState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }
}
