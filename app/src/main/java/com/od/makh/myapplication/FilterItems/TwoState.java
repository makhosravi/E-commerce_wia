package com.od.makh.myapplication.FilterItems;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.od.makh.myapplication.R;

import wiadevelopers.com.library.DivarUtils;

public class TwoState extends FilterRootView
{
    private String leftValue;
    private String rightValue;

    private Button btnLeft;
    private Button btnRight;

    public static final int SELECTED_COLOR = Color.parseColor("#CCCCCC");
    public static final int UN_SELECTED_COLOR = Color.parseColor("#EEEEEE");

    public TwoState(String title, String key, String leftValue, String rightValue)
    {
        super(title, key, leftValue, FilterRootView.TWO_STATE);
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        super.data = leftValue;

        initialize();
    }

    private void initialize()
    {
        findViews();
        setTypefaces();
        setData();
        setupListeners();
    }

    private void findViews()
    {
        btnLeft = (Button) super.view.findViewById(R.id.btnLeft);
        btnRight = (Button) super.view.findViewById(R.id.btnRight);
    }

    private void setTypefaces()
    {
        btnLeft.setTypeface(DivarUtils.face);
        btnRight.setTypeface(DivarUtils.face);
    }

    private void setData()
    {
        btnLeft.setText(leftValue);
        btnRight.setText(rightValue);
        TwoState.super.checkState();
    }

    private void setupListeners()
    {

        super.imgClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TwoState.super.data = TwoState.super.defData;
                btnLeft.setBackgroundColor(SELECTED_COLOR);
                btnRight.setBackgroundColor(UN_SELECTED_COLOR);
                TwoState.super.checkState();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TwoState.super.data = leftValue;
                btnLeft.setBackgroundColor(SELECTED_COLOR);
                btnRight.setBackgroundColor(UN_SELECTED_COLOR);
                TwoState.super.checkState();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TwoState.super.data = rightValue;
                btnLeft.setBackgroundColor(UN_SELECTED_COLOR);
                btnRight.setBackgroundColor(SELECTED_COLOR);
                TwoState.super.checkState();
            }
        });
    }
}
