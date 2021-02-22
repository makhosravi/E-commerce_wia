package com.od.makh.myapplication.FilterItems;

import android.graphics.Color;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.od.makh.myapplication.R;

import wiadevelopers.com.library.DivarUtils;

public class Optional extends FilterRootView
{
    private String text1;
    private String text2;
    private String text3;

    private RadioButton rdbtn1;
    private RadioButton rdbtn2;
    private RadioButton rdbtn3;

    public static final int SELECTED_COLOR = Color.parseColor("#CCCCCC");
    public static final int UN_SELECTED_COLOR = Color.parseColor("#EEEEEE");

    public Optional(String title, String key, String text1, String text2, String text3)
    {
        super(title, key, text1, FilterRootView.OPTIONAL);
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;

        super.data = super.defData;

        initialize();
    }

    private void initialize()
    {
        findViews();
        setTypefaces();
        setupListeners();
        setData();
    }

    private void findViews()
    {
        rdbtn1 = (RadioButton) super.view.findViewById(R.id.rdBtn1);
        rdbtn2 = (RadioButton) super.view.findViewById(R.id.rdBtn2);
        rdbtn3 = (RadioButton) super.view.findViewById(R.id.rdBtn3);
    }

    private void setTypefaces()
    {
        rdbtn1.setTypeface(DivarUtils.face);
        rdbtn2.setTypeface(DivarUtils.face);
        rdbtn3.setTypeface(DivarUtils.face);
    }

    private void setupListeners()
    {
        super.imgClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                rdbtn1.setChecked(true);
            }
        });

        rdbtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (isChecked)
                {
                    Optional.super.data = text1;
                    rdbtn1.setBackgroundColor(SELECTED_COLOR);
                    rdbtn2.setBackgroundColor(UN_SELECTED_COLOR);
                    rdbtn3.setBackgroundColor(UN_SELECTED_COLOR);
                    Optional.super.checkState();
                }
            }
        });

        rdbtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (isChecked)
                {
                    Optional.super.data = text2;
                    rdbtn1.setBackgroundColor(UN_SELECTED_COLOR);
                    rdbtn2.setBackgroundColor(SELECTED_COLOR);
                    rdbtn3.setBackgroundColor(UN_SELECTED_COLOR);
                    Optional.super.checkState();
                }
            }
        });

        rdbtn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (isChecked)
                {
                    Optional.super.data = text3;
                    rdbtn1.setBackgroundColor(UN_SELECTED_COLOR);
                    rdbtn2.setBackgroundColor(UN_SELECTED_COLOR);
                    rdbtn3.setBackgroundColor(SELECTED_COLOR);
                    Optional.super.checkState();
                }
            }
        });
    }

    private void setData()
    {
        rdbtn1.setText(text1);
        rdbtn2.setText(text2);
        rdbtn3.setText(text3);

        rdbtn1.setChecked(true);
//        Optional.super.checkState();
    }
}
