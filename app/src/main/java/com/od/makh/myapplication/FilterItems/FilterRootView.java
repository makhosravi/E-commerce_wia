package com.od.makh.myapplication.FilterItems;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.od.makh.myapplication.R;

import java.util.HashMap;

import wiadevelopers.com.library.DivarUtils;
import wiadevelopers.com.library.ExpandableLayout.ExpandableLayout;

public class FilterRootView
{

    public interface expandCollapseListener
    {
        void expandCollapse(FilterRootView filterRootView);
    }

    protected static final int TWO_STATE = 1;
    protected static final int OPTIONAL = 2;
    protected static final int CONTAIN_ITEMS = 3;
    protected static final int TWO_INPUT = 4;

    protected LinearLayout lnrParent;
    protected TextView txtTitle;
    protected TextView txtData;
    protected ImageView imgTick;
    protected ImageView imgClear;
    protected ImageView imgArrow;
    protected ExpandableLayout expandableLayout;

    protected String title;
    protected String key;
    protected String defData;
    protected int type;
    protected View view;

    protected  String data;

    private static expandCollapseListener expandCollapseListener;

    public FilterRootView(String title, String key, String defData, int type)
    {
        this.title = title;
        this.key = key;
        this.defData = defData;
        this.type = type;

        if (type == TWO_STATE)
        {
            this.view = DivarUtils.mLayoutInflater.inflate(R.layout.filter_two_state, null);
        }
        else if (type == OPTIONAL)
        {
            this.view = DivarUtils.mLayoutInflater.inflate(R.layout.filter_optional, null);
        }
        else if (type == CONTAIN_ITEMS)
        {
            this.view = DivarUtils.mLayoutInflater.inflate(R.layout.filter_contain_items, null);
        }
        else if (type == TWO_INPUT)
        {
            this.view = DivarUtils.mLayoutInflater.inflate(R.layout.filter_two_input, null);
        }

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
        lnrParent = (LinearLayout) view.findViewById(R.id.lnrParent);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtData = (TextView) view.findViewById(R.id.txtData);
        imgArrow = (ImageView) view.findViewById(R.id.imgArrow);
        imgClear = (ImageView) view.findViewById(R.id.imgClear);
        imgTick = (ImageView) view.findViewById(R.id.imgTick);

        expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandLayout);
    }

    private void setupListeners()
    {
        lnrParent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*if (expandableLayout.isExpanded())
                {
                    expandableLayout.collapse();
                }
                else
                {
                    expandableLayout.expand();
                }*/
                if (expandCollapseListener != null)
                {
                    expandCollapseListener.expandCollapse(FilterRootView.this);
                }
            }
        });
    }

    private void setTypefaces()
    {
        txtData.setTypeface(DivarUtils.face);
        txtTitle.setTypeface(DivarUtils.face);
    }

    private void setData()
    {
        txtTitle.setText(title);
    }

    protected void checkState()
    {
        if (defData.equals(data))
        {
            imgTick.setVisibility(View.INVISIBLE);
            imgClear.setVisibility(View.INVISIBLE);
        }
        else
        {
            imgTick.setVisibility(View.VISIBLE);
            imgClear.setVisibility(View.VISIBLE);
        }

        txtData.setText(data);
    }

    public static void setExpandCollapseListener(FilterRootView.expandCollapseListener expandCollapseListener)
    {
        FilterRootView.expandCollapseListener = null;
        FilterRootView.expandCollapseListener = expandCollapseListener;
    }

    public boolean isExpand()
    {
        return expandableLayout.isExpanded();
    }

    public void expand()
    {
        txtData.startAnimation(DivarUtils.slideOutBottom(200));
        imgArrow.setRotation(180);
        expandableLayout.expand();
    }

    public void collapse()
    {
        txtData.startAnimation(DivarUtils.slideInBottom(200));
        imgArrow.setRotation(180);
        expandableLayout.collapse();
    }

    public View getView()
    {
        return view;
    }

    public HashMap getData()
    {
        HashMap hashMap = new HashMap();
        hashMap.put(this.key, this.data);
        return hashMap;
    }
}
