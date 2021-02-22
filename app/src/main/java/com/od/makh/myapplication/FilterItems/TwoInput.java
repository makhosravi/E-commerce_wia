package com.od.makh.myapplication.FilterItems;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.od.makh.myapplication.R;

import wiadevelopers.com.library.EasyMoney.EasyMoneyEditText;

public class TwoInput extends FilterRootView
{
    private String strFrom;
    private String strTo;
    private EasyMoneyEditText edtFrom;
    private EasyMoneyEditText edtTo;

    public TwoInput(String title, String key, String strFrom, String strTo)
    {
        super(title, key, strFrom + " - " + strTo, FilterRootView.TWO_INPUT);
        this.strFrom = strFrom;
        this.strTo = strTo;

        super.data = super.defData;

        initialize();
    }

    private void initialize()
    {
        findViews();
        setupListeners();
        setData();
    }

    private void findViews()
    {
        edtFrom = (EasyMoneyEditText) super.view.findViewById(R.id.edtFrom);
        edtTo = (EasyMoneyEditText) super.view.findViewById(R.id.edtTo);
    }

    private void setupListeners()
    {
        imgClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TwoInput.super.data = defData;
                edtFrom.setText(strFrom);
                edtTo.setText(strTo);
                TwoInput.super.checkState();
            }
        });

        edtFrom.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                String from = edtFrom.getValueString().toString();
                String to = edtTo.getValueString().toString();
                TwoInput.super.data = from + " - " + to;
                TwoInput.super.checkState();
            }
        });

        edtTo.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                String from = edtFrom.getValueString().toString();
                String to = edtTo.getValueString().toString();
                TwoInput.super.data = from + " - " + to;
                TwoInput.super.checkState();
            }
        });
    }

    private void setData()
    {
        edtFrom.setText(strFrom);
        edtTo.setText(strTo);
    }
}
