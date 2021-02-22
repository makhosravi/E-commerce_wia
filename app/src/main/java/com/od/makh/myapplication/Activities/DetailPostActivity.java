package com.od.makh.myapplication.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.od.makh.myapplication.Activities.Dialog.ReportActivity;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.CustomView.RowInfo;
import com.od.makh.myapplication.Model.BuildingPostDetail;
import com.od.makh.myapplication.R;
import com.od.makh.myapplication.Utils.AppSingleton;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wiadevelopers.com.library.BannerSlider.banners.RemoteBanner;
import wiadevelopers.com.library.BannerSlider.views.BannerSlider;
import wiadevelopers.com.library.Components.CustomProgressDialog;
import wiadevelopers.com.library.DivarUtils;
import wiadevelopers.com.library.MaterialLoading.MaterialProgressBar;

public class DetailPostActivity extends AppCompatActivity
{

    private RelativeLayout include, rltvReportPost, rltvContainer;
    private NestedScrollView scrollView;
    private View transState, colorState, margin, viewNoInternetConnection;
    private LinearLayout lnrContainer;
    private TextView txtTitle, txtDate, txtExplain, txtReportPost;
    private BannerSlider bannerSlider;
    private FloatingActionButton fabInfo;
    private ImageView imgBookMark, imgBack, imgShare;
    private MaterialProgressBar progressBar;

    private String postId = null;
    private String image1 = null;
    private String image2 = null;
    private String image3 = null;
    private String image4 = null;

    private int bannerHeight = -1;

    private String userId = null;
    private boolean isLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        initialize();
    }

    private void initialize()
    {
        findViews();
        setTypefaces();
        setupActivity();
        setupListeners();
    }

    private void findViews()
    {
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        rltvContainer = (RelativeLayout) findViewById(R.id.rltvContainer);
        viewNoInternetConnection = findViewById(R.id.viewNoInternetConnection);
        progressBar = (MaterialProgressBar) findViewById(R.id.progressBar);
        transState = findViewById(R.id.transState);
        colorState = findViewById(R.id.colorState);
        margin = findViewById(R.id.margin);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtExplain = (TextView) findViewById(R.id.txtExplain);
        txtReportPost = (TextView) findViewById(R.id.txtReportPost);
        include = (RelativeLayout) findViewById(R.id.include);
        lnrContainer = (LinearLayout) findViewById(R.id.lnrContainer);
        rltvReportPost = (RelativeLayout) findViewById(R.id.rltvReportPost);
        bannerSlider = (BannerSlider) findViewById(R.id.bannerSlider);
        fabInfo = (FloatingActionButton) findViewById(R.id.fabInfo);

        imgBookMark = (ImageView) findViewById(R.id.imgBookMark);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        imgBack = (ImageView) findViewById(R.id.imgBack);
    }

    private void setTypefaces()
    {
        txtDate.setTypeface(DivarUtils.faceLight);
        txtTitle.setTypeface(DivarUtils.faceLight);
        txtExplain.setTypeface(DivarUtils.faceLight);
        txtReportPost.setTypeface(DivarUtils.faceLight);
    }

    private void setupActivity()
    {
        postId = getIntent().getStringExtra(Constant.KEY_POST_ID);
        image1 = getIntent().getStringExtra(Constant.KEY_IMAGE1);
        image2 = getIntent().getStringExtra(Constant.KEY_IMAGE2);
        image3 = getIntent().getStringExtra(Constant.KEY_IMAGE3);
        image4 = getIntent().getStringExtra(Constant.KEY_IMAGE4);

        bannerHeight = DivarUtils.widthPX;
        bannerSlider.getLayoutParams().height = bannerHeight;
        margin.getLayoutParams().height = bannerHeight;

        addBanners();
        getPostDetails();
    }

    private void addBanners()
    {
        ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

        if (image1 != null)
            bannerSlider.addBanner(new RemoteBanner(image1).setScaleType(scaleType));

        if (image2 != null)
            bannerSlider.addBanner(new RemoteBanner(image2).setScaleType(scaleType));

        if (image3 != null)
            bannerSlider.addBanner(new RemoteBanner(image3).setScaleType(scaleType));

        if (image4 != null)
            bannerSlider.addBanner(new RemoteBanner(image4).setScaleType(scaleType));

        if (image2 == null && image3 == null && image4 == null)
            bannerSlider.setInterval(0);
    }

    private void getPostDetails()
    {
        String url = Constant.BASE_URL + "DetailPost.php";

        rltvContainer.setVisibility(View.VISIBLE);
        viewNoInternetConnection.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        bannerSlider.setVisibility(View.GONE);
        fabInfo.setVisibility(View.GONE);

        JSONObject jsonData = new JSONObject();
        try
        {
            String phone = DivarUtils.readDataFromStorage(Constant.USER_PHONE,null);
            jsonData.put(Constant.USER_PHONE, phone);
            jsonData.put(Constant.KEY_POST_ID, postId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Gson gson = new Gson();
                String categoryId = null;
                try
                {
                    categoryId = response.getString(Constant.CATEGORY_ID);
                    rltvContainer.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    bannerSlider.setVisibility(View.VISIBLE);
                    fabInfo.setVisibility(View.VISIBLE);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if (getPostType(categoryId) == Constant.ID_BULDING)
                {
//                    Toast.makeText(getApplicationContext(),"building",Toast.LENGTH_SHORT).show();
                    isLoad = true;
                    BuildingPostDetail details = gson.fromJson(response.toString(),BuildingPostDetail.class);
                    txtDate.setText(details.getAdDate());
                    txtTitle.setText(details.getAdTitle());
                    txtExplain.setText(details.getAdExplain());

                    ArrayList<RowInfo> rowInfos = new ArrayList<>();

                    String price = details.getPrice();
                    if (price.equalsIgnoreCase(Constant.FREE)
                            || price.equalsIgnoreCase(Constant.AGREEMENT)
                            || price.equalsIgnoreCase(Constant.EXCHANGE))
                        rowInfos.add(new RowInfo("قیمت",price));
                    else
                        rowInfos.add(new RowInfo("قیمت",price + " تومان"));

                    userId = details.getUserId();

                    if (details.isMarked())
                        imgBookMark.setImageResource(R.drawable.ic_vector_bookmark_true);
                    else
                        imgBookMark.setImageResource(R.drawable.ic_vector_bookmark_false);

                    rowInfos.add(new RowInfo("متراژ",details.getArea()));
                    rowInfos.add(new RowInfo("تعد اتاق", details.getNumOfRoom()));
                    rowInfos.add(new RowInfo("ودیعه", details.getTrust()));
                    rowInfos.add(new RowInfo("اجاره", details.getRent()));
                    rowInfos.add(new RowInfo("سند اداری", details.getOfficialDoc()));
                    rowInfos.add(new RowInfo("نوع", details.getOrderType()));
                    rowInfos.add(new RowInfo("محل", details.getCity()));

                    lnrContainer.removeAllViews();
                    for (int i = 0; i < rowInfos.size(); i++)
                        lnrContainer.addView(rowInfos.get(i).getView());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                /*Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();*/
                scrollView.setVisibility(View.GONE);
                bannerSlider.setVisibility(View.GONE);
                rltvContainer.setVisibility(View.VISIBLE);
                viewNoInternetConnection.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                fabInfo.setVisibility(View.GONE);
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,jsonData
                ,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json; charset=utf-8");
                return hashMap;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void getUserPhone()
    {
        final String url = Constant.BASE_URL + "userPhone.php";
        final CustomProgressDialog progressDialog = new CustomProgressDialog(DetailPostActivity.this);
        progressDialog.setMessage("لطفا صبر کنید");
        progressDialog.setTextSize(16);
        progressDialog.setTypeface(DivarUtils.face);
        progressDialog.show();

        JSONObject jsonData = new JSONObject();
        try
        {
            jsonData.put(Constant.USER_ID,userId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    final String phoneNumber = "0" + response.getString(Constant.phoneNumber);
                    AlertDialog alertDialog = new AlertDialog.Builder(DetailPostActivity.this).create();
                    alertDialog.setTitle("شماره تماس");
                    alertDialog.setMessage(phoneNumber);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "تماس", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                    progressDialog.dismiss();
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),"مجددا تلاش کنید",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,jsonData,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json; charset=utf-8");
                return hashMap;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void markedPost()
    {
        final String requestUrl = Constant.BASE_URL + "mark.php";

        JSONObject jsonData = new JSONObject();
        try
        {
            String phone = DivarUtils.readDataFromStorage(Constant.USER_PHONE,null);
            jsonData.put(Constant.USER_PHONE, phone);
            jsonData.put(Constant.KEY_POST_ID, postId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    String result = response.getString(Constant.STATUS);
                    if (result.equals(Constant.MARKED))
                        imgBookMark.setImageResource(R.drawable.ic_vector_bookmark_true);
                    else if (result.equals(Constant.UN_MARKED))
                        imgBookMark.setImageResource(R.drawable.ic_vector_bookmark_false);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),"مجددا تلاش کنید",Toast.LENGTH_SHORT).show();
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,requestUrl,jsonData,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json; charset=utf-8");
                return hashMap;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private int getPostType(String categoryId)
    {
        if (categoryId.startsWith("1"))
            return Constant.ID_BULDING;
        else if (categoryId.startsWith("2"))
            return Constant.ID_VEHICLES;
        else
            return Constant.ID_NONE;
    }

    private void setupListeners()
    {
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
        {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                int diff = bannerHeight - scrollY;
                /*Log.i(G.TAG, "bannerHeight = " + bannerHeight);
                Log.i(G.TAG, "scrollY = " + scrollY);
                Log.i(G.TAG, "Diff = " + diff);*/

                if (diff < 0)
                    DivarUtils.setViewHeigth(bannerSlider, 0);
                else
                    DivarUtils.setViewHeigth(bannerSlider, DivarUtils.Px2Dp(diff));

                float upperBound = DivarUtils.getScrollViewUpperBound(v);

                float ratio = scrollY / upperBound;

                /*Log.i(G.TAG, "scrollY    = " + scrollY);
                Log.i(G.TAG, "upperBound = " + upperBound);
                Log.i(G.TAG, "ratio      = " + ratio);*/

                colorState.setAlpha(ratio);

            }
        });

        viewNoInternetConnection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getPostDetails();
            }
        });

        fabInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getUserPhone();
            }
        });

        imgBookMark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isLoad)
                    markedPost();
                else
                    Toast.makeText(getApplicationContext(),"امکان نشان کردن این پست وجود ندارد",Toast.LENGTH_SHORT).show();
            }
        });

        rltvReportPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(DetailPostActivity.this, ReportActivity.class));
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
