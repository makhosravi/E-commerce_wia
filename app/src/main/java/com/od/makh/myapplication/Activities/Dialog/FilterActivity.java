package com.od.makh.myapplication.Activities.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.FilterItems.ContainItems;
import com.od.makh.myapplication.FilterItems.FilterRootView;
import com.od.makh.myapplication.FilterItems.Optional;
import com.od.makh.myapplication.FilterItems.TwoInput;
import com.od.makh.myapplication.FilterItems.TwoState;
import com.od.makh.myapplication.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import wiadevelopers.com.library.DivarUtils;

public class FilterActivity extends AppCompatActivity
{

    private TextView txtTitle;
    private TextView txtApplyFilter;
    private TextView txtClearFilter;

    private LinearLayout lnrInsertPoint;

    private ArrayList<FilterRootView> rootViews = new ArrayList<>();

    private String selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initialize();
    }

    private void initialize()
    {
        findViews();
        setTypefaces();
        setupListeners();
        selectedId = getIntent().getStringExtra(Constant.KEY_SELECTED_ID);
        setData(selectedId);
    }

    private void findViews()
    {
        FilterRootView.setExpandCollapseListener(new FilterRootView.expandCollapseListener()
        {
            @Override
            public void expandCollapse(FilterRootView filterRootView)
            {
                for (int i = 0; i < rootViews.size(); i++)
                {
                    FilterRootView rootView = rootViews.get(i);
                    if (filterRootView == rootView)
                    {
                        if (rootView.isExpand())
                        {
                            rootView.collapse();
                        }
                        else
                        {
                            rootView.expand();
                        }
                    }
                    else
                    {
                        if (rootView.isExpand())
                        {
                            rootView.collapse();
                        }
                    }
                }
            }
        });
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtApplyFilter = (TextView) findViewById(R.id.txtApply);
        txtClearFilter = (TextView) findViewById(R.id.txtClearFilter);

        lnrInsertPoint = (LinearLayout) findViewById(R.id.lnrContainer);
    }

    private void setTypefaces()
    {
        txtTitle.setTypeface(DivarUtils.faceLight);
        txtApplyFilter.setTypeface(DivarUtils.face);
        txtClearFilter.setTypeface(DivarUtils.faceLight);
    }

    private void setupListeners()
    {
        txtApplyFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HashMap hashMap = new HashMap();

                hashMap.put(Constant.CATEGORY_ID, selectedId);
                hashMap.put(Constant.AREA, null);
                hashMap.put(Constant.PRICE, null);
                hashMap.put(Constant.ORDER_TYPE, null);
                hashMap.put(Constant.ADVERTISER_TYPE, null);
                hashMap.put(Constant.NUMBER_OF_ROOM, null);
                hashMap.put(Constant.IS_COUNTRY_SIDE, null);
                hashMap.put(Constant.TRUST, null);
                hashMap.put(Constant.RENT, null);
                hashMap.put(Constant.OFFICIAL_DOCUMENT, null);
                hashMap.put(Constant.IS_IMAGE, null);
                hashMap.put(Constant.IS_EMERGENCY, null);

                for (int i = 0; i < rootViews.size(); i++)
                {
                    hashMap.putAll(rootViews.get(i).getData());
                }

                JSONObject object = new JSONObject(hashMap);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_FILTER_DATA_JSON, object.toString());
                setResult(Constant.RESULT_OK, intent);
                finish();
            }
        });

        txtClearFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HashMap hashMap = new HashMap();

                hashMap.put(Constant.CATEGORY_ID, selectedId);
                hashMap.put(Constant.AREA, null);
                hashMap.put(Constant.PRICE, null);
                hashMap.put(Constant.ORDER_TYPE, null);
                hashMap.put(Constant.ADVERTISER_TYPE, null);
                hashMap.put(Constant.NUMBER_OF_ROOM, null);
                hashMap.put(Constant.IS_COUNTRY_SIDE, null);
                hashMap.put(Constant.TRUST, null);
                hashMap.put(Constant.RENT, null);
                hashMap.put(Constant.OFFICIAL_DOCUMENT, null);
                hashMap.put(Constant.IS_IMAGE, null);
                hashMap.put(Constant.IS_EMERGENCY, null);

                JSONObject object = new JSONObject(hashMap);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_FILTER_DATA_JSON, object.toString());
                setResult(Constant.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setData(String mId)
    {
        int id = Integer.parseInt(mId.toString().trim());
        if (rootViews.size() != 0)
        {
            rootViews.clear();
        }

        if (id == 0)
        {
            rootViews.add(new TwoState("برچسب",Constant.IS_EMERGENCY,"همه","فوری"));
            rootViews.add(new TwoState("عکس",Constant.IS_IMAGE,"همه","عکس دار"));
           /* rootViews.add(new Optional("نوع آگهی دهنده","","همه","شخصی","مشاوره املاک"));
            rootViews.add(new Optional("حومه شهر","","همه","هست","نیست"));

            ArrayList<String> items = new ArrayList<>();
            items.add("همه");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق","",items));

            rootViews.add(new TwoInput("قیمت کل","","0","2000000"));*/
        }

        else if (id == 1)
        {
            rootViews.add(new TwoState("برچسب", Constant.IS_EMERGENCY, "همه", "فوری"));
            rootViews.add(new TwoState("عکس", Constant.IS_IMAGE, "همه", "عکس دار"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "همه", "شخصی", "مشاور املاک"));
        }

        else if (id == 11 || id == 113)
        {
            rootViews.add(new TwoState("برچسب", Constant.IS_EMERGENCY, "همه", "فوری"));
            rootViews.add(new TwoState("عکس", Constant.IS_IMAGE, "همه", "عکس دار"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "همه", "نیست", "هست"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "همه", "شخصی", "مشاور املاک"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "همه", "فروشی", "درخواستی"));
            rootViews.add(new TwoInput("قیمت کل", Constant.PRICE, "0", "20000000"));
            rootViews.add(new TwoInput("متراژ (متر مربع)", Constant.AREA, "0", "200000"));
        }

        else if (id == 111 || id == 112)
        {
            rootViews.add(new TwoState("برچسب", Constant.IS_EMERGENCY, "همه", "فوری"));
            rootViews.add(new TwoState("عکس", Constant.IS_IMAGE, "همه", "عکس دار"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "همه", "نیست", "هست"));

            ArrayList<String> items = new ArrayList<>();
            items.add("همه");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق", Constant.NUMBER_OF_ROOM, items));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "همه", "شخصی", "مشاور املاک"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "همه", "فروشی", "درخواستی"));
            rootViews.add(new TwoInput("قیمت کل", Constant.PRICE, "0", "20000000"));
            rootViews.add(new TwoInput("متراژ (متر مربع)", Constant.AREA, "0", "200000"));
        }

        else if (id == 12)
        {
            rootViews.add(new TwoState("برچسب", Constant.IS_EMERGENCY, "همه", "فوری"));
            rootViews.add(new TwoState("عکس", Constant.IS_IMAGE, "همه", "عکس دار"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "همه", "نیست", "هست"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "همه", "شخصی", "مشاور املاک"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "همه", "ارائه", "درخواستی"));
            rootViews.add(new TwoInput("اجاره ماهیانه", Constant.RENT, "0", "20000000"));
            rootViews.add(new TwoInput("ودیعه", Constant.TRUST, "0", "20000000"));
            rootViews.add(new TwoInput("متراژ (متر مربع)", Constant.AREA, "0", "200000"));
        }

        else if (id == 121 || id == 122)
        {
            rootViews.add(new TwoState("برچسب", Constant.IS_EMERGENCY, "همه", "فوری"));
            rootViews.add(new TwoState("عکس", Constant.IS_IMAGE, "همه", "عکس دار"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "همه", "نیست", "هست"));

            ArrayList<String> items = new ArrayList<>();
            items.add("همه");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق", Constant.NUMBER_OF_ROOM, items));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "همه", "شخصی", "مشاور املاک"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "همه", "ارائه", "درخواستی"));
            rootViews.add(new TwoInput("اجاره ماهیانه", Constant.RENT, "0", "20000000"));
            rootViews.add(new TwoInput("ودیعه", Constant.TRUST, "0", "20000000"));
            rootViews.add(new TwoInput("متراژ (متر مربع)", Constant.AREA,  "0", "200000"));
        }

        else if (id == 13)
        {
            rootViews.add(new TwoState("برچسب", Constant.IS_EMERGENCY, "همه", "فوری"));
            rootViews.add(new TwoState("عکس", Constant.IS_IMAGE, "همه", "عکس دار"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "همه", "نیست", "هست"));
            rootViews.add(new Optional("سند اداری", Constant.OFFICIAL_DOCUMENT, "همه", "ندارد", "دارد"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "همه", "شخصی", "مشاور املاک"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "همه", "فروشی", "درخواستی"));
            rootViews.add(new TwoInput("قیمت کل", Constant.PRICE, "0", "20000000"));
            rootViews.add(new TwoInput("متراژ (متر مربع)", Constant.AREA,  "0", "200000"));
        }

        else if (id == 131 || id == 132 || id == 133)
        {
            rootViews.add(new TwoState("برچسب", Constant.IS_EMERGENCY, "همه", "فوری"));
            rootViews.add(new TwoState("عکس", Constant.IS_IMAGE, "همه", "عکس دار"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "همه", "نیست", "هست"));
            rootViews.add(new Optional("سند اداری", Constant.OFFICIAL_DOCUMENT, "همه", "ندارد", "دارد"));

            ArrayList<String> items = new ArrayList<>();
            items.add("همه");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق", Constant.NUMBER_OF_ROOM, items));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "همه", "شخصی", "مشاور املاک"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "همه", "فروشی", "درخواستی"));
            rootViews.add(new TwoInput("قیمت کل", Constant.PRICE, "0", "20000000"));
            rootViews.add(new TwoInput("متراژ (متر مربع)", Constant.AREA,  "0", "200000"));
        }

        else if (id == 14)
        {
            rootViews.add(new TwoState("برچسب", Constant.IS_EMERGENCY, "همه", "فوری"));
            rootViews.add(new TwoState("عکس", Constant.IS_IMAGE, "همه", "عکس دار"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "همه", "نیست", "هست"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "همه", "شخصی", "مشاور املاک"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "همه", "ارائه", "درخواستی"));
            rootViews.add(new TwoInput("اجاره ماهیانه", Constant.RENT,  "0", "20000000"));
            rootViews.add(new TwoInput("ودیعه", Constant.TRUST,  "0", "20000000"));
            rootViews.add(new TwoInput("متراژ (متر مربع)", Constant.AREA,  "0", "200000"));
        }

        else if (id == 141 || id == 142 || id == 143)
        {
            rootViews.add(new TwoState("برچسب", Constant.IS_EMERGENCY, "همه", "فوری"));
            rootViews.add(new TwoState("عکس", Constant.IS_IMAGE, "همه", "عکس دار"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "همه", "نیست", "هست"));

            ArrayList<String> items = new ArrayList<>();
            items.add("همه");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق", Constant.NUMBER_OF_ROOM, items));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "همه", "شخصی", "مشاور املاک"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "همه", "ارائه", "درخواستی"));
            rootViews.add(new TwoInput("اجاره ماهیانه", Constant.RENT,  "0", "20000000"));
            rootViews.add(new TwoInput("ودیعه", Constant.TRUST,  "0", "20000000"));
            rootViews.add(new TwoInput("متراژ (متر مربع)", Constant.AREA,  "0", "200000"));
        }

        setupLayouts();
    }

    private void setupLayouts()
    {
        lnrInsertPoint.removeAllViews();
        for (int i = 0; i < rootViews.size(); i++)
        {
            View view = rootViews.get(i).getView();
            lnrInsertPoint.addView(view);
        }
    }
}
